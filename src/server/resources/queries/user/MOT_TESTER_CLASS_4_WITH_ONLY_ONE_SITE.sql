/* Description */
/* This SQL is to obtain an active MOT tester, with  valid  2FA, who can perform an MOT test on class 4 only */


SELECT DISTINCT
   p.username,
   s.name AS site
FROM
   person p
   -- Check that user has logged in with 2fa
   JOIN person_security_card_map pscm
       ON p.id = pscm.person_id
   JOIN security_card sc
       ON pscm.security_card_id = sc.id
   LEFT JOIN security_card_drift scd
       ON sc.id = scd.security_card_id
   -- Checking tester is authorised to test
   JOIN auth_for_testing_mot aftm
       ON p.id = aftm.person_id
   -- Checking site is authorised to test
   JOIN site_business_role_map sbrm
       ON p.id = sbrm.person_id
   JOIN site s
       ON sbrm.site_id = s.id
   JOIN auth_for_testing_mot_at_site afts
       ON s.id = afts.site_id
   -- Checking AE is authorised to test
   JOIN organisation_site_map osm
       ON s.id = osm.site_id
   JOIN organisation o
       ON osm.organisation_id = o.id
   JOIN auth_for_ae afa
       ON o.id = afa.organisation_id
WHERE
   -- Check user has a username
   p.username IS NOT NULL
   -- Check the security card assigned to them is active
   AND sc.security_card_status_lookup_id = 1
   -- Check last 2FA input was within +/-2 windows
   AND NOT EXISTS (
      	SELECT
         	1
      	FROM
       	  	security_card_drift
      	WHERE
         	(last_observed_drift > 60 or last_observed_drift < -60)
   )
   -- Check user doesn’t need to claim account
   AND p.is_account_claim_required = 0
   -- Check user doesn’t need to change password
   AND p.is_password_change_required = 0
   -- User is a tester
   AND sbrm.site_business_role_id = 1
   -- Tester is active
   AND sbrm.status_id = 1
   -- Tester authorised to test
   AND aftm.status_id = 9
   -- Tester authorised to test class 4 vehicles
   AND aftm.vehicle_class_id = 4
   -- Site authorised to test class 4 vehicles
   AND afts.vehicle_class_id = 4
   -- AE is authorised to test
   AND afa.status_id = 2
   -- AE linked to site
   AND osm.status_id = 2
   -- AE has slots available
   AND o.slots_balance > 15
   -- tester is only a tester at one site
   AND p.id NOT IN (
       SELECT
           person_id
       FROM
           site_business_role_map
       WHERE
           -- User is a tester
           site_business_role_id = 1
           -- Tester is active
           AND status_id = 1
       GROUP BY
           person_id
       HAVING
           COUNT(*) > 1
   )
   -- Check users have acknowledge all special notices
   -- Check users don’t have active tests
   AND p.id NOT IN (
       SELECT
           person_id
       FROM
           special_notice
       WHERE
           is_acknowledged = 0

   UNION
       SELECT
           person_id
       FROM
           mot_test_current
       WHERE
           status_id = 4
   )
   -- Changes the ordering so the same users are not used in other SQL results
   ORDER BY p.username DESC
   LIMIT 100;