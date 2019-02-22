SELECT p.username AS username, o.name AS organisation, COUNT(tst.id) AS transaction_count, ae.ae_ref  AS ae_reference
FROM person p
  -- Check that user has logged in with 2fa
  JOIN person_security_card_map pscm
    ON p.id = pscm.person_id
  JOIN security_card sc
    ON pscm.security_card_id = sc.id
  JOIN security_card_drift scd
    ON sc.id = scd.security_card_id
  -- Checking its a AEDM User
  JOIN organisation_business_role_map obrm
    ON p.id = obrm.person_id
  -- Getting organisation AE's
  JOIN organisation o
    ON obrm.organisation_id = o.id
  -- Obtaining AE reference
  JOIN auth_for_ae ae
    ON o.id = ae.organisation_id
 -- Obtaining transaction information
  JOIN test_slot_transaction tst
    ON o.id = tst.organisation_id
WHERE
  -- Check the security card assigned to them is active
  sc.security_card_status_lookup_id = 1
  -- Check last 2FA input was within +/-2 windows, this can change depending on drift as table continually updates
  AND ( scd.last_observed_drift BETWEEN -60 AND 60)
  -- Check user doesn’t need to claim account
  AND p.is_account_claim_required = 0
  -- Check user doesn’t need to change password
  AND p.is_password_change_required = 0
  -- AEDM role
  AND obrm.business_role_id = 1
  -- Person has a valid username
  AND p.username IS NOT NULL
  -- To see the actual transactions (minus amendments) use completed_on.
  AND tst.completed_on >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
GROUP BY p.username, o.name
ORDER BY transaction_count DESC
LIMIT 50;