@moth2
Feature: 26 - Dangerous failures are shown

  Scenario: A MOTH user searches for an MOT test with dangerous failures
    Given I browse to /
    And I load "VEHICLE_REG_DANGEROUS" as {registration}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "Fail"
    And The page contains "Dangerous"
    And The page contains "What are failures and advisories?"
    And The page title contains "Check MOT history"