@mothpp @mothint
Feature: 12 - Check blank registration

  Scenario: A MOTH user searches for a vehicle with the registration field blank
    Given I browse to /
#    Leave the reg field blank

    When I press the "Continue" button

    Then The page contains "There was a problem"
    And The page contains "Enter the vehicle's registration"
    And The page title contains "What is the vehicle's registration number"