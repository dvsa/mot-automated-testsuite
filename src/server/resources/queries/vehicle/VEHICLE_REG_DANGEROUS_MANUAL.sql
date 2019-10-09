select veh.registration, rfrc.comment
from vehicle veh, mot_test_current mtc, mot_test_current_rfr_map rfr, mot_test_rfr_map_comment rfrc
where veh.id < 10000000
and veh.registration is not null
and mtc.vehicle_id = veh.id
and rfr.id = rfrc.id
and mtc.prs_mot_test_id is null -- only shows tests that do not have a Pass after Rectification at Station
and mtc.id = rfr.mot_test_id
and rfr.failure_dangerous = 1 -- shows results for tests that fail for a dangerous reason
and rfr.rfr_type_id = 6 -- manually entered defect
and mtc.status_id = 5 -- Failed test
and mtc.mot_test_type_id = 1 -- Normal test
and mtc.completed_date < str_to_date('20/05/2018', '%d/%m/%Y') -- competed mot tests before EURW changes
limit 10;