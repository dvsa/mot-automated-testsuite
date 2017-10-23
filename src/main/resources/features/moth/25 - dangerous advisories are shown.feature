@moth2
Feature: 25 - Dangerous advisories are shown

  Scenario: A MOTH user searches for an MOT test with dangerous advisories
    Given I browse to /
    And I load "VEHICLE_REG_DANGEROUS_ADVISE" as {registration}
    And I enter {registration} in the registration field

    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "Pass"
    And The page contains "Dangerous"
    And The page contains "What are advisories?"
    And The page title contains "Check MOT history"