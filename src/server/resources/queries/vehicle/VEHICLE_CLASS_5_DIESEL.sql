SELECT veh.registration, veh.vin, lmot.odometer_value
FROM vehicle veh
	JOIN (SELECT id, vehicle_class_id
  		  FROM model_detail
  		  WHERE vehicle_class_id = 5 -- class 5 only
  		  AND fuel_type_id = 2 -- 1 = Petrol, 2 = Diesel
  		  LIMIT 100 ) AS md
		ON md.id = veh.model_detail_id
   JOIN (SELECT vehicle_id, MAX(submitted_date) AS subDate
         FROM mot_test_current
         WHERE mot_test_type_id IN (1,9) -- Normal MOT test
         AND odometer_result_type = 'OK'
         AND vehicle_weight_source_lookup_id IN (3,9,13)
		 AND odometer_value < 994999
   		 GROUP BY vehicle_id
   		  LIMIT 10000 -- change this value to speed up the sql query or increase the total amount of records required
            		 ) AS mtc -- Find last submitted MOT date
        ON mtc.vehicle_id = veh.id
     -- Latest MOT date linked back to extract odometer reading
 	JOIN mot_test_current lmot ON mtc.subDate = lmot.submitted_date AND lmot.vehicle_id = mtc.vehicle_id
		  WHERE lmot.status_id NOT IN (4,5) -- Not failed or active MOT tests
   			AND lmot.mot_test_type_id IN (1,9) -- Normal MOT test
   			AND lmot.odometer_result_type = 'OK'
   			AND lmot.odometer_value < 994999
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