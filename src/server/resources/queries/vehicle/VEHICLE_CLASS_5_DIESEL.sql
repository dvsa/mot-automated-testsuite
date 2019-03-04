SELECT veh.registration, veh.vin, mtc.odometer_value
FROM vehicle veh
     JOIN mot_test_current mtc
       ON mtc.vehicle_id = veh.id
     JOIN (SELECT id
  		   FROM model_detail
  		   WHERE vehicle_class_id = 5 -- class 5 only
  		   AND fuel_type_id = 2) AS md -- 1 = Petrol, 2 = Diesel
  		   ON md.id = veh.model_detail_id
LEFT JOIN (SELECT max(submitted_date) AS subDate, vehicle_id
           FROM mot_test_current
           GROUP BY vehicle_id
           LIMIT 100) AS lmot -- Latest MOT to extract odometer reading
       ON lmot.subDate = mtc.submitted_date AND lmot.vehicle_id = mtc.vehicle_id
WHERE veh.model_detail_id = md.id
   AND veh.id = mtc.vehicle_id
   AND mtc.status_id not in (4,5) -- Not failed or active MOT tests
   AND mtc.mot_test_type_id = 1 -- Normal MOT test
   AND odometer_result_type = 'OK'
   AND mtc.odometer_value < 994999
   AND veh.registration NOT LIKE "%-%" -- exclude dodgy test data on ACPT
   AND veh.registration IS NOT NULL -- nullable in PP/Prod
   AND veh.vin IS NOT NULL -- nullable in PP/Prod
   AND NOT EXISTS (
             SELECT 1
             FROM vehicle v
             WHERE v.registration = veh.registration
             GROUP BY v.registration
             HAVING COUNT(v.registration) > 1 -- exclude where same registration has been entered as different vehicles
             )
   AND NOT EXISTS (
             SELECT 1
             FROM vehicle v
             WHERE v.vin = veh.vin
             GROUP BY v.vin
             HAVING COUNT(v.vin) > 1 -- exclude where same vin has been entered as different vehicles
             )
limit 10