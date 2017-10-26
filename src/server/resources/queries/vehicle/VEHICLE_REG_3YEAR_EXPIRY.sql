select d.registration as registration, date_format(date_sub(adddate(d.first_registration_date, INTERVAL 3 YEAR), INTERVAL 1 DAY), "%d %M %Y") as motdue
from dvla_vehicle d
where d.vehicle_id is null -- vehicle not used in MOT application yet
and d.eu_classification = "M1"
and d.registration is not null -- nullable in PP/Prod
and d.vin is not null -- nullable in PP/Prod
and d.registration not in (
  select v.registration from vehicle v
  where v.registration is not null -- exclude same registration manually entered into the application
)
and d.vin not in (
  select v.vin from vehicle v
  where v.vin is not null -- exclude same vin manually entered into the application
)
limit 50