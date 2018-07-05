@mothpp @mothint
Feature: 25 - Dangerous advisories are shown

  Scenario: A MOTH user searches for an MOT test with dangerous advisories
    Given I browse to /
    And I load "VEHICLE_REG_DANGEROUS_ADVISE" as {registration}
    And I enter {registration} in the registration field

    When I press the "Continue" button
    Then The page contains "{registration}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "Pass"
    And The page contains "Dangerous"
    And The page contains "What are advisories?"
    And I click the "What are advisories?" help link
    And The page contains "Advisory items are provided for advice. For some of these, if they became more serious, your vehicle may no longer be roadworthy and could require immediate attention."
    And The page title contains "Check MOT history"