select v.registration
from vehicle v
where not exists (
    select 1
    from mot_test_current m
    where m.vehicle_id = v.id
    and m.status_id = 6
)
limit 5;