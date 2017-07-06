@regression
Feature: 7 - Tester does... (part C)

    # Scenario: Tester enters a class 2 MOT test fail, with colour change
    # Search for vehicle
    # Change colour and confirm for test
    # Check inspection sheet - Class 1 and 2 version
    # Enter odometer
    # Add Failure
    # Add decelerometer brake test (fail)
    # Check summary and complete
    # Check Certificate (dates and details - including colour update)

  # Scenario: Tester enters a class 5 MOT test pass, with engine capacity change
    # Search for vehicle
    # Change engine capacity and confirm for test
    # Check inspection sheet including engine update
    # Enter odometer
    # Add non component Advisory (should only have advisory as option)
    # Add decelerometer brake test
    # Check summary and complete
    # Check Certificate (dates and details)

  # Scenario: Tester enters a class 7 MOT test fail
    # Select vehicle start test
    # Check inspection sheet
    # Enter odometer in KM
    # Add Failure (using search)
    # Add Failure
    # Edit first failure
    # Remove first failure
    # Add Plate/Gradient brake test - fail imbalance
    # Check summary and complete
    # Check Certificate (dates and details)

  # Scenario: Tester abandons an MOT test
    # Select vehicle start test
    # Check inspection sheet
    # Enter odometer
    # Add advisory
    # Cancel test - Inspection may be dangerous or cause damage (enter reason)
    # Check certificate
    # (Admin check abandoned)

  # Scenario: Tester enters a class 4 MOT test pass, for a DVLA vehicle
    # Search for DVLA vehicle
    # Set MOT test class 4 and confirm
    # Check inspection sheet
    # Enter odometer
    # Add roller brake test (roughly check calculations)
    # Check summary and complete
    # Check Certificate (dates and details)

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
