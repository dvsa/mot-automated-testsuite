SELECT
  ifnull(MAX(email), 'test000000@example.com') AS lastemail
FROM
  email
WHERE
  email LIKE 'test______@example.com';