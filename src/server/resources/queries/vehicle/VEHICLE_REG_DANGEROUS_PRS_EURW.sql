select veh.registration
from vehicle veh, mot_test_current mtc, mot_test_current_rfr_map rfr
where veh.registration is not null
and mtc.vehicle_id = veh.id
and mtc.prs_mot_test_id is not null  -- only shows tests that do have a Pass after Rectification at Station
and mtc.id = rfr.mot_test_id
and rfr.failure_dangerous=1 -- shows results for tests that fail for a dangerous reason
and rfr.rfr_type_id = 3 -- PRS failure defect
and mtc.status_id = 5 -- Failed test (part of the PRS)
and mtc.mot_test_type_id = 1 -- Normal test
and mtc.completed_date between str_to_date('20/06/2018', '%d/%m/%Y') and str_to_date('20/08/2018', '%d/%m/%Y')-- completed mot tests before EURW changes, end date restricted due to large dataset
limit 1