@mothpp
@mothint
Feature: 07 - Check vehicle with no EU classification

  Scenario: A MOTH user searches for a vehicle with no EU classification and goes to fees table
    Given I browse to /
    And I load "VEHICLE_REG_EUCLASS_NULL" as {registration}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    And The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page contains "Check the MOT fees table to see when different vehicle types need their first MOT."
    And The page title contains "Check MOT history"
    And The page contains "First MOT due"
    And The page contains "Unknown"

    When I click the "MOT fees" link
    And I go to the next tab

    Then The page title contains "Getting an MOT: MOT costs"
    And I go to the next tab
    And I close extra tabs

    And I click the accordion section with the id "mot-history-description"
    And The page contains "This vehicle hasn't had its first MOT."