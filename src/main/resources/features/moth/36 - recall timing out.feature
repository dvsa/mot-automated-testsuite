@mothrecall @mothpp @mothint
Feature: 36 - Recall feature timing out

  Scenario: A MOTH user searches for a vehicle and the Recall Request times out
    Given I browse to /
    And I load "VEHICLE_RECALL_TIME_OUT" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Check mileage recorded at test, MOT expiry date, defects and advisories"
    And The page contains "Check if {make} {model} {registration} has outstanding safety recalls"
    And I click the accordion section with the id "vehicle-recalls-description"
    And I wait for "17" seconds
    And The page contains "Sorry, something went wrong with the search."
    And The page contains "Please try again later."

