@moth2
Feature: 26 - Dangerous failures are shown

  Scenario: A MOTH user searches for an MOT test with dangerous failures
    Given I browse to /
#    Need to replace with details from DB
    And I load "VEHICLE_REG_DANGEROUS" as {registration}
    And I enter {registration} in the registration field

    When I press the "Continue" button
#    Replace these checks based on values from the DB
    Then The page contains "{registration}"
#    And The page contains "IVECO DAILY"
    And The page contains "Fail"
#    And The page contains "8235 7939 0011"
#    And The page contains "Nearside Front Tyre"
#    And The page contains "has ply or cords exposed (4.1.D.1b)"
    And The page contains "Dangerous"
    And The page contains "What are failures and advisories?"
#    Add extra checks based on details extracted from the DB
    And The page title contains "Check MOT history"