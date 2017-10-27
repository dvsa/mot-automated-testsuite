@moth2 @moth-int
Feature: 20 - Check vehicle with inverted appeal fail is not shown

  Scenario: A MOTH user searches for a vehicle with inverted appeal fail
    Given I browse to /
    And I load "VEHICLE_REG_INVAPPEAL_FAIL" as {registration}, {testno}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page does not contain "{testno}"
    And The page title contains "Check MOT history"