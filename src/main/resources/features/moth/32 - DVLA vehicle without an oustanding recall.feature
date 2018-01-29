@mothrecall @mothpp @mothint
Feature: 32 - DVLA Vehicle without an Outstanding Recall

  Scenario: A MOTH user searches for a DVLA vehicle without an Outstanding Recall
    Given I browse to /
    And I load "VEHICLE_RECALL_DVLA_NOT_OUTSTANDING" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Mileage recorded at test, parts failed or had minor problems"
    And The page contains "Check if {make} {model} has outstanding safety recall"
    And I click the accordion section with the id "vehicle-recalls-description"
    And I wait for "6" seconds
    And The page contains "This vehicle hasn't had its first MOT"
    And The page contains "No outstanding recalls found"
    And The page contains "There are no outstanding manufacturer's safety recalls for"
    And The page contains "{make} {model} {registration}"

