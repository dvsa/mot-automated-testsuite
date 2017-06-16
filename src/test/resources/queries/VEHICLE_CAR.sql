select veh.registration, veh.vin, mtc.odometer_value
from vehicle veh, model_detail md, model, make, mot_test_current mtc
where veh.model_detail_id = md.id
and md.model_id = model.id
and model.name is not null
and model.make_id = make.id
and model.name is not null
and md.vehicle_class_id = 4 -- cars only
and mtc.vehicle_id = veh.id -- with current MOT
and mtc.odometer_result_type = 'OK' -- with successful previous odometer reading