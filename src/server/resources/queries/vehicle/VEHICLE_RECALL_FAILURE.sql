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
and ma.name = 'CITROEN' -- show results where the manufacture is CITROEN, as this cause the fake SMMT service to return a failure message
and length(veh.vin) > 5 -- the VIN number needs to be at least 5 chars long
limit 10