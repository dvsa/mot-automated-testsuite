select v.registration, ma.name dvsa_make, mo.name dvsa_model
from dvla_vehicle v
left join dvla_make dma on v.make_code = dma.code
left join dvla_model dmo on v.model_code = dmo.code and dmo.make_code = v.make_code
left join dvla_model_model_detail_code_map dmap on dmap.dvla_make_code = dmo.make_code and dmap.dvla_model_code = dmo.code
left join make ma on dmap.make_id = ma.id
left join model mo on dmap.model_id = mo.id
where v.id < 1000000
and v.vehicle_id is null -- vehicle not used in MOT application yet
and v.registration is not null -- nullable in PP/Prod
and v.vin is not null -- nullable in PP/Prod
and length(v.vin) > 5
and ma.name is not null
and mo.name is not null
and (v.eu_classification = 'L1' or v.eu_classification = 'N1')-- DVLA cars and vans only
and ma.name = 'MERCEDES' -- show results where the manufacture is not part of the fake SMMT service
limit 10;