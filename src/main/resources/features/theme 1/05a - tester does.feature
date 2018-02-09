@regression
Feature: 05a - Tester does...

  @smoke
  Scenario: Tester enters a class 4 MOT test pass, with no defects and slot is consumed
    Given I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    And I get the slot count from the homepage for site {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 51 and parking brake 16
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the major failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20            |
      | {registration1} |
      | {vin1}          |
      | {site}          |
    And I click the "Back to user home" link
    And I check a slot was successfully used for site {site}


  Scenario: Tester enters a class 4 MOT test fail, with brake test failure
    Given I load "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 50 and parking brake 15
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Fail"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the major failures section of the test summary has "Parking brake efficiency below requirements"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                         |
      | {registration1}                              |
      | {vin1}                                       |
      | {site}                                       |
      | Parking brake efficiency below requirements  |


  @smoke
  Scenario: Tester enters a class 4 MOT test fail, with failure defects
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Dangerous" defect of ("Steering", "Steering column", "Modified such that the steering is adversely affected") with comment "Test defect 1"
    And I browse for a "Major" defect of ("Body, structure and attachments", "Exhaust system", "Exhaust system insecure") with comment "Test defect 2"
    And I browse for a "Major" defect of ("Lamps, reflectors and electrical equipment", "Battery(ies)", "Battery leaking") with comment "Test defect 3"
    And I enter decelerometer results of service brake 60 and parking brake 60
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the dangerous failures section of the test summary has "modified such that the steering is adversely affected"
    And I check the dangerous failures section of the test summary has "Test defect 1"
    And I check the dangerous failures section of the test summary does not have "Exhaust system insecure"
    And I check the dangerous failures section of the test summary does not have "Test defect 2"
    And I check the dangerous failures section of the test summary does not have "Battery leaking"
    And I check the dangerous failures section of the test summary does not have "Test defect 3"
    And I check the major failures section of the test summary has "Exhaust system insecure"
    And I check the major failures section of the test summary has "Test defect 2"
    And I check the major failures section of the test summary has "Battery leaking"
    And I check the major failures section of the test summary has "Test defect 3"
    And I check the major failures section of the test summary does not have "modified such that the steering is adversely affected"
    And I check the major failures section of the test summary does not have "Test defect 1"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                                   |
      | {registration1}                                        |
      | {vin1}                                                 |
      | {site}                                                 |
      | Modified such that the steering is adversely affected  |
      | Exhaust system insecure                                |
      | Battery leaking                                        |
      | Test defect 1                                          |
      | Test defect 2                                          |
      | Test defect 3                                          |


  Scenario: Tester enters a class 4 MOT test fail, with brake performance not tested
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter odometer not present
    And I search for a "Major" defect of "Brake performance not tested" with comment "test defect"
    And I check the "Add brake test" link is hidden
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Fail"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Not tested"
    And I check the major failures section of the test summary has "Brake performance not tested"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                         |
      | {registration1}              |
      | {vin1}                       |
      | {site}                       |
      | Brake performance not tested |


  Scenario: Tester enters a class 4 MOT test pass, with advisory defects
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Advisory" defect of ("Brakes", "Anti-lock braking system", "Wiring excessively damaged") with comment "Test advisory 1"
    And I browse for a "Advisory" defect of ("Non-component advisories", "Oil leak") with comment "Test advisory 2"
    And I search for a "Advisory" defect of "Stub axle has excessive vertical movement between stub axle and axle beam" with comment "Test advisory 3"
    And I search for a "Advisory" defect of "Exhaust emits excessive smoke or vapour likely to obscure the vision of other road users" with comment "Test advisory 4"
    And I add a manual advisory of "Test manual advisory"
    And I enter class 4 roller results for vehicle weight of 1000 as service brake 200,200,200,200 and parking brake 100,100
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the advisory section of the test summary has "wiring damaged but not excessively"
    And I check the advisory section of the test summary has "Oil leak"
    And I check the advisory section of the test summary has "Stub axle has slight vertical movement between stub axle and axle beam"
    And I check the advisory section of the test summary has "Exhaust emits excessive smoke or vapour but not likely to obscure the vision of other road users"
    And I check the advisory section of the test summary has "Test advisory 1"
    And I check the advisory section of the test summary has "Test advisory 2"
    And I check the advisory section of the test summary has "Test advisory 3"
    And I check the advisory section of the test summary has "Test advisory 4"
    And I check the advisory section of the test summary has "Test manual advisory"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the major failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
    | VT20                                                                                             |
    | {registration1}                                                                                  |
    | {vin1}                                                                                           |
    | {site}                                                                                           |
    | Wiring damaged but not excessively                                                               |
    | Oil leak                                                                                         |
    | Stub axle has slight vertical movement between stub axle and axle bea                         |
    | Exhaust emits excessive smoke or vapour                                                          |
    | Test advisory 1                                                                                  |
    | Test advisory 2                                                                                  |
    | Test advisory 3                                                                                  |
    | Test advisory 4                                                                                  |
    | Test manual advisory                                                                             |

