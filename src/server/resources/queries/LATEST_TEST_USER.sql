select ifnull(MAX(person.username), 'VARC0000') as username
from person
where person.username like "VARC%"
