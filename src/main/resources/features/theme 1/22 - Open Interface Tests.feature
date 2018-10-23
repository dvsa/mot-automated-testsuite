@OpenInterfaceTests
Feature: MOT test for class 4 vehicles for open interface testing

  Scenario: 3 - Tester enters a class 4 MOT test for a DVLA vehicle without a previous MOT
    Given I load "VEHICLE_CLASS_4_NO_MOT" as {registration1}, {vin1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for DVLA vehicle {registration1}, {vin1}, {site} as class 4
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 5000
    And I enter class 4 roller results for vehicle weight of 1000 as service brake 200,200,200,200 and parking brake 100,100
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
      | VT20                                                                         |
      | {registration1}                                                              |
      | {vin1}                                                                       |


  Scenario: 4 - Tester enters a class 4 MOT test for a DVLA 1960's vehicle without a previous MOT
    Given I load "VEHICLE_CLASS_4_BEFORE_1960" as {registration1}, {vin1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for DVLA vehicle {registration1}, {vin1}, {site} as class 4
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of 5000
    And I enter class 4 roller results for vehicle weight of 1000 as service brake 200,200,200,200 and parking brake 100,100
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
      | VT20                                                                         |
      | {registration1}                                                              |
      | {vin1}                                                                       |


  Scenario: 5 - Tester enters a class 4 MOT test for a DVLA 1960's vehicle with a current MOT
    Given I load "VEHICLE_CLASS_4_BEFORE_1960_WITH_AN_MOT" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for DVLA vehicle {registration1}, {vin1}, {site} as class 4
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 1500
    And I enter class 4 roller results for vehicle weight of 1000 as service brake 200,200,200,200 and parking brake 100,100
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
      | VT20                                                                         |
      | {registration1}                                                              |
      | {vin1}                                                                       |


  Scenario: Tester enters a class 4 MOT test for a DVLA 1960's vehicle with an expired MOT
    Given I load "VEHICLE_CLASS_4_BEFORE_1960_WITH_EXPIRED_MOT" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for DVLA vehicle {registration1}, {vin1}, {site} as class 4
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 3000
    And I enter class 4 roller results for vehicle weight of 1000 as service brake 200,200,200,200 and parking brake 100,100
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
      | VT20                                                                         |
      | {registration1}                                                              |
      | {vin1}                                                                       |
