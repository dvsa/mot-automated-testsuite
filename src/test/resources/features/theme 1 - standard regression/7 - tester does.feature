@regression
Feature: 7 - Tester does...

  Scenario: Tester enters a class 4 MOT test pass, with no defects
    Given I load "MOT_TESTER_CLASS_4" as {username1}, {site}
    And I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as {username1}

    When I start an MOT test for {registration1}, {vin1}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading of {mileage1} plus 5000
    And I enter decelerometer results of service brake 60 and parking brake 60
    # TODO: add more explicit checks (for all tests) on summary page
    # TODO: add explicit checks on generated PDF (if possible?)
    Then The completed test status is "Pass"


  Scenario: Tester enters a class 4 MOT test fail, with brake test failure
    Given I load "MOT_TESTER_CLASS_4" as {username1}, {site}
    And I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as {username1}

    When I start an MOT test for {registration1}, {vin1}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading of {mileage1} plus 5000
    And I enter decelerometer results of service brake 20 and parking brake 20
    Then The completed test status is "Fail"


  Scenario: Tester enters a class 4 MOT test fail, with failure defects
    Given I load "MOT_TESTER_CLASS_4" as {username1}, {site}
    And I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as {username1}

    When I start an MOT test for {registration1}, {vin1}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading of {mileage1} plus 5000
    And I add a "Failure" defect of ("Steering", "Steering operation", "Steering system excessively tight") with comment "Test defect 1"
    # TODO: add a couple more failures
    And I enter decelerometer results of service brake 60 and parking brake 60
    Then The completed test status is "Fail"


  # Scenario: Tester enters a class 4 MOT test fail, with brake performance not tested
    # Enter odometer not present
    # Add brake performance not tested (using search journey)
    # Check can not enter brake test results


  # Scenario: Tester enters a class 4 MOT test pass, with advisory defects
    # Add advisory and manual advisory defects
    # roller brake tests


  # Scenario: Tester enters a class 4 MOT test pass, with advisory and PRS defects
    # Add advisory and PRS defects
    # gradient brake tests


  # Scenario: Tester enters a class 4 MOT test fail, add edit and remove advisory, PRS and failure defects
    # Add advisory
    # Add PRS
    # Edit PRS
    # Add Failure
    # Add Failure
    # Add Failure with location
    # Remove first failure
    # Edit second failure - set dangerous and add comment


  Scenario: Tester aborts an MOT test
    Given I load "MOT_TESTER_CLASS_4" as {username1}, {site}
    And I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as {username1}

    When I start an MOT test for {registration1}, {vin1}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And The page title contains "MOT test results"
    And I click the "Cancel test" link

    And The page title contains "Cancel MOT test"
    And I click the "Aborted by VE" radio button
    And I press the "Confirm and cancel test" button
    Then The page title contains "MOT test aborted"


  Scenario: Tester enters a class 4 MOT retest pass, all failures repaired, no need to repeat brake test
    Given I load "MOT_TESTER_CLASS_4" as {username1}, {site}
    And I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA as {username1}

    And I start an MOT test for {registration1}, {vin1}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading of {mileage1} plus 5000
    And I add a "Failure" defect of ("Steering", "Steering operation", "Steering system excessively tight") with comment "Test defect 1"
    And I enter decelerometer results of service brake 60 and parking brake 60
    And The completed test status is "Fail"

    When I click the "Back to user home" link
    And I start an MOT retest for {registration1}, {vin1}
    And The page title contains "Your home"
    And I click the "Enter retest results" link

    And I enter an odometer reading of {mileage1} plus 5000
    And I mark the defect "Steering system excessively tight" as repaired
    Then The completed retest status is "Pass"

  # Scenario: Tester enters a class 4 MOT retest fail, with brake test re-entry
    # ..enter fail with several failure defects
    # Enter odometer
    # Repair all but 1 failure
    # Brake test reentry required due to previous failure (review test button disabled) - test brake lock

  # Scenario: Tester enters a class 4 MOT retest pass, with brake test re-entry
    # ..enter fail with several failure defects and no brake test
    # Enter odometer
    # Repair all failures
    # Brake test reentry required as not previously entered (review test button disabled)

  # Scenario: Tester enters a class 1 MOT test pass
    # Select vehicle start test
    # Check inspection sheet - Class 1 and 2 version
    # Enter odometer (check for odometer warnings)
    # Add roller brake test (roughly check calculations)
    # Check summary and complete
    # Check Certificate (dates and details)

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

  # Scenario: Tester enters a class 4 MOT test pass, for a new vehicle
    # Search for vehicle that does not exist ANDY1 / 111111
    # Enter required fields then confirm for test
    # Check inspection sheet
    # Enter odometer in KM
    # Add failure
    # Add Decelerometer brake test
    # Check summary and complete
    # Check Certificate (dates and details)

  # TODO: group common passes/fails into scenario examples