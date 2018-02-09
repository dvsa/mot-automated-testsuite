@regression
Feature: 05d - Tester does...

  Scenario: Tester abandons an MOT test, for a class 3 vehicle
    Given I load "VEHICLE_CLASS_3" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_3" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And The page title contains "MOT test results"
    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Advisory" defect of ("Non-component advisories", "Spare tyre defective") with comment "Test advisory 1"
    And I click the "Cancel test" link

    And The page title contains "Cancel test"
    And I click the "Inspection may be dangerous or cause damage" radio button
    And I enter "Test reason" in the "How might this be dangerous or cause damage?" field
    And I press the "Cancel test" button
    Then The page title contains "MOT test abandoned"
    And I click "Print documents" and check the PDF contains:
      | VT30                                                                         |
      | {registration1}                                                              |
      | {vin1}                                                                       |
      | {site}                                                                       |


  Scenario: Tester enters a class 4 MOT test pass, for a DVLA vehicle
    Given I load "DVLA_VEHICLE" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for DVLA vehicle {registration1}, {vin1}, {site} as class 4
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
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
      | {site}                                                                       |


  Scenario: Tester enters a class 4 MOT test pass, for a new vehicle
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    And I search for a vehicle with {site}
    And I click the "create a new vehicle record" link

    And The page title contains "Make a new vehicle record"
    And I click the "Start now" link
    And I enter reg "ANDY1" and vin "111111"
    And I select make "AUDI" and model "A1"
    And I select fuel type "Petrol" and cylinder capacity 1200
    And I select Vehicle Class 4
    And I select primary colour "Blue" and secondary colour "No other colour"
    And I select country of registration "GB, UK, ENG, CYM, SCO (UK) - Great Britain"
    And I enter the date of first use as today minus 4 years

    And The page title contains "Confirm new record and start test"
    And I press the "Confirm and start test" button
    And The page title contains "MOT test started"
    And I click the "Continue to home" link

    And The page title contains "Your home"
    When I click the "Enter test results" link
    And I enter an odometer reading in kilometres of 12345
    And I enter decelerometer results of service brake 60 and parking brake 60
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of "ANDY1"
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of "111111"
    And I check the vehicle summary section of the test summary has "Colour" of "Blue"
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
      | ANDY1                                                                        |
      | 111111                                                                       |
      | {site}                                                                       |
