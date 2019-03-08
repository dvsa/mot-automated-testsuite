select d.registration as registration, d.vin as vin, mtc.odometer_value as mileage
from dvla_vehicle d, (
     select max(id) as id, vehicle_id  from mot_test_current
     group by vehicle_id
     limit 1000000) as latest_mot,
     mot_test_current mtc
where d.registration is not null -- nullable in PP/Prod
and d.vin is not null -- nullable in PP/Prod
and mtc.id = latest_mot.id
and d.vehicle_id = latest_mot.vehicle_id
and d.first_registration_date < '1960-01-01' -- select a vehicle which requires an optional MOT
and d.vehicle_id in (select vehicle_id from mot_test_current where expiry_date > now())
and not exists (
    select 1 from mot_test_current mtc
    where d.id = mtc.vehicle_id
    and mtc.status_id not in (4,5) -- exclude vehicles whose latest status is under test or failed
) -- BL-8277 Due to know issue, exclude vehicles that have the same id as a failed dvsa vehicle
limit 5
