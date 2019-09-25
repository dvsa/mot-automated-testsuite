@mothpp
Feature: 14 - Check vehicle with short/long registration

  Scenario: A MOTH user searches for a vehicle with short reg
    Given I browse to /
    And I load "VEHICLE_REG_SHORT" as {registration}, {model}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page title contains "Check MOT history"

  Scenario: A MOTH user searches for a vehicle with long reg
    Given I browse to /
    And I load "VEHICLE_REG_LONG" as {registration}, {model}git add
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page title contains "Check MOT history"