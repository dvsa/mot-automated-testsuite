@regression
Feature: 14 - Vehicle Examiner

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
    And I check the fails section of the test summary has "None recorded"
    And I press the "Finish reinspection" button
    And The page title contains "MOT reinspection complete"
    And I click the "Compare test results" link
    And I perform a test comparison with outcome "No further action" and justification "Test was satisfactory"
    And I check the case outcome "No further action" is saved

  Scenario: Authorised Examiner Search
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "AUTHORISED_EXAMINER" as {aeNumber}, {aeName}, {slotUsage}
    When I search for AE information with {aeNumber}
    Then I check the AE name is {aeName}
    And I click the "Slot usage" link
    And The page contains "Test logs of Authorised Examiner"
    And I check the slot usage for the past 30 days is {slotUsage}

  # Scenario: AE search negative
    # Select AE information
    # Provide incorrect AE number / leave field empty
    # Ensure that validation is in place - AE Number was not found

  Scenario: Site information search and abort active test
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {tester}, {site}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I get the site number {siteNumber} by name {site}
    And I start an MOT test for {reg}, {vin}
    And I click the "Sign out" link
    And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    When I search for Site information by site number with {siteNumber}
    And I abort the active mot test at site for reg {reg}, vin {vin}

  # Scenario: Site information search negative
    # Select Site information
    # Provide incorrect data / leave all fields blank
    # Ensure that validation is in place - You need to enter some search criteria or Unable to find any matches. Try expanding your search criteria / that Help with Site Search section is expandable

  Scenario: Vehicle examiner searches for user by username
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "AEDM_USER" as {searchUser}, {organisation}
    And I search for user with username {searchUser}
    And I click the first name in the list
    And I check the user profile contains username {searchUser}
    And I change the testers group "A" status to "Qualified"

  # Scenario: User search negative
    # Select User search
    # Provide incorrect information to any of the fields
    # Ensure that user still on User search page / that information message is displayed - Your search for fff returned no results. Check what you have entered or add more details and try again.

  Scenario: Vehicle information search
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I click the "Vehicle information" link
    When I search for vehicle information by "Registration (VRM)" with {reg}
    Then I check the reg {reg}, vin {vin} on vehicle information
    And I click the "View MOT history" link
    And The page contains "Vehicle MOT test history"
    And I click the first "View" link
    And The page contains "MOT test summary"
    And I check there is a "Print certificate" link

  # Scenario: Vehicle information search negative
    # Select Vehicle information
    # Provide an invalid VRM / VIN
    # Ensure that validation message is in place - 1. Search term(s) not found...

  # Scenario: MOT tests Site
    # Select MOT tests
    # Provide a valid Site number (with existing MOT history)
    # Ensure that list of tests is displaying in the results table / that after clicking on one of the links in summary column user is being redirected to MOT test summary page

  # Scenario: MOT tests Tester
    # Select MOT tests
    # Provide a valid Username (with existing MOT history for that user)
    # Ensure that list of tests is displaying in the results table / that after clicking on one of the links in summary column user is being redirected to MOT test summary page

  # Scenario: MOT tests VRM / VIN
    # Select MOT tests
    # Provide a valid VRM / VIN (with existing MOT history for the vehicle)
    # Ensure that list of tests is displaying in the results table / that after clicking on one of the links in summary column user is being redirected to MOT test summary page

  # Scenario: MOT tests negative
    # Select MOT tests
    # Provide an invalid Site number / Username / VRM / VIN
    # Ensure that correct validation message is in place