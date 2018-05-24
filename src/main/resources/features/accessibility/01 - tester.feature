@accessibility
Feature: Tester accessibility reports

  Scenario: Accessibility report for login page
    When I browse to /
    Then The page title contains "Sign in"
    And I create an accessibility report "login"

  Scenario: Accessibility report for test homepage and your profile
    When I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then The page title contains "Your home"
    And I create an accessibility report "tester_homepage"
    Then I click the "Your profile" link
    And I create an accessibility report "tester_your_profile"
    Then I click the "Roles and associations" link
    And I create an accessibility report "tester_roles_associations"
    Then I click the "Return to your profile" link
    And I click the "Test logs" link
    And I create an accessibility report "tester_test_logs"
    Then I click the "Return to Your profile" link
    And I click the "Test quality information" link
    And I create an accessibility report "tester_tqi"

  Scenario: Tester enters a class 4 MOT test pass of a DVLA vehicle
    Given I load "DVLA_VEHICLE" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    When I start an MOT test for DVLA vehicle {registration1}, {vin1}, {site} as class 4
    And I click the "Enter test results" link
    And I create an accessibility report "tester_mot_test_results"
    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 51 and parking brake 16
    And I press the "Review test" button
    And I create an accessibility report "tester_mot_test_summary"
    And I press the "Save test result" button
    And I create an accessibility report "tester_mot_test_complete"