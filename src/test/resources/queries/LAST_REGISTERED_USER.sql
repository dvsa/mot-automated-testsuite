SELECT
  MAX(person.username) AS lastusername
FROM
  person
WHERE
  person.username LIKE "TEST%"
