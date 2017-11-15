@mothrecall
Feature: 31 - Class 1 Vehicle Not Showing Recall

  Scenario: A MOTH user searches for a class 1 vehicle which will not display any Recalls
    Given I browse to /
    And I load "VEHICLE_RECALL_CLASS_1" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    #    The below subsection might just be classed as a button, will need to wait until the final UI gets developed
    And I press the "MOT history" subsection drop down
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Check if {make} {model} {registration} has outstanding safety recall"
    #    The below subsection might just be classed as a button, will need to wait until the final UI gets developed
    And I press the "Outstanding vehicle recalls" subsection drop down
    And The page contains "No information found"
    And The page contains "We don't hold information about manufacturer's safety recalls for {make} {model} {registration}."
    And The page contains "Contact a {make} dealership to arrange for repairs."