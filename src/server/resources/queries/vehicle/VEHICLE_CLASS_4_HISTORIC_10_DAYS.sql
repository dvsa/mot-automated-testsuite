SELECT veh.registration, veh.vin, mtc.odometer_value mtc.number AS number
 FROM vehicle AS veh
 JOIN (SELECT ROUND(RAND() * (SELECT MAX(id) FROM vehicle)) AS id ) AS x       ON veh.id >= x.id
 JOIN mot_test_current AS mtc                                                  ON veh.id = mtc.vehicle_id
 JOIN model_detail AS md                                                       ON veh.model_detail_id = md.id
WHERE veh.vin IS NOT NULL
  AND veh.registration NOT IN ('R3GHAU5', 'R3GHA01', 'R3GHDVL5', 'DVLA903')                                                         -- Exclude vehicles that have already been modified by automation
  AND veh.registration NOT LIKE '%-%'                                                                                               -- exclude dodgy test data on ACPT
  AND NOT EXISTS (
     SELECT 1 FROM vehicle v
     WHERE v.registration = veh.registration
     GROUP BY v.registration
     HAVING COUNT(v.registration) > 1                                                                                               -- exclude where same registration has been entered as different vehicles
     )
   AND NOT EXISTS (
     SELECT 1 FROM vehicle v
     WHERE v.vin = veh.vin
     GROUP BY v.vin
     HAVING COUNT(v.vin) > 1                                                                                                        -- exclude where same vin has been entered as different vehicles
     )
  AND mtc.submitted_date = (SELECT MAX(mtc1.submitted_date) FROM mot_test_current mtc1 WHERE mtc1.vehicle_id = mtc.vehicle_id)
  AND mtc.mot_test_type_id = 1                                                                                                      -- Normal MOT test
  AND mtc.odometer_result_type = 'OK'
  AND mtc.odometer_value < 994999
  AND mtc.status_id = 6                                                                                                             -- Passed MOT tests
  AND md.vehicle_class_id = 4                                                                                                       -- cars only
  AND DATE(mtc.issued_date) < date_sub(CURDATE(), INTERVAL 14 DAY) -- Oldest certificate older than 10 days
  AND DATE(mtc.issued_date) > date_sub(CURDATE(), INTERVAL 4 YEAR) -- Testers can only see 4 years worth
  AND DATE(mtc.issued_date) != CURDATE()
LIMIT 50;