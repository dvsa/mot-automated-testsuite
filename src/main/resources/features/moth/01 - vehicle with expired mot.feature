@mothpp @mothint @karl
Feature: 01 - Check vehicle with expired MOT

  Scenario: A MOTH user searches for a vehicle with an expired Pre EU roadworthiness MOT
    Given I browse to /
    And I load "VEHICLE_REG_MOT_EXPIRED" as {registration}, {model}, {date}, {mot_expiry}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page title contains "Check MOT history"
    And The page contains "This vehicle's MOT has expired"
    And The page contains "You can be fined up to £1000 for driving without a valid MOT"
    And The page contains "This vehicle may be MOT exempt, for more information refer to MOT exemption guidance"
    And The page contains "Colour"
    And The page contains "Fuel type"
    And The page contains "Petrol"
    And The page contains "Date registered"
    And The page contains "{date}"
    And The page contains "MOT expired on"
    And The page contains "{mot_expiry}"
    And The page contains "MOT history"
    And The page contains "Check mileage recorded at test, MOT expiry date, defects and advisories"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "View test location"

    And The page contains "The MOT test changed on 20 May 2018"
    And The page contains "Defects are now categorised according to their severity – dangerous, major, and minor. Find out more"

    And I click the last "Find out more" link
    And I go to the next tab
    Then The page title contains "MOT rule changes: 20 May 2018 - GOV.UK"
    And I go to the next tab
    And I close extra tabs

  Scenario: A MOTH user searches for a vehicle with an expired Post EU roadworthiness MOT
    Given I browse to /
    And I load "VEHICLE_REG_MOT_EXPIRED" as {registration}, {model}, {date}, {mot_expiry}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page title contains "Check MOT history"
    And The page contains "This vehicle's MOT has expired"
    And The page contains "You can be fined up to £1000 for driving without a valid MOT"
    And The page contains "This vehicle may be MOT exempt, for more information refer to MOT exemption guidance"
    And The page contains "Colour"
    And The page contains "Fuel type"
    And The page contains "Petrol"
    And The page contains "Date registered"
    And The page contains "{date}"
    And The page contains "MOT expired on"
    And The page contains "{mot_expiry}"
    And The page contains "MOT history"
    And The page contains "Check mileage recorded at test, MOT expiry date, defects and advisories"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "View test location"

    And The page contains "The MOT test changed on 20 May 2018"
    And The page contains "Defects are now categorised according to their severity – dangerous, major, and minor. Find out more"

    And I click the last "Find out more" link
    And I go to the next tab
    Then The page title contains "MOT rule changes: 20 May 2018 - GOV.UK"
    And I go to the next tab
    And I close extra tabs