select d.registration as registration, d.vin as vin, 0 as mileage
from dvla_vehicle d
where d.vehicle_id in
    (select v.id
	from vehicle v
	where v.first_registration_date < "1960-01-01")
	and (select max(m.expiry_date)
		from mot_test_current m
		where m.vehicle_id = d.vehicle_id
		and m.mot_test_type_id = 1
		and m.status_id = 6) < now()
		limit 10;