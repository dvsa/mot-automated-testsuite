select p.username, p.id, v.registration, v.vin, mtc.odometer_value, s.name
         from mot_test_current mtc, vehicle v, person p, site s
         where v.id = mtc.vehicle_id
         and p.id = mtc.person_id
         and mtc.mot_test_type_id = 1
         and mtc.`status_id`= 5
         and mtc.site_id = s.id
         and mtc.completed_date > DATE_SUB(CURDATE(), INTERVAL 7 DAY)
         and not exists (
         	select 1 from mot_test_current mtc2
         	where mtc.vehicle_id = mtc2.vehicle_id
         	and mtc2.mot_test_type_id = 1
         	and mtc2.`status_id`= 6
         	and mtc.`completed_date` < mtc2.`completed_date`)
         and not exists (
                  	select 1 from mot_test_current mtc2
                  	where mtc.vehicle_id = mtc2.vehicle_id
                  	and mtc2.mot_test_type_id = 9
                  	and mtc2.`status_id`= 6
                  	and mtc.`completed_date` < mtc2.`completed_date`)
                  	order by completed_date desc
limit 15;
