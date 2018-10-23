select d.registration as registration, d.vin as vin, 0 as mileage
       from dvla_vehicle d
       where d.registration is not null -- nullable in PP/Prod
       and d.vin is not null -- nullable in PP/Prod
       and d.first_registration_date < '1960-01-01' -- select a vehicle which requires an MOT by law
       and d.vehicle_id in (select vehicle_id from mot_test_current where expiry_date > now())
       and not exists (
           select 1 from mot_test_current mtc
           where d.id = mtc.vehicle_id
           and mtc.status_id = 5 -- failed test
           ) -- BL-8277 Due to know issue, exclude vehicles that have the same id as a failed dvsa vehicle
       limit 5