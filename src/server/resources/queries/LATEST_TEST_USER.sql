select ifnull(MAX(person.username), 'TEST0000') as username
from person
where person.username like "VARC%"
