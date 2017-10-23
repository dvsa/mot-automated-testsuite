@moth2
Feature: 19 - Check vehicle with inverted appeal pass

  Scenario: A MOTH user searches for a vehicle with inverted appeal pass
    Given I browse to /
    And I load "VEHICLE_REG_INVAPPEAL_PASS" as {registration}, {testno}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "Pass"
    And The page contains "{testno}"
    And The page title contains "Check MOT history"