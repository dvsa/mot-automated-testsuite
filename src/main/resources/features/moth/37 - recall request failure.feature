@mothrecall @mothpp @mothint
Feature: 37 - Recall Request Fails

  Scenario: A MOTH user searches for a Vehicle which will display the Persistent failure Message
    Given I browse to /
    And I load "VEHICLE_RECALL_FAILURE" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Check if {make} {model} {registration} has outstanding recalls"
    And I click the accordion section with the id "vehicle-recalls-description"
    And I wait for "6" seconds
    And The page contains "We don't hold information about manufacturer's safety recalls for"
    And The page contains "{make} {model} {registration}"
    And The page contains "Contact a"
    And The page contains "dealership to find out about outstanding safety recalls."
