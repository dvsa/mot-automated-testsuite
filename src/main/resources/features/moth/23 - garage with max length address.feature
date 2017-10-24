@moth2
Feature: 23 - Garage with max length address

  Scenario: A MOTH user searches for a vehicle, test location has max length address
    Given I browse to /
    And I load "VEHICLE_REG_GARAGE_MAX_ADDR" as {registration}, {number}, {add1}, {add2}
    And I enter {registration} in the registration field

    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "Pass"
    And The page contains "{number}"
    And The page contains "{add1}"
    And The page contains "{add2}"
    And The page title contains "Check MOT history"