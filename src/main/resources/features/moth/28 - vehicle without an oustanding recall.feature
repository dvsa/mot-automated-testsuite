@mothrecall
Feature: 28 - Vehicle without an Outstanding Recall

  Scenario: A MOTH user searches for a vehicle without an Outstanding Recall
    Given I browse to /
    And I load "VEHICLE_RECALL_NOT_OUTSTANDING" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
#    The below subsection might just be classed as a button, will need to wait until the final UI gets developed
    And I press the "MOT history" subsection drop down
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Check if {make} {model} {registration} has outstanding safety recall"
    #    The below subsection might just be classed as a button, will need to wait until the final UI gets developed
    And I press the "Outstanding vehicle recalls" subsection drop down
    And The page contains "No outstanding recalls found"
    And The page contains "There are no outstanding manufacturer's safety recalls for {make} {model} {registration}."

