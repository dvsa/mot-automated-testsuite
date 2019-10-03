select veh.registration
from vehicle veh, mot_test_current mtc, mot_test_current_rfr_map rfr, model_detail md
where veh.registration is not null
and veh.model_detail_id = md.id
and md.vehicle_class_id = 4 -- cars only
and mtc.vehicle_id = veh.id
and mtc.prs_mot_test_id is null -- only shows tests that do not have a Pass after Rectification at Station
and mtc.id = rfr.mot_test_id
and rfr.failure_dangerous = 1 -- shows results for tests that fail for a dangerous reason
and rfr.rfr_type_id = 2 -- failure defect
and mtc.status_id = 5 -- Failed test
and mtc.mot_test_type_id = 1 -- Normal test
and mtc.completed_date > str_to_date('20/05/2018', '%d/%m/%Y') -- competed mot tests before EURW changes
and not exists (select 1 from mot_test_current_rfr_map ads where mtc.id = ads.mot_test_id and ads.rfr_type_id in (1, 6)) -- no advisories
limit 10