@smoke @regression
Feature: 00 - MOT details check - Find vehicle, confirm and start test

  Scenario: Tester enters MOT test and validates the vehicle MOT Testing details
    Given I load "VEHICLE_CLASS_4_DETAILS" as {registration1}, {vin1}, {mileage1}, {colour1}, {colour2}, {issueDate}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    When I search for a vehicle {registration1}, {vin1}, {site}
    And I select an MOT test for {registration1}, {vin1}, {site} with {colour1}, {colour2}, {issueDate}
    And The page title contains "Your home"
    And I click the "Enter test results" link
    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 58 and parking brake 16
    And I press the "Review test" button
    Then The page title contains "MOT test summary"
    And I check the registration plate {registration1} is shown within the registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the brake test summary section has "Brake results overall" of "Pass"
    And I check the defect section has "Dangerous failures" with value "None recorded"
    And I check the defect section has "Major failures" with value "None recorded"
    And I check the defect section has "Minors" with value "None recorded"
    And I check the defect section has "PRS" with value "None recorded"
    And I check the defect section has "Advisory text" with value "None recorded"
    And I record the MOT test number
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20            |
      | {registration1} |
      | {vin1}          |
    And I click the "Back to user home" link
