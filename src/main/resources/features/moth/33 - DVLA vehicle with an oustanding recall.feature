@mothrecall @mothpp @mothint
Feature: 33 - DVLA Vehicle with an Outstanding Recall

  Scenario: A MOTH user searches for a DVLA vehicle with an Outstanding Recall
    Given I browse to /
    And I load "VEHICLE_RECALL_DVLA_OUTSTANDING" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "First MOT due"
    And The page contains "Check if {make} {model} {registration} has outstanding recalls"
    And I click the accordion section with the id "vehicle-recalls-description"
    And I wait for "6" seconds

    And The page contains "Outstanding recall found"
    And The page contains "there's an outstanding manufacturer's safety recall on {make} {model} {registration}."
    And The page contains "Contact a {make} dealership to arrange for repairs."
    And The page contains "If you've had the recalled component repaired recently, it can take up to three weeks for the manufacturer to update their records."
    And The page contains "Was this vehicle recall information useful to you? Tell us your views so we can improve the service."
