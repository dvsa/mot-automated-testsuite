# Theme 1 (Standard Regression), Test 7:
# Tester does:
#   MOT test
#   Retest
#   Abort
#   Add RFR Failures to a FAIL
#   Add PRS Failures to a PASS
#   Add advisory tests to both FAIL and PASS
#   Add, Edit, Remove a defect.
Feature: Tester does...

  Scenario: Tester enters an MOT test pass
    Given I load "MOT_TESTER" as <username1>
    And I load "VEHICLE_CAR" as <registration1>, <vin1>, <mileage1>
    And I login with 2FA as <username1>

    When I start an MOT test for <registration1>, <vin1>
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And The page title contains "MOT test results"
    And I click the "Add reading" link

    And The page title contains "Odometer reading"
    And I enter <mileage1> plus 5000 in the odometer field
    And I press the "Update reading" button

    And The page title contains "MOT test results"
    And I click the "Add brake test" link

    And The page title contains "Brake test configuration"
    And I select "Decelerometer" in the "Service brake test type" field
    And I select "Decelerometer" in the "Parking brake test type" field
    And I press the "Next" button

    And The page title contains "Add brake test results"
    And I enter "60" in the "Service brake" field
    And I enter "60" in the "Parking brake" field
    And I press the "Submit" button

    And The page title contains "Brake test summary"
    And I click the "Done" link

    And The page title contains "MOT test results"
    And I press the "Review test" button

    And The page title contains "MOT test summary"
    And The MOT status is "Pass"
    And I press the "Save test result" button

    Then The page title contains "MOT test complete"


  Scenario: Tester enters an MOT test fail (service brakes using decelerometer)
    Given I load "MOT_TESTER" as <username1>
    And I load "VEHICLE_CAR" as <registration1>, <vin1>, <mileage1>
    And I login with 2FA as <username1>

    When I start an MOT test for <registration1>, <vin1>
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And The page title contains "MOT test results"
    And I click the "Add reading" link

    And The page title contains "Odometer reading"
    And I enter <mileage1> plus 5000 in the odometer field
    And I press the "Update reading" button

    And The page title contains "MOT test results"
    And I click the "Add brake test" link

    And The page title contains "Brake test configuration"
    And I select "Decelerometer" in the "Service brake test type" field
    And I select "Decelerometer" in the "Parking brake test type" field
    And I press the "Next" button

    And The page title contains "Add brake test results"
    And I enter "20" in the "Service brake" field
    And I enter "20" in the "Parking brake" field
    And I press the "Submit" button

    And The page title contains "Brake test summary"
    And I click the "Done" link

    And The page title contains "MOT test results"
    And I press the "Review test" button

    And The page title contains "MOT test summary"
    And The MOT status is "Fail"
    And I press the "Save test result" button

    Then The page title contains "MOT test complete"


  Scenario: Tester aborts an MOT test
    Given I load "MOT_TESTER" as <username1>
    And I load "VEHICLE_CAR" as <registration1>, <vin1>, <mileage1>
    And I login with 2FA as <username1>

    When I start an MOT test for <registration1>, <vin1>
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And The page title contains "MOT test results"
    And I click the "Cancel test" link

    And The page title contains "Cancel MOT test"
    And I click the "Aborted by VE" radio button
    And I press the "Confirm and cancel test" button

    Then The page title contains "MOT test aborted"
