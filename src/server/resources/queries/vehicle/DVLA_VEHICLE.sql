select d.registration as registration, d.vin as vin, 0 as mileage
from dvla_vehicle d
where d.vehicle_id is null -- vehicle not used in MOT application yet
and d.registration is not null -- nullable in PP/Prod
and d.vin is not null -- nullable in PP/Prod
and not exists (	-- No vehicle exists in the vehicle table with same reg and vin - manually entered
	select 1 from vehicle v
	where v.registration = d.registration
	and v.vin = d.vin
	)
and not exists (
    select 1 from mot_test_current mtc
    where d.id = mtc.vehicle_id
    and mtc.status_id = 5 -- failed test
    ) -- BL-8277 Due to know issue, exclude vehicles that have the same id as a failed dvsa vehicle
limit 50