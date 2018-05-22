SELECT
  ifnull(MAX(email), 'success+test000000@simulator.amazonses.com') AS lastemail
FROM
  email
WHERE
  email LIKE 'success+test______@simulator.amazonses.com';