SELECT
  MAX(email) AS lastemail
FROM
  email
WHERE
  email LIKE 'test______@example.com';