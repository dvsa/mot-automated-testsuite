@regression
Feature: 12a - Vehicle Examiner

  Scenario: VE records re-inspection
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I click the "Vehicle information" link
    And I search for vehicle information by "Registration (VRM)" with {registration1}
    And I click the "View MOT history" link
    And I click the first "View" link
    When I start a "Targeted Reinspection" test
    And I enter an odometer reading in miles of {mileage1} plus 1000
    And I enter decelerometer results of service brake 60 and parking brake 60
    Then I press the "Review test" button
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the major failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Finish reinspection" button
    And The page title contains "MOT reinspection complete"
    And I click "Print documents" and check the PDF contains:
      | VT32            |
      | {registration1} |
      | {vin1}          |
    And I click the "Compare test results" link
    And I perform a test comparison with outcome "No further action" and justification "Test was satisfactory"
    And I check the case outcome "No further action" is saved

  Scenario: Authorised Examiner Search
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "AUTHORISED_EXAMINER" as {aeNumber}, {aeName}, {slotUsage}
    When I search for AE information with {aeNumber}
    Then I check the AE name is {aeName}
    And I click the "Slot usage" link
    And The page contains "Test slot usage"
    And I check the slot usage for the past 30 days is {slotUsage}

  Scenario: Site information search and abort active test
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {tester}, {site}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I get the site number {siteNumber} by name {site}
    And I start an MOT test for {reg}, {vin}, {site}
    And I click the "Sign out" link
    And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    When I search for Site information by site number with {siteNumber}
    And I abort the active mot test at site for reg {reg}, vin {vin}
    And I click "Print certificate" and check the PDF contains:
      | VT30            |
      | {reg}           |
      | {vin}           |

  Scenario: Vehicle examiner searches for user by username
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "AEDM_USER" as {searchUser}, {organisation}
    And I search for user with username {searchUser}
    And I click the first name in the list
    And I check the user profile contains username {searchUser}
    And I change the testers group "A" status to "Qualified"

  Scenario: Vehicle information search
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_CLASS_4_MOT_LAST_10_DAYS" as {reg}, {vin}, {odo}
    And I click the "Vehicle information" link
    When I search for vehicle information by "Registration (VRM)" with {reg}
    Then I check the reg {reg}, vin {vin} on vehicle information
    And I click the "View MOT history" link
    And The page contains "Vehicle MOT test history"
    And The page does not contain "Test date/time utc"
    And I click the first "View" link
    And The page contains "MOT test summary" or "MOT re-test summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |

  Scenario: Vehicle information search table
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_CLASS_4_AFTER_2010" as {reg}, {vin}, {odo}
    And I click the "Vehicle information" link
    When I search for vehicle information by "Registration (VRM)" with {reg}
    Then I check the reg {reg}, vin {vin} on vehicle information
    And I click the "View MOT history" link
    And The page contains "Vehicle MOT test history"
    And The page does not contain "Test date/time utc"
    And The page contains "Filter"
    And The page contains "Test date/time"
    And The page contains "Result"
    And The page contains "Summary"
    And The page contains "Test"
    And The page contains "VIN/Chassis"
    And The page contains "Registration"
    And The page contains "Make"
    And The page contains "Model"
    And The page contains "Type"
    And The page contains "Site"
    And The page contains "User ID"
    And The page contains "Previous"
    And The page contains "Next"


