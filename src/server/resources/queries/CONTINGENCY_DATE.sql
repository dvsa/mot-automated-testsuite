select date_format(SUBDATE(NOW(), 4) , "%d") as day,
date_format(now(), "%m") as month,
date_format(now(), "%Y") as year
from dual;