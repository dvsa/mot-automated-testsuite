@regression
Feature: 14 - Vehicle Examiner

  Scenario: VE records re-inspection
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I click the "vehicle information" link
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

  # Scenario: AE search positive
    # Select AE information
    # Provide a valid AE number
    # Ensure that home page for AE displays correct information

  # Scenario: AE search negative
    # Select AE information
    # Provide incorrect AE number / leave field empty
    # Ensure that validation is in place - AE Number was not found

  # Scenario: AE test logs
    # Select AE information
    # Search for valid AE with test history
    # Select Test logs
    # Ensure that logs are present / that filters are doing what they suppose to do / that Custom date range works / that Total tests numbers are correct / that pagination works on page / that user can download logs / that sorting is working in the results table / that backward navigation to AE home page is working

  # Scenario: Site information search positive
    # Select Site information
    # Provide Site ID / Trading name / City or town / Postcode
    # Ensure that needed site is in the list of results and Site home page can be accessed from that list

  # Scenario: Site information search negative
    # Select Site information
    # Provide incorrect data / leave all fields blank
    # Ensure that validation is in place - You need to enter some search criteria or Unable to find any matches. Try expanding your search criteria / that Help with Site Search section is expandable

  # Scenario: Site information - View active test
    # Select Site information
    # Search for a Site which has a MOT test in progress
    # Select MOT test
    # Ensure that MOT Test page displayed / that information about vehicle is correct / that information about test is correct / that user can navigate back to Site home page

  # Scenario: Site information - Abort active test
    # Select Site information
    # Search for a Site which has a MOT test in progress
    # Select MOT test
    # Abort MOT test
    # Ensure that user can't abort test without providing a reason (validation in place - Reason for cancelling test is required) / that information about MOT test is correct on Abort MOT test page / that user can navigate back to view active MOT test page / that user can abort test and print VT30 certificate

  # Scenario: User search + change tester group status
    # Select User search
    # Provide a valid username
    # Select user from the results list
    # Select Change Group A / B qualification status
    # Select Qualification status
    # Select Change status button
    # Select Confirm qualification status on a Summary and confirmation page
    # Ensure that confirmation message is displayed on a User profile page / that status was actually updated

  # Scenario: User search negative
    # Select User search
    # Provide incorrect information to any of the fields
    # Ensure that user still on User search page / that information message is displayed - Your search for fff returned no results. Check what you have entered or add more details and try again.

  # Scenario: Vehicle information search + test details
    # Select Vehicle information
    # Provide a valid VRM (ensure that vehicle has at least on test in the past)
    # Select View MOT history on a Vehicle information page
    # Select View for one of the tests on the Vehicle MOT test history page
    # Ensure that MOT test summary page is present / that user can print certificate from this page

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

  # Scenario: Record re-inspection + comparison
    # Select Vehicle information
    # Provide a valid VRM (ensure that vehicle has at least on test in the past)
    # Select View MOT history on a Vehicle information page
    # Select View for one of the tests on the Vehicle MOT test history page
    # Start re-inspection
    # Complete re-inspection with the valid data
    # Start comparison
    # Complete comparison with the valid data