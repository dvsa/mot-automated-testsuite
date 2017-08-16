@regression
Feature: 07 - Tester does... (part C)

  Scenario: Tester enters a class 2 MOT test fail, with colour change
    Given I load "VEHICLE_CLASS_2_RED" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_2" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1} with colour changed to "Blue"
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


  Scenario: Tester enters a class 5 MOT test pass, with engine capacity change
    Given I load "VEHICLE_CLASS_5_DIESEL" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_5" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1} with engine changed to "Petrol" with capacity 2700
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

  Scenario: Tester enters a class 7 MOT test fail
    Given I load "VEHICLE_CLASS_7" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_7" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}
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

  Scenario: Tester abandons an MOT test, for a class 3 vehicle
    Given I load "VEHICLE_CLASS_3" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_3" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And The page title contains "MOT test results"
    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Advisory" defect of ("Non-component advisories", "Spare tyre defective") with comment "Test advisory 1"
    And I click the "Cancel test" link

    And The page title contains "Cancel MOT test"
    And I click the "Inspection may be dangerous or cause damage" radio button
    And I enter "Test reason" in the "How might this be dangerous or cause damage?" field
    And I press the "Confirm and cancel test" button
    Then The page title contains "MOT test abandoned"


  Scenario: Tester enters a class 4 MOT test pass, for a DVLA vehicle
    Given I load "DVLA_VEHICLE" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for DVLA vehicle {registration1}, {vin1} as class 4
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter roller results for vehicle weight of 1000 as service brake 200,200,200,200 and parking brake 100,100
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"


  Scenario: Tester enters a class 4 MOT test pass, for a new vehicle
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    And I search for a vehicle with " ", " "
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
    And I check the fails section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
