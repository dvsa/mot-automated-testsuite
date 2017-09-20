select p.username, o.name, s.name
from site s, organisation o, person p, organisation_business_role_map obrm,
  (select max(id) as id, site_id from enforcement_site_assessment group by site_id) as latest_assessment,
  enforcement_site_assessment esa, security_card sc, person_security_card_map pscm, security_card_drift scd
where latest_assessment.id = esa.id
and latest_assessment.site_id = s.id
and esa.id = latest_assessment.id
and esa.site_assessment_score > 270.00
and esa.site_assessment_score <= 360.00
and o.id = s.organisation_id
and obrm.organisation_id = o.id
and obrm.business_role_id = 1
and obrm.status_id = 1
and p.id = obrm.person_id
and p.id = pscm.person_id
and sc.id = pscm.security_card_id
and sc.security_card_status_lookup_id = 1
and scd.security_card_id = pscm.security_card_id
and scd.last_observed_drift between -60 and 60
and p.username is not null
group by s.id
order by esa.site_assessment_score desc
limit 10