@regression @VEinspection
Feature: 12b - Vehicle Examiner

  Scenario: MOT test search by vin
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_CLASS_4_MOT_LAST_10_DAYS" as {reg}, {vin}, {mileage}
    And I click the "MOT tests" link
    When I search for an mot by "VIN/Chassis (comparison available)" with {vin}
    And I click the first "View" link
    Then The page contains "summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |

  Scenario: MOT test search by VRM
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_CLASS_4_LATEST" as {reg}, {vin}, {mileage}
    And I click the "MOT tests" link
    When I search for an mot by "Registration (comparison available)" with {reg}
    And I click the first "View" link
    Then The page contains "summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |

  Scenario: MOT test search by site by date range
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "SITE" as {siteNumber}, {siteName}
    And I click the "MOT tests" link
    When I search for an mot by "Site (by date range)" with {siteNumber} from 2 months ago
    And I click the first "View" link
    Then The page contains "summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |

  Scenario: MOT test search by tester by date range
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "TESTER_WITH_10_DAY_HISTORY" as {tester}
    And I click the "MOT tests" link
    When I search for an mot by "Tester (by date range)" with {tester} from 2 months ago
    And I click the first "View" link
    Then The page contains "summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |

  Scenario: MOT test search by site recent tests
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    When I start an MOT test for {registration1}, {vin1}, {site}
    And I click the "Enter test results" link
    And I enter an odometer reading in miles of {mileage1} plus 1000
    And I enter decelerometer results of service brake 92 and parking brake 85
    And I press the "Review test" button
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the registration number {registration1} is shown within the Registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the brake results section of the test summary is "Pass"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click the "Back to user home" link

    Then I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "SITE" as {siteNumber}, {siteName}
    And I click the "MOT tests" link
    When I search for an mot by "Site (recent tests)" with {siteNumber}
    And I click the first "View" link
    Then The page contains "summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |
