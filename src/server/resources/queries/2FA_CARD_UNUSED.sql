select sc.serial_number
from security_card sc
where sc.security_card_status_lookup_id = 3 -- unused card
limit 10