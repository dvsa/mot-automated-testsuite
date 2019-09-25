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

  Scenario Outline: A MOTH user searches for a reg with invalid symbols
    Given I browse to /
    And I enter "<reg_number>" in the registration field
    When I press the "Continue" button

    Then The page contains "There was a problem"
    And The page contains "The registration can only contain letters and numbers"
    And The page title contains "What is the vehicle's registration number"

    Examples:
      | reg_number |
      | AB!?ABC    |
      | A<>,.      |

  Scenario: A MOTH user searches for a vehicle reg with blank spaces
    Given I browse to /
    And I load "VEHICLE_REG_WITH_SPACE" as {registration}, {regspace}, {model}
    And I enter {regspace} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page title contains "Check MOT history"

  Scenario: A MOTH user searches for a reg longer than 14 characters
    Given I browse to /
    And I enter "ABCDEFGHIJ12345" in the registration field
    When I press the "Continue" button

    Then The page contains "There was a problem"
    And The page contains "The input is more than 14 characters long"
    And The page title contains "What is the vehicle's registration number"

  Scenario: A MOTH user searches for a vehicle with the registration field blank
    Given I browse to /
#    Leave the reg field blank
    When I press the "Continue" button

    Then The page contains "There was a problem"
    And The page contains "Enter the vehicle's registration"
    And The page title contains "What is the vehicle's registration number"