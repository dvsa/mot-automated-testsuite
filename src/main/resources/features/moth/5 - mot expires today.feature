@mothpp
Feature: 05 - Check vehicle with MOT that expires today

  Scenario: A MOTH user searches for a vehicle with an MOT that expires today
    Given I browse to /
    And I load "VEHICLE_REG_EXPIRES_TODAY" as {registration}, {model}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "This vehicle's MOT expires soon"