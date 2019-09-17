@mothpp @mothint
Feature: 06 - DVLA vehicle with and with EU classifications

  Scenario: A MOTH user searches for a expired vehicle with a 3 year due date
    Given I browse to /
    And I load "VEHICLE_REG_3YEAR_EXPIRY" as {registration}, {motdue}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    And The page contains "This vehicle's MOT is overdue"
    And The page contains "You can be fined up to £1000 for driving without a valid MOT"
    And The page contains "This vehicle may be MOT exempt, for more information refer to MOT exemption guidance"
    And The page contains "{registration}"
    And The page contains "First MOT due"
    And The page contains "{motdue}"
    And The page title contains "Check MOT history"
    And The page contains "About this date"
    When I click the last "About this date" text

    Then The page contains "If this vehicle is a taxi, ambulance or private passenger vehicle over 9 seats, it will need to be tested earlier, at one year after the registration date"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "This vehicle hasn't had its first MOT."
    And The page contains "The MOT test changed on 20 May 2018"
    And The page contains "Defects are now categorised according to their severity – dangerous, major, and minor. Find out more"
    And I click the last "Find out more" link

    And I go to the next tab
    Then The page title contains "MOT rule changes: 20 May 2018 - GOV.UK"
    And I go to the next tab
    And I close extra tabs

  Scenario: A MOTH user searches for a expired vehicle with a 1 year due date
    Given I browse to /
    And I load "VEHICLE_REG_1YEAR_EXPIRY" as {registration}, {motdue}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    And The page contains "This vehicle's MOT is overdue"
    And The page contains "You can be fined up to £1000 for driving without a valid MOT"
    And The page contains "This vehicle may be MOT exempt, for more information refer to MOT exemption guidance"
    And The page contains "{registration}"
    And The page contains "First MOT due"
    And The page contains "{motdue}"
    And The page title contains "Check MOT history"
    And The page contains "About this date"
    When I click the last "About this date" text

    Then The page contains "If this vehicle is a taxi, ambulance or private passenger vehicle over 9 seats, it will need to be tested earlier, at one year after the registration date"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "This vehicle hasn't had its first MOT."

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