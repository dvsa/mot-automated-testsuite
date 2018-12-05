@mothpp @mothint
Feature: 08 - Check fake and invalid registration numbers

  Scenario Outline: A MOTH user searches for a vehicle with an invalid reg
    Given I browse to /
    And I enter "<reg_number>" in the registration field

    When I press the "Continue" button

    Then The page contains "There was a problem"
    And The page contains "Check that the registration you entered is correct"
    And The page title contains "What is the vehicle's registration number"

    Examples:
      | reg_number |
      | AB12ABC    |
      | ABCDEF     |
      | 12345      |
      | MISTAKE    |
      | ORPHAN     |
      | FRAUD      |