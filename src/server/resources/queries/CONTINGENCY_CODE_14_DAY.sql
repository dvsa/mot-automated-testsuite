select number as ContingencyCode,
       date_format(start_date, "%e %M %Y") as issue_date,
       date_format(start_date, "%d.%m.%Y") as date_of_test,
       date_format(start_date, "%d") as day,
       date_format(start_date, "%m") as month,
       date_format(start_date, "%Y") as year
from emergency_log
where start_date = SUBDATE(CURDATE(), INTERVAL 14 DAY)
