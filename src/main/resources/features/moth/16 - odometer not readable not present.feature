@mothpp
@mothint
Feature: 16 - Check vehicle with odometer unreadable/not present

  Scenario: A MOTH user searches for a vehicle with unreadable odometer
    Given I browse to /
    And I load "VEHICLE_REG_ODO_NOTREAD" as {registration}, {model}
    And I enter {registration} in the registration field

    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "Unreadable"
    And The page title contains "Check MOT history"

  Scenario: A MOTH user searches for a vehicle with odometer not present
    Given I browse to /
    And I load "VEHICLE_REG_ODO_NOTPRESENT" as {registration}, {model}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "Not present"
    And The page title contains "Check MOT history"