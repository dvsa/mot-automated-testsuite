@mothpp  @mothint
Feature: 06 - DVLA vehicle with EU classification

  Scenario: A MOTH user searches for a vehicle with a 3 year due date
    Given I browse to /
    And I load "VEHICLE_REG_3YEAR_EXPIRY" as {registration}, {motdue}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{motdue}"
    And The page title contains "Check MOT history"
    And The page contains "This vehicle hasn't had its first MOT"
    And The page contains "About this date"

    When I click the last "About this date" text

    Then The page contains "If this vehicle is a taxi"

  Scenario: A MOTH user searches for a vehicle with a 1 year due date
    Given I browse to /
    And I load "VEHICLE_REG_1YEAR_EXPIRY" as {registration}, {motdue}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{motdue}"
    And The page title contains "Check MOT history"
    And The page contains "This vehicle hasn't had its first MOT"
    And The page contains "About this date"

    When I click the last "About this date" text

    Then The page contains "If this vehicle is a taxi"
