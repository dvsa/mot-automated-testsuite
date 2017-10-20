@moth2
Feature: 03 - Check vehicle with MOT about to expire

  Scenario: A MOTH user searches for a vehicle with an MOT about to expire
    Given I browse to /
    And I load "VEHICLE_REG_DUE_TOMORROW" as {registration}, {model}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "This vehicle's MOT expires soon"