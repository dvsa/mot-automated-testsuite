SELECT
  MAX(person.username) AS username
FROM
  person
WHERE
  person.username LIKE "TEST%"
