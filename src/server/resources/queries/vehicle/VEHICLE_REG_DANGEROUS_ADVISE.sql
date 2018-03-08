select veh.registration
from vehicle veh, mot_test_current mtc, mot_test_current_rfr_map rfr
where veh.id < 50000000
and veh.registration is not null
and mtc.vehicle_id = veh.id
and mtc.prs_mot_test_id is null  -- only shows tests that do not have a Pass after Rectification at Station
and mtc.id = rfr.mot_test_id
and rfr.rfr_type_id=1 -- Advisory reason for Reason for Rejection
and rfr.failure_dangerous=1 -- shows results for tests that fail for a dangerous reason
and mtc.vehicle_id = veh.id
and mtc.status_id=6 -- MOT test passed
limit 10