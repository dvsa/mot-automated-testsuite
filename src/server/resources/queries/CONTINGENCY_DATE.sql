select date_format(SUBDATE(NOW(), INTERVAL 4 DAY)  , "%d") as day,
date_format(SUBDATE(NOW(), INTERVAL 4 DAY) , "%m") as month,
date_format(SUBDATE(NOW(), INTERVAL 4 DAY) , "%Y") as year
from dual;