@mothrecall @mothpp @mothint
Feature: 28 - Vehicle without an Outstanding Recall

  Scenario: A MOTH user searches for a vehicle without an Outstanding Recall
    Given I browse to /
    And I load "VEHICLE_RECALL_NOT_OUTSTANDING" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Check mileage recorded at test, MOT expiry date, defects and advisories"
    And The page contains "Check if {make} {model} {registration} has outstanding recalls"
    And I click the accordion section with the id "vehicle-recalls-description"
    And I wait for "6" seconds
    And The page contains "No outstanding safety recalls found"
    And The page contains "There are no outstanding manufacturer's safety recalls for {make} {model} {registration}"
    And The page contains "There can be a delay before we receive information about safety recalls. If you are concerned, contact your vehicle manufacturer’s dealership."
    And The page contains "Was this vehicle recall information useful to you? Tell us your views so we can improve the service."
