SELECT veh.registration, veh.vin, mtc.odometer_value
FROM vehicle veh, model_detail md, model mo, make ma, mot_test_current mtc
INNER JOIN (SELECT  vehicle_id, max(id) AS id
			FROM mot_test_current
	 	    WHERE created_on > current_date - interval '0' day -- only current certificates can be pulled
 	   		GROUP BY vehicle_id) AS mtcId
 	  ON mtc.id = mtcId.id
WHERE veh.model_detail_id = md.id
AND mtc.status_id = 6 -- Passed tests only
AND mtc.document_id IS NOT NULL  -- exclude where there are no MOT certificates
AND mtc.mot_test_type_id in (4,5,6,7) -- Latest Reinspection Test
AND md.vehicle_class_id = 4 -- cars only
AND veh.id = mtc.vehicle_id
AND md.model_id = mo.id
AND mo.make_id = ma.id
AND mtc.status_id = 6 -- status pass
AND odometer_result_type = 'OK'
AND veh.registration not like "%-%" -- exclude dodgy test data on ACPT
AND veh.registration is not null -- nullable in PP/Prod
AND veh.vin is not null -- nullable in PP/Prod
AND not exists (
    SELECT 1 FROM vehicle v
    WHERE v.registration = veh.registration
    GROUP BY v.registration
    HAVING count(v.registration) > 1 -- exclude where same registration has been entered as different vehicles
)
AND not exists (
    SELECT 1 FROM vehicle v
    WHERE v.vin = veh.vin
    GROUP BY v.vin
    HAVING count(v.vin) > 1 -- exclude where same vin has been entered as different vehicles
)
LIMIT 10