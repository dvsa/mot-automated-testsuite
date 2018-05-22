@extended
Feature: 05f - Tester does...

  Scenario: Tester enters a class 5 MOT test fail, with a dangerous service brake test failure on a roller
    Given I load "VEHICLE_CLASS_5" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_5" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 123456
    And I enter class 5 roller results for vehicle weight of 1000 as service brake 60,60,60,60 and parking brake 100,100

    And The page contains "Service brake efficiency less than 50% of the required value"
    And The page contains "Dangerous"
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
      | 123,456 miles                                                |

  Scenario: Tester enters a class 5 MOT test fail, with a dangerous parking brake test failure on a roller
    Given I load "VEHICLE_CLASS_5" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_5" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 123456
    And I enter class 5 roller results for vehicle weight of 1000 as service brake 200,200,200,200 and parking brake 40,41

    And The page contains "Parking brake efficiency below requirements"
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
      | 123,456 miles                               |

  Scenario: Tester enters a class 4 MOT test fail, with a major brake imbalance test failure on a plate
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 12345
    And I enter class 4 plate results for weights of 1000 as service brake 130,200,200,200 and parking brake 100,100
    And The page contains "Brakes imbalanced across an axle"
    And The page contains "Front"
    And The page contains "Axle 1"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the major failures section of the test summary has "Brakes imbalanced across an axle"
    And I check the major failures section of the test summary has "Front"
    And I check the major failures section of the test summary has "Axle 1"
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
      | Front                              |
      | Axle 1                             |
      | 12,345 miles                       |

  Scenario: Tester enters a class 7 MOT test fail, with a dangerous brake imbalance test failure on a plate
    Given I load "VEHICLE_CLASS_7" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_7" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 42345
    And I enter class 7 plate results for weights of 1000 as service brake 20,200,200,200 and parking brake 100,100
    And The page contains "Brakes imbalanced more than 50% across an axle"
    And The page contains "Front"
    And The page contains "Axle 1"
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the dangerous failures section of the test summary has "Brakes imbalanced more than 50% across an axle"
    And I check the dangerous failures section of the test summary has "Front"
    And I check the dangerous failures section of the test summary has "Axle 1"
    And I check the major failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                            |
      | {registration1}                                 |
      | {vin1}                                          |
      | Do not drive until repaired (dangerous defects) |
      | Brakes imbalanced more than 50% across an axle  |
      | Front                                           |
      | Axle 1                                          |
      | 42,345 miles                                    |

  Scenario: Tester enters a class 7 MOT test fail, with a major service brake test failure on a plate
    Given I load "VEHICLE_CLASS_7" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_7" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 12345
    And I enter class 7 plate results for weights of 1000 as service brake 100,100,100,100 and parking brake 100,100
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
      | 12,345 miles                                |

  Scenario: Tester enters a class 4 MOT test fail, with a service parking brake test failure on a plate
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 12345
    And I enter class 4 plate results for weights of 1000 as service brake 20,20,20,20 and parking brake 100,100
    And The page contains "Service brake efficiency less than 50% of the required value"
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
      | 12,345 miles                                                 |

  Scenario: Tester enters a class 7 MOT test fail, with a major parking brake test failure on a plate
    Given I load "VEHICLE_CLASS_7" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_7" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 12345
    And I enter class 7 plate results for weights of 1000 as service brake 200,200,200,200 and parking brake 55,55
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
      | 12,345 miles                                |

  Scenario: Tester enters a class 4 MOT test fail, with a dangerous parking brake test failure on a plate
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 12345
    And I enter class 4 plate results for weights of 1000 as service brake 200,200,200,200 and parking brake 12,13
    And The page contains "Parking brake efficiency less than 50% of the required value"
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
      | 12,345 miles                                                 |

  Scenario: Tester enters a class 5 MOT test fail, with a major service brake test failure
    Given I load "VEHICLE_CLASS_5" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_5" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 123456
    And I enter decelerometer results of service brake 40 and parking brake 16
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
      | 123,456 miles                               |

  Scenario: Tester enters a class 4 MOT test fail, with a major parking brake test failure
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 123456
    And I enter decelerometer results of service brake 60 and parking brake 13
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
      | 123,456 miles                               |