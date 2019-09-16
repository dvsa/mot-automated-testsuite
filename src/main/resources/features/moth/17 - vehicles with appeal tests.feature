@mothpp @mothint
Feature: 17 - Check vehicle with appeal tests

  Scenario: A MOTH user searches for a vehicle with statutory appeal pass that shows
    Given I browse to /
    And I load "VEHICLE_REG_STATAPPEAL_PASS" as {registration}, {testno}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page contains "Pass"
    And The page contains "{testno}"

  Scenario: A MOTH user searches for a vehicle with statutory appeal fail that doesn't show
    Given I browse to /
    And I load "VEHICLE_REG_STATAPPEAL_FAIL" as {registration}, {testno}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page does not contain "{testno}"

  Scenario: A MOTH user searches for a vehicle with inverted appeal pass that shows
    Given I browse to /
    And I load "VEHICLE_REG_INVAPPEAL_PASS" as {registration}, {testno}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page contains "Pass"
    And The page contains "{testno}"

  Scenario: A MOTH user searches for a vehicle with inverted appeal fail that doesn't show
    Given I browse to /
    And I load "VEHICLE_REG_INVAPPEAL_FAIL" as {registration}, {testno}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page does not contain "{testno}"
