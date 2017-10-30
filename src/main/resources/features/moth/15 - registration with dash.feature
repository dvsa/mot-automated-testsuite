@mothpp
Feature: 15 - Check vehicle with dash in the registration

  Scenario: A MOTH user searches for a vehicle with dash
    Given I load "VEHICLE_REG_DASH" as {registration1}, {make}
    And I browse to /
#    Need to replace with details from DB
    And I enter {registration1} in the registration field
    When I press the "Continue" button
#    Replace these checks based on values from the DB
    Then The page contains "{registration1}"
    And The page contains "{make}"
#    Add extra checks based on details extracted from the DB
    And The page title contains "Check MOT history"