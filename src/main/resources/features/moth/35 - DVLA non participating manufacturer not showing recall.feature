@mothrecall @mothpp @mothint
Feature: 35 - DVLA Non Participating Manufacturer Not Showing Recall

  Scenario: A MOTH user searches for a Non Participating Manufacturer DVLA vehicle which will not display any Recalls
    Given I browse to /
    And I load "VEHICLE_RECALL_DVLA_NOT_PARTICIPATING" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Check if {make} {model} has outstanding safety recall"
    And I click the button with class name "js-accordion__expand-button"
    And The page contains "We don't hold information about manufacturer's safety recalls for"
    And The page contains "{make} {model} {registration}"
    And The page contains "Contact a"
    And The page contains "dealership to find out about outstanding safety recalls."
