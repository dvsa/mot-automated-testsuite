@regression
Feature: 12b - Vehicle Examiner

  @browserstack
  Scenario: MOT test search by vin
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I click the "MOT tests" link
    When I search for an mot by "VIN/Chassis (comparison available)" with {vin}
    And I click the first "View" link
    Then The page contains "MOT test summary"
    And I check there is a "Print certificate" link

  Scenario: MOT test search by VRM
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I click the "MOT tests" link
    When I search for an mot by "Registration (comparison available)" with {reg}
    And I click the first "View" link
    Then The page contains "MOT test summary"
    And I check there is a "Print certificate" link

  Scenario: MOT test search by site
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "SITE" as {siteNumber}, {siteName}
    And I click the "MOT tests" link
    When I search for an mot by "Site (by date range)" with {siteNumber} from 2 months ago
    And I click the first "View" link
    Then The page contains "MOT test summary"
    And I check there is a "Print certificate" link

  Scenario: MOT test search by tester
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "TESTER_WITH_2_MONTH_HISTORY" as {tester}
    And I click the "MOT tests" link
    When I search for an mot by "Tester (by date range)" with {tester} from 2 months ago
    And I click the first "View" link
    Then The page contains "MOT test summary"
    And I check there is a "Print certificate" link
