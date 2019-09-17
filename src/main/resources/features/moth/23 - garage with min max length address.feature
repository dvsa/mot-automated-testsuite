@mothint
Feature: 23 - Garage with min and max length address details

  Scenario: A MOTH user searches for a vehicle, test location has min length address
    Given I browse to /
    And I load "VEHICLE_REG_GARAGE_MIN_ADDR" as {registration}, {number}, {add1}, {add2}, {v5c}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page contains "Pass"
    And The page contains "{number}"
    When I click the accordion section with the id "mot-history-description"
    And I click the first "View test location" text
    Then The page contains "Enter latest V5C number"
    And I enter {v5c} in the field with id "v5c-input"
    And I press the first "Show test location" button

    And The page contains "{add1}"
    And The page contains "{add2}"

  Scenario: A MOTH user searches for a vehicle, test location has max length address
    Given I browse to /
    And I load "VEHICLE_REG_GARAGE_MAX_ADDR" as {registration}, {number}, {add1}, {add2}, {v5c}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page contains "Pass"
    And The page contains "{number}"
    When I click the accordion section with the id "mot-history-description"
    And I click the first "View test location" text
    Then The page contains "Enter latest V5C number"
    Given I enter {v5c} in the field with id "v5c-input"
    And I press the first "Show test location" button

    And The page contains "{add1}"
    And The page contains "{add2}"