SELECT veh.registration, veh.vin, mtc.odometer_value, mtc.number AS number
FROM vehicle veh, model_detail md, mot_test_current mtc
INNER JOIN (SELECT count(vehicle_id) as test_count, min(id) as id, vehicle_id
			from mot_test_current
			WHERE created_on > CURRENT_DATE - INTERVAL 4 YEAR -- only current certificates can be pulled
 	   		GROUP BY vehicle_id
 	   		limit 100000) AS oldest_mot
 	   		ON mtc.id = oldest_mot.id
where veh.model_detail_id = md.id
and md.vehicle_class_id = 4 -- cars only
and veh.id = oldest_mot.vehicle_id
and mtc.id = oldest_mot.id
and oldest_mot.test_count > 1 -- More than one test for the vehicle
and mtc.status_id = 6 -- Passed tests only
and odometer_result_type = 'OK'
and veh.registration not like "%-%" -- exclude dodgy test data on ACPT
and veh.registration is not null -- nullable in PP/Prod
and veh.registration NOT IN ('R3GHAU5','DVLA903','DVLA904') -- Exclude vehicles that have already been modified by automation
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
and DATE(mtc.issued_date) < date_sub(CURDATE(), INTERVAL 14 DAY) -- Oldest certificate older than 10 days
and DATE(mtc.issued_date) > date_sub(CURDATE(), INTERVAL 4 YEAR) -- Testers can only see 4 years worth
AND DATE(mtc.issued_date) != CURDATE()
limit 50