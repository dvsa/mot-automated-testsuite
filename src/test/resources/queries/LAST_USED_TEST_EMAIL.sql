SELECT
  ifnull(MAX(email), 0) AS lastemail
FROM
  email
WHERE
  email LIKE 'test______@example.com';