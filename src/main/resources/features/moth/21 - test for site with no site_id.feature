@mothpp @mothint
Feature: 21 - No site id

  Scenario: A MOTH user searches for a test where there is no site id
    Given I browse to /
    And I load "VEHICLE_REG_SITEID_NULL" as {registration}, {testno}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page does not contain "{testno}"
    And The page title contains "Check MOT history"