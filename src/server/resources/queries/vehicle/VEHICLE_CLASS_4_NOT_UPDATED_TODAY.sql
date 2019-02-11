SELECT veh.registration, veh.vin, mtc.odometer_value
FROM vehicle veh, model_detail md, model mo, make ma, mot_test_current mtc
INNER JOIN (SELECT  vehicle_id, max(id) AS id
			FROM mot_test_current
	 	    WHERE created_on > current_date - interval '1' month -- only current certificates can be pulled
 	   		GROUP BY vehicle_id) AS mtcId
 	  ON mtc.id = mtcId.id
where veh.model_detail_id = md.id
AND mtc.document_id IS NOT NULL  -- exclude where there are no MOT certificates
AND veh.id = mtc.vehicle_id
and md.vehicle_class_id = 4 -- cars only
and mtc.status_id = 6 -- Passed tests only
and odometer_result_type = 'OK'
and veh.registration not like "%-%" -- exclude dodgy test data on ACPT
and veh.registration is not null -- nullable in PP/Prod
and veh.vin is not null -- nullable in PP/Prod
and not exists (
    select 1 from vehicle v
    where v.registration = veh.registration
    group by v.registration
    having count(v.registration) > 1 -- exclude where same registration has been entered as different vehicles
)
and not exists (
    select 1 from vehicle v
    where v.vin = veh.vin
    group by v.vin
    having count(v.vin) > 1 -- exclude where same vin has been entered as different vehicles
)
and not exists (
	select 1 from mot_test_current mtc2
	where mtc2.vehicle_id = veh.id
	and mtc2.completed_date = CURDATE() -- test not completed in today
	)
 and veh.last_updated_on < CURDATE() -- vehicles not updated today
limit 5