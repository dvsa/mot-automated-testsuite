@mothpp
@mothint
  @karl1
Feature: 07 - Check vehicle with no EU classification

  Scenario: A MOTH user searches for a vehicle with no EU classification and goes to fees table
    Given I browse to /
    And I load "VEHICLE_REG_EUCLASS_NULL" as {registration}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"

    And The page title contains "Check MOT history"
    And The page contains "Unknown"
    And The page contains "MOT fees table"

    When I click the "MOT fees" link
    And I go to the next tab

    Then The page title contains "Getting an MOT: MOT costs"
    And I close extra tabs
