@extended
Feature: 05i - Tester does...

  Scenario: Tester enters a class 4 MOT test fail, with a single line decelerometer major brake failure
    Given I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 65555
    And I enter single line decelerometer results of service brake 40 and parking brake 100
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

  Scenario: Tester enters a class 4 MOT test fail, with a single line decelerometer dangerous brake failure
    Given I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 55448
    And I enter single line decelerometer results of service brake 14 and parking brake 100
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

  Scenario: Tester enters a class 4 MOT test fail, with a single line decelerometer major parking brake failure
    Given I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 65333
    And I enter single line decelerometer results of service brake 90 and parking brake 20
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

  Scenario: Tester enters a class 4 MOT test fail, with a single line decelerometer dangerous parking brake failure
    Given I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 55431
    And I enter single line decelerometer results of service brake 90 and parking brake 10
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

  Scenario: Tester enters a class 4 MOT test fail, with a single line gradient major parking brake failure
    Given I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 65333
    And I enter single line decelerometer service brake result of 60 and gradient parking brake result of "Fail"
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