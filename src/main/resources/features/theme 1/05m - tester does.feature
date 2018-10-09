@extended
Feature: 05m - Tester does...

  Scenario: Tester enters a class 4 MOT test fail with a major brake imbalance failure on a single line 3 axle vehicle using a plate test
    Given I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 55555
    And I enter single line 3 axle class 4 plate results for vehicle weight of 1000 as service brake 200,200,200,200,200,130 and parking brake 200,200
    And The page contains "Brakes imbalanced across an axle"
    And The page contains "Rear"
    And The page contains "Axle 3"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the major failures section of the test summary has "Brakes imbalanced across an axle"
    And I check the major failures section of the test summary has "Axle 3"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                               |
      | {registration1}                    |
      | {vin1}                             |
      | Repair immediately (major defects) |
      | Brakes imbalanced across an axle   |
      | Rear                               |
      | Axle 3                             |
      | 55,555 miles                       |

  Scenario: Tester enters a class 5 MOT test fail with a dangerous brake imbalance failure on a single line 3 axle vehicle using a plate test
    Given I load "VEHICLE_CLASS_5" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_5" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 55445
    And I enter single line 3 axle class 7 plate results for vehicle weight of 1000 as service brake 200,10,200,200,200,200 and parking brake 200,200
    And The page contains "Brakes imbalanced across an axle by more than 50%"
    And The page contains "Dangerous"
    And The page contains "Front"
    And The page contains "Axle 1"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the dangerous failures section of the test summary has "Brakes imbalanced across an axle by more than 50%"
    And I check the dangerous failures section of the test summary has "Axle 1"
    And I check the major failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                              |
      | {registration1}                                   |
      | {vin1}                                            |
      | Do not drive until repaired (dangerous defects)   |
      | Brakes imbalanced across an axle by more than 50% |
      | Front                                             |
      | Axle 1                                            |
      | 55,445 miles                                      |

  Scenario: Tester enters a class 7 MOT test fail with a major brake failure on a single line 3 axle vehicle using a plate test
    Given I load "VEHICLE_CLASS_7" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_7" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 65555
    And I enter single line 3 axle class 7 plate results for vehicle weight of 1000 as service brake 50,50,50,50,50,50 and parking brake 200,200
    And The page contains "Service brake efficiency below requirements"
    And The page contains "Location not recorded"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the major failures section of the test summary has "Service brake efficiency below requirements"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                        |
      | {registration1}                             |
      | {vin1}                                      |
      | Repair immediately (major defects)          |
      | Service brake efficiency below requirements |
      | 65,555 miles                                |

  Scenario: Tester enters a class 4 MOT test fail with a dangerous brake failure on a single line 3 axle vehicle using a plate test
    Given I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 55448
    And I enter single line 3 axle class 4 plate results for vehicle weight of 1000 as service brake 20,20,20,20,20,20 and parking brake 200,200
    And The page contains "Service brake efficiency less than 50% of the required value"
    And The page contains "Dangerous"
    And The page contains "Location not recorded"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the dangerous failures section of the test summary has "Service brake efficiency less than 50% of the required value"
    And I check the major failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                                         |
      | {registration1}                                              |
      | {vin1}                                                       |
      | Do not drive until repaired (dangerous defects)              |
      | Service brake efficiency less than 50% of the required value |
      | 55,448 miles                                                 |

  Scenario: Tester enters a class 7 MOT test fail with a major parking brake failure on a single line 3 axle vehicle using a plate test
    Given I load "VEHICLE_CLASS_7" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_7" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 65333
    And I enter single line 3 axle class 7 plate results for vehicle weight of 1000 as service brake 100,100,100,100,100,100 and parking brake 60,60
    And The page contains "Parking brake efficiency below requirements"
    And The page contains "Location not recorded"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the major failures section of the test summary has "Parking brake efficiency below requirements"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                        |
      | {registration1}                             |
      | {vin1}                                      |
      | Repair immediately (major defects)          |
      | Parking brake efficiency below requirements |
      | 65,333 miles                                |

  Scenario: Tester enters a class 4 MOT test fail with a dangerous parking imbalance failure on a single line 3 axle vehicle using a plate test
    Given I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 55431
    And I enter single line 3 axle class 4 plate results for vehicle weight of 1000 as service brake 100,100,100,100,100,100 and parking brake 24,24
    And The page contains "Parking brake efficiency less than 50% of the required value"
    And The page contains "Dangerous"
    And The page contains "Location not recorded"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the dangerous failures section of the test summary has "Parking brake efficiency less than 50% of the required value"
    And I check the major failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                                         |
      | {registration1}                                              |
      | {vin1}                                                       |
      | Do not drive until repaired (dangerous defects)              |
      | Parking brake efficiency less than 50% of the required value |
      | 55,431 miles                                                 |