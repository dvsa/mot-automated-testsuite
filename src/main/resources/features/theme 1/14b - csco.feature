@regression
Feature: 14b - CSCO

  Scenario: CSCO user performs certificate search by vin, and is able to print duplicate certificate
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "VEHICLE_CLASS_4_MOT_LAST_10_DAYS" as {reg}, {vin}, {mileage}
    And I search for certificates with vin {vin}
    And I click the first "View certificate" link
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |


  Scenario: CSCO user performs MOT test search by site, and is able to view a recent test certificate
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "SITE_RECENT" as {siteNumber}, {siteName}
    And I click the "MOT tests" link
    When I search for an mot by "Site (recent tests)" with {siteNumber}
    And I click the first "View" link
    Then The page contains "MOT test summary" or "MOT re-test summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |

  Scenario: CSCO user performs MOT test search by tester, and is able to view test certificates
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load immediately "TESTER_WITH_10_DAY_HISTORY" as {tester}
    And I click the "MOT tests" link
    When I search for an mot by "Tester (by date range)" with {tester} from 2 months ago
    And I click the first "View" link
    Then The page contains "MOT test summary" or "MOT re-test summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |


  Scenario: CSCO user performs MOT test search by vin, and view the test details
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "VEHICLE_CLASS_4_MOT_LAST_10_DAYS" as {reg}, {vin}, {mileage}
    And I click the "MOT tests" link
    When I search for an mot by "VIN/Chassis (comparison available)" with {vin}
    And I click the first "View" link
    Then The page contains "MOT test summary" or "MOT re-test summary"
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin}
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |


  Scenario: CSCO user performs MOT test search by vrm/registration, and view the test details
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "VEHICLE_CLASS_4_MOT_LAST_10_DAYS" as {reg}, {vin}, {mileage}
    And I click the "MOT tests" link
    When I search for an mot by "Registration (comparison available)" with {reg}
    And I click the first "View" link
    Then The page contains "MOT test summary" or "MOT re-test summary"
    And I check the vehicle summary section of the test summary has "Registration number" of {reg}
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |