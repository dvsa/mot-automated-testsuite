select distinct(person_tester.username) as tester_username,
  concat_ws(' ', person_tester.first_name,
     person_tester.middle_name, person_tester.family_name) as tester_name,
  person_mgr.username as mgr_username, s.name as site_name, s.site_number as site_number
from person person_mgr, person person_tester, auth_for_testing_mot aftm, organisation o,
  organisation_site_map osm, site s, auth_for_testing_mot_at_site afts,
  site_business_role_map sbrm_mgr, site_business_role_map sbrm_tester,
  auth_for_ae afa, security_card sc, person_security_card_map pscm
where s.id = sbrm_mgr.site_id
and sbrm_mgr.person_id = person_mgr.id
and sbrm_mgr.site_business_role_id = 2 -- site manager
and sbrm_mgr.status_id = 1 -- site manager role accepted
and not exists (
  select 1 from site_business_role_map sbrm
  where s.id = sbrm.site_id
  and person_mgr.id = sbrm.person_id
  and sbrm.site_business_role_id = 1 -- exclude any managers that are also testers
)
and aftm.person_id = person_tester.id
and aftm.vehicle_class_id = 4 -- only cars
and aftm.status_id = 9 -- only qualified testing authorisations
and person_tester.is_account_claim_required = 0
and person_tester.is_password_change_required = 0
and o.id = osm.organisation_id
and s.id = osm.site_id
and s.id = afts.site_id
and afts.vehicle_class_id = 4 -- only cars
and afts.status_id = 2 -- only valid site authorisations
and sbrm_tester.site_id = s.id
and sbrm_tester.person_id = person_tester.id
and sbrm_tester.site_business_role_id = 1 -- only testers
and sbrm_tester.status_id = 1 -- tester role accepted
and afa.organisation_id = o.id
and afa.status_id = 2 -- only valid ae authorisations
and o.slots_balance > 0 -- ae's with slots available
and person_tester.id = pscm.person_id
and sc.id = pscm.security_card_id
and sc.security_card_status_lookup_id = 1 -- only assigned cards
and not exists ( -- not all security_card have a corresponding security_card_drift
  select 1 from security_card_drift scd
  where sc.id = scd.security_card_id
  and (scd.last_observed_drift >= 60 or scd.last_observed_drift <= -60) -- no drift forward or back > 1 hour
)
and not exists (
  select 1 from mot_test_current mtc
  where person_tester.id = mtc.last_updated_by
  and mtc.status_id = 4 -- exclude any testers with active tests
)
and person_tester.username is not null -- exclude dodgy test data
and person_mgr.username is not null -- exclude dodgy test data
limit 50