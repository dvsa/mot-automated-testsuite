@mothpp @mothint
Feature: 01 - Check vehicle with expired MOT

  Scenario: A MOTH user searches for a vehicle with an expired MOT
    Given I browse to /
    And I load "VEHICLE_REG_MOT_EXPIRED" as {registration}, {model}, {date}, {mot_expiry}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page title contains "{model}- Check MOT history"
    And The page contains "This vehicle's MOT has expired"
    And The page contains "You can be fined up to £1000 for driving without a valid MOT"
    And The page contains "Colour"
    And The page contains "Green"
    And The page contains "Fuel type"
    And The page contains "Petrol"
    And The page contains "Date registered"
    And The page contains "{date}"
    And The page contains "MOT expired on"
    And The page contains "{mot_expiry}"
    And The page contains "MOT History"
    And The page contains "Mileage recorded at test, parts failed or had minor problems"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "unavailable until further notice"
