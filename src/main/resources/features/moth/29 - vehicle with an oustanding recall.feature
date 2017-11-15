@mothrecall
Feature: 29 - Vehicle with an Outstanding Recall

  Scenario: A MOTH user searches for a vehicle with an Outstanding Recall
    Given I browse to /
    And I load "VEHICLE_RECALL_OUTSTANDING" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    #    The below subsection might just be classed as a button, will need to wait until the final UI gets developed
    And I press the "MOT history" subsection drop down
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Check if {make} {model} {registration} has outstanding safety recall"
    #    The below subsection might just be classed as a button, will need to wait until the final UI gets developed
    And I press the "Outstanding vehicle recalls" subsection drop down
    And The page contains "Outstanding recall found"
    And The page contains "there's an outstanding manufacturer's safety recall on {make} {model} {registration}."
    And The page contains "Contact a {make} dealership to arrange for repairs."
    And The page contains "If you've had the recalled component repaired recently, it can take up to three weeks for the manufacturer to update their records."

