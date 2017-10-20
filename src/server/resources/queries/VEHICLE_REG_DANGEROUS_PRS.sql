select veh.registration
from vehicle veh, mot_test_current mtc, mot_test_current_rfr_map rfr
where veh.registration is not null
and mtc.vehicle_id = veh.id
and mtc.prs_mot_test_id is not null  -- only shows tests that do have a Pass after Rectification at Station
and mtc.id = rfr.mot_test_id
and rfr.failure_dangerous=1 -- shows results for tests that fail for a dangerous reason
limit 10