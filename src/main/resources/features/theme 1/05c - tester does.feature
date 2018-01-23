@regression
Feature: 05c - Tester does...

  Scenario: Tester enters a class 4 MOT retest pass, with brake test re-entry
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    And I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Failure" defect of ("Body, structure and general items", "Engine mountings", "Engine mounting missing") with comment "Test defect 1"
    And I browse for a "Failure" defect of ("Brakes", "ABS", "Anti-lock braking system component missing") with comment "Test defect 2"
    And I search for a "Failure" defect of "Brake performance not tested" with comment "Test defect 3"
    And I press the "Review test" button

    And The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Not tested"
    And I check the fails section of the test summary has "engine mounting missing"
    And I check the fails section of the test summary has "Anti-lock braking system component missing"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                       |
      | Engine mounting missing                    |
      | Anti-lock braking system component missing |
      | {registration1}                            |
      | {vin1}                                     |
      | {site}                                     |

    When I click the "Back to user home" link
    And I start an MOT retest for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter retest results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I mark the defect "engine mounting missing" as repaired
    And I mark the defect "Anti-lock braking system component missing" as repaired
    And I mark the defect "Brake performance not tested" as repaired

    And I check the "Review test" button is disabled
    And I enter decelerometer results of service brake 60 and parking brake 30
    And I press the "Review test" button

    Then The page title contains "MOT re-test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT re-test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20            |
      | {registration1} |
      | {vin1}          |
      | {site}          |
    And I click the "Back to user home" link


  Scenario: Tester enters a class 1 MOT test pass
    Given I load "VEHICLE_CLASS_1" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_1" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter group a plate results for weights of 100,120,80 as 120,120,120,120
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20            |
      | {registration1} |
      | {vin1}          |
      | {site}          |
    And I click the "Back to user home" link


  Scenario: Tester enters a class 2 MOT test fail, with colour change
    Given I load "VEHICLE_CLASS_2_RED" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_2" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site} with colour changed to "Blue"
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Failure" defect of ("Motorcycle driving controls", "Throttle", "Throttle operating incorrectly") with comment "Test defect 1"
    And I enter decelerometer results of efficiency 15
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the vehicle summary section of the test summary has "Colour" of "Blue"
    And I check the brake results section of the test summary is "Fail"
    And I check the fails section of the test summary has "Throttle operating incorrectly"
    And I check the fails section of the test summary has "Test defect 1"
    And I check the fails section of the test summary has "Decelerometer brake test neither brake control achieves an efficiency of 25%"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                                                         |
      | Blue                                                                         |
      | Throttle operating incorrectly                                               |
      | Test defect 1                                                                |
      | {registration1}                                                              |
      | {vin1}                                                                       |
      | {site}                                                                       |


  Scenario: Tester enters a class 5 MOT test pass, with engine capacity change
    Given I load "VEHICLE_CLASS_5_DIESEL" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_5" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site} with engine changed to "Petrol" with capacity 2700
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Advisory" defect of ("Non-component advisories", "Nail in tyre") with comment "Test advisory 1"
    And I enter decelerometer results of service brake 60 and parking brake 60
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the vehicle summary section of the test summary has "Fuel type" of "Petrol"
    And I check the brake results section of the test summary is "Pass"
    And I check the advisory section of the test summary has "Nail in tyre"
    And I check the advisory section of the test summary has "Test advisory 1"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20                                                                         |
      | Nail in tyre                                                                 |
      | Test advisory 1                                                              |
      | {registration1}                                                              |
      | {vin1}                                                                       |
      | {site}                                                                       |


  Scenario: Tester enters a class 7 MOT test fail
    Given I load "VEHICLE_CLASS_7" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_7" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in kilometres of 30000
    And I search for a "Failure" defect of "Steering system excessively tight" with comment "Test failure 1"
    And I edit the "Failure" defect of "Steering system excessively tight" with comment "Edited failure 1" and is dangerous
    And I remove the "Failure" defect of "Steering system excessively tight"
    And I enter class 7 roller results for vehicle weight of 1000 as service brake 350,50,50,350 and parking brake 350,50
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the fails section of the test summary does not have "Steering system excessively tight"
    And I check the fails section of the test summary does not have "Test failure 1"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                                                         |
      | {registration1}                                                              |
      | {vin1}                                                                       |
      | {site}                                                                       |
