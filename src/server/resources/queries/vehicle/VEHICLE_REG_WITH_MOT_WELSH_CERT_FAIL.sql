select veh.registration, concat(ma.name, ' ', mo.name) as make, concat(left(mtc.number,4), ' ', substring(mtc.number,5,4), ' ', right(mtc.number,4)) as testno, dvla.recent_v5_document_number
from vehicle veh, mot_test_current mtc, model_detail md, model mo, make ma, dvla_vehicle dvla, jasper_document jd, site s, (SELECT MAX(submitted_date) AS max_date,
               vehicle_id
          FROM mot_test_current
         GROUP BY vehicle_id
       LIMIT 5000) as lmot -- Latest MOT
where veh.registration is not null
and lmot.max_date = mtc.submitted_date
and lmot.vehicle_id = mtc.vehicle_id
and mtc.site_id = s.id
and s.dual_language = 1 -- Welsh certificate
and mtc.vehicle_id = veh.id
and veh.model_detail_id = md.id
and veh.dvla_vehicle_id = dvla.dvla_vehicle_id
and md.model_id = mo.id
and mo.make_id = ma.id
and mtc.document_id is not null
and mtc.mot_test_type_id = 1 -- Normal test
and mtc.status_id = 5 -- Failed test
and ma.name is not null
and mo.name is not null
and ma.name not like "	%" -- exclude dodgy test data on PREP
and ma.name not like " 	%" -- exclude dodgy test data on PREP, not the same as the above line
and ma.name not like " %" -- exclude dodgy test data on PREP
and ma.name not like "." -- exclude dodgy test data on PREP
and ma.name not like "" -- exclude dodgy test data on PREP
and mo.name not like "" -- exclude dodgy test data on PREP
and veh.registration not like "%-%" -- exclude dodgy test data on PREP
and veh.registration is not null -- nullable in PP/Prod
and veh.vin is not null -- nullable in PP/Prod
and mtc.completed_date > str_to_date('20/05/2018', '%d/%m/%Y') -- competed mot tests before EURW changes
limit 1;