@mothpp
Feature: 04 - Check vehicle with just expired MOT

  Scenario: A MOTH user searches for a vehicle with just expired MOT
    Given I browse to /
    And I load "VEHICLE_REG_JUST_EXPIRED" as {registration}, {model}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "This vehicle's MOT has expired"
    And The page contains "You can be fined up to £1000 for driving without a valid MOT"