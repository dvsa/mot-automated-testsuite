@mothpp
@mothint
Feature: 27 - Dangerous PRS are shown

  Scenario: A MOTH user searches for an MOT test with dangerous PRS
    Given I browse to /
    And I load "VEHICLE_REG_DANGEROUS_PRS" as {registration}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "Pass"
    And The page contains "Fail"
    And The page contains "Dangerous"
    And The page title contains "Check MOT history"