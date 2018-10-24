select d.registration as registration, d.vin as vin, mtc.odometer_value as mileage
from dvla_vehicle d, (
     select max(id) as id, vehicle_id  from mot_test_current
     group by vehicle_id
     limit 100000) as latest_mot,
     mot_test_current mtc
where mtc.id = latest_mot.id
and d.vehicle_id = latest_mot.vehicle_id
and d.vehicle_id in
    (select v.id
	from vehicle v
	where v.first_registration_date < "1960-01-01")
	and (select max(m.expiry_date)
		from mot_test_current m
		where m.vehicle_id = d.vehicle_id
	    and m.mot_test_type_id = 1
		and m.status_id = 6
		) < now() -- expired MOT
		limit 10;
