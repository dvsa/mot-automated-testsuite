@mothpp @mothint
Feature: 05 - Check correct vehicle registration variations

  Scenario: A MOTH user searches for a vehicle reg with lowercase letters
    Given I browse to /
    And I load "VEHICLE_REG_LOWERCASE" as {registration}, {reglcase}, {model}
    And I enter {reglcase} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page title contains "Check MOT history"

  Scenario: A MOTH user searches for a vehicle with short registration
    Given I browse to /
    And I load "VEHICLE_REG_SHORT" as {registration}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page title contains "Check MOT history"

  Scenario: A MOTH user searches for a vehicle with long registration
    Given I browse to /
    And I load "VEHICLE_REG_LONG" as {registration}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page title contains "Check MOT history"

  Scenario: A MOTH user searches for a vehicle with dash
    Given I load "VEHICLE_REG_DASH" as {registration1}, {make}
    And I browse to /
    And I enter {registration1} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration1}"
    And The page contains "{make}"
    And The page title contains "Check MOT history"