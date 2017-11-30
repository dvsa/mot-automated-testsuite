@mothrecall @mothpp @mothint
Feature: 36 - Recall feature timing out

  Scenario: A MOTH user searches for a vehicle and the Recall Request times out
    Given I browse to /
    And I load "VEHICLE_RECALL_TIME_OUT" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Mileage recorded at test, parts failed or had minor problems"
    And The page contains "Check if {make} {model} has outstanding safety recall"
    And I click the button with class name "js-accordion__expand-button"
    And I wait for "17" seconds
    And The page contains "Sorry, something went wrong with the search."
    And The page contains "Please try again later."

