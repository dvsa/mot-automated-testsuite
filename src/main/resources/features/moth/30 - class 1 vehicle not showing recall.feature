@mothrecall @mothpp @mothint
Feature: 30 - Class 1 Vehicle Not Showing Recall

  Scenario: A MOTH user searches for a class 1 vehicle which will not display any Recalls
    Given I browse to /
    And I load "VEHICLE_RECALL_CLASS_1" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Check if {make} {model} {registration} has outstanding safety recalls"
    And I click the accordion section with the id "vehicle-recalls-description"
    And The page contains "We don't hold information about manufacturer's safety recalls for"
    And The page contains "{make} {model} {registration}"
    And The page contains "Contact a"
    And The page contains "dealership to find out about outstanding safety recalls."
