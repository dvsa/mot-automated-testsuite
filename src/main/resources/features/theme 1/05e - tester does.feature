@regression
Feature: 05e - Tester does...

  Scenario: Tester enters a class 7 MOT test fail, with a dangerous service brake test failure
    Given I load "VEHICLE_CLASS_7" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_7" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 123456
    And I enter decelerometer results of service brake 24 and parking brake 16
    And The page contains "Service brake efficiency less than 50% of the required value"
    And The page contains "Dangerous"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the fails section of the test summary has "Service brake efficiency less than 50% of the required value"
    And I check the fails section of the test summary has "Dangerous"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                                         |
      | {registration1}                                              |
      | {vin1}                                                       |
      | {site}                                                       |
      | Service brake efficiency less than 50% of the required value |
      | * DANGEROUS *                                                |
      | 123456 mi                                                    |


  Scenario: Tester enters a class 4 MOT test fail, with a dangerous parking brake test failure
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 12345
    And I enter class 4 roller results for vehicle weight of 1000 as service brake 200,200,200,200 and parking brake 35,39
    And The page contains "Parking brake efficiency less than 50% of the required value"
    And The page contains "Dangerous"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the fails section of the test summary has "Parking brake efficiency less than 50% of the required value"
    And I check the fails section of the test summary has "Dangerous"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                                         |
      | {registration1}                                              |
      | {vin1}                                                       |
      | {site}                                                       |
      | Parking brake efficiency less than 50% of the required value |
      | * DANGEROUS *                                                |
      | 12345 mi                                                     |


  Scenario: Tester enters a class 4 MOT test fail, with brake imbalance failure
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 55555
    And I enter class 4 roller results for vehicle weight of 1000 as service brake 200,200,200,130 and parking brake 100,100
    And The page contains "Brakes imbalanced across an axle"
    And The page contains "Rear"
    And The page contains "Axle 2"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the fails section of the test summary has "Brakes imbalanced across an axle"
    And I check the fails section of the test summary has "Axle 2"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                             |
      | {registration1}                  |
      | {vin1}                           |
      | {site}                           |
      | Brakes imbalanced across an axle |
      | Rear                             |
      | Axle 2                           |
      | 55555 mi                         |


  Scenario: Tester enters a class 4 MOT test fail, with steered axle imbalance (dangerous)
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in kilometres of 99999
    And I enter class 4 roller results for vehicle weight of 1000 as service brake 200,99,200,200 and parking brake 100,100
    And The page contains "Brakes imbalanced across an axle by more than 50%"
    And The page contains "Front"
    And The page contains "Axle 1"
    And The page contains "Dangerous"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the fails section of the test summary has "Brakes imbalanced across an axle by more than 50%"
    And I check the fails section of the test summary has "Axle 1"
    And I check the fails section of the test summary has "Dangerous"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                              |
      | {registration1}                                   |
      | {vin1}                                            |
      | {site}                                            |
      | Brakes imbalanced across an axle by more than 50% |
      | Front                                             |
      | Axle 1                                            |
      | * DANGEROUS *                                     |
      | 99999 km                                          |


  Scenario: Tester enters a class 4 MOT test pass, with brake imbalance 40kg rule
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 100000
    And I enter class 4 roller results for vehicle weight of 1000 as service brake 300,300,40,20 and parking brake 100,100
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20                             |
      | {registration1}                  |
      | {vin1}                           |
      | {site}                           |
      | 100000 mi                        |


  Scenario: Tester enters a class 4 MOT test pass, with minor defects
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Minor" defect of ("Lamps, reflectors and electrical equipment", "Daytime running lamps", "Daytime running lamp insecure") with comment "Test browse minor"
    And I search for a "Minor" defect of "Brake hose slightly damaged" with comment "Test search minor"
    And I enter decelerometer service brake result of 60 and gradient parking brake result of "Pass"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the minors section of the test summary has "Daytime running lamp insecure"
    And I check the minors section of the test summary has "Test browse minor"
    And I check the minors section of the test summary has "Brake hose slightly damaged"
    And I check the minors section of the test summary has "Test search minor"

    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20                          |
      | {registration1}               |
      | {vin1}                        |
      | {site}                        |
      # Commented out until the new certificate is in place which includes minor defects
      #| Daytime running lamp insecure |
      #| Test browse minor             |
      #| Brake hose slightly damaged   |
      #| Test search minor             |