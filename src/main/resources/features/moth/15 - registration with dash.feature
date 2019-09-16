@mothpp @mothint
Feature: 15 - Check vehicle with dash in the registration

  Scenario: A MOTH user searches for a vehicle with dash
    Given I load "VEHICLE_REG_DASH" as {registration1}, {make}
    And I browse to /
    And I enter {registration1} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration1}"
    And The page contains "{make}"
    And The page title contains "Check MOT history"