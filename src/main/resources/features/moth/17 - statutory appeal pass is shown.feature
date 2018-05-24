@mothpp
Feature: 17 - Check vehicle with statutory appeal pass

  Scenario: A MOTH user searches for a vehicle with statutory appeal pass
    Given I browse to /
    And I load "VEHICLE_REG_STATAPPEAL_PASS" as {registration}, {testno}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "Pass"
    And The page contains "{testno}"
    And The page title contains "Check MOT history"