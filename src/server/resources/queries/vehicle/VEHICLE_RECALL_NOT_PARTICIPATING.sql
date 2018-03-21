select veh.registration, ma.name, mo.name
from vehicle veh, model_detail md, model mo, make ma
where veh.id < 10000000
and veh.registration is not null
and veh.model_detail_id = md.id
and md.model_id = mo.id
and mo.make_id = ma.id
and md.vehicle_class_id = 4 -- cars only
and length(ma.name) > 0
and length(mo.name) > 0
and veh.registration != "740LOT" -- broken data in PP, removed to test correctly
and ma.name not in (select smm.make from smmt_make_map smm) -- this is set to return a non participating manufacture
and length(veh.vin) > 5 -- the VIN number needs to be at least 5 chars long
and veh.registration not like "%-%" -- exclude dodgy test data on ACPT
limit 10