@moth2
Feature: 08 - Check fake and invalid registration numbers

  Scenario: A MOTH user searches for a vehicle with an invalid reg
    Given I browse to /
    And I enter "AB12ABC" in the registration field

    When I press the "Continue" button

    Then The page contains "There was a problem"
    And The page contains "Check that the registration you entered is correct"
    And The page title contains "What is the vehicle's registration number"

  Scenario: A MOTH user searches for a vehicle with an invalid reg - just letters
    Given I browse to /
    And I enter "ABCDEF" in the registration field

    When I press the "Continue" button

    Then The page contains "There was a problem"
    And The page contains "Check that the registration you entered is correct"
    And The page title contains "What is the vehicle's registration number"

  Scenario: A MOTH user searches for a vehicle with an invalid reg - just numbers
    Given I browse to /
    And I enter "12345" in the registration field

    When I press the "Continue" button

    Then The page contains "There was a problem"
    And The page contains "Check that the registration you entered is correct"
    And The page title contains "What is the vehicle's registration number"

