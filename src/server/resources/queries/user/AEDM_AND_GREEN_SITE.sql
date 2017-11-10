select p.username, o.name, s.name
from site s, organisation o, person p, organisation_business_role_map obrm,
  enforcement_site_assessment esa, security_card sc, person_security_card_map pscm
where s.last_site_assessment_id = esa.id
and esa.site_assessment_score <= 270.00
and o.id = s.organisation_id
and exists (
  select 1 from auth_for_testing_mot_at_site aft
  where aft.site_id = s.id
  and aft.vehicle_class_id in (3, 4, 5, 7) -- group B
)
and obrm.organisation_id = o.id
and obrm.business_role_id = 1
and obrm.status_id = 1
and p.id = obrm.person_id
and p.id = pscm.person_id
and sc.id = pscm.security_card_id
and sc.security_card_status_lookup_id = 1 -- only assigned cards
and not exists ( -- not all security_card have a corresponding security_card_drift
  select 1 from security_card_drift scd
  where sc.id = scd.security_card_id
  and (scd.last_observed_drift > 60 or scd.last_observed_drift < -60) -- no drift beyond +/-2
)
and p.username is not null
and exists (select 1 from site_business_role_map sbrm where sbrm.site_id = s.id and sbrm.site_business_role_id = 1) -- At least one tester exists
group by s.id
order by esa.site_assessment_score desc
limit 10