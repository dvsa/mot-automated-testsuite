@moth2 @moth-int
Feature: 27 - Dangerous PRS are shown

  Scenario: A MOTH user searches for an MOT test with dangerous PRS
    Given I browse to /
#    Need to replace with details from DB
    And I load "VEHICLE_REG_DANGEROUS_PRS" as {registration}
    And I enter {registration} in the registration field

    When I press the "Continue" button
#    Replace these checks based on values from the DB
    Then The page contains "{registration}"
#    And The page contains "BMW 318D SE"
    And The page contains "Pass"
    And The page contains "Fail"
#    And The page contains "6244 8410 8421"
#    And The page contains "Front Windscreen wiper"
#    And The page contains "does not clear the windscreen effectively"
    And The page contains "Dangerous"
#    Add extra checks based on details extracted from the DB
    And The page title contains "Check MOT history"