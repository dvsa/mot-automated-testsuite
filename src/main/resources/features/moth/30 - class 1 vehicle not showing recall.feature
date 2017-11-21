@mothrecall
Feature: 30 - Class 1 Vehicle Not Showing Recall

  Scenario: A MOTH user searches for a class 1 vehicle which will not display any Recalls
    Given I browse to /
    And I load "VEHICLE_RECALL_CLASS_1" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"

    # The following section might not be required, need to confirm exact flow with dev/BA
    And The page contains "Check if {make} {model} has outstanding safety recall"
    And I click the button with class name "js-accordion__expand-button"

    And The page contains "We don't currently hold vehicle recall records for this vehicle. Please"
    And The page contains "contact your"
    And The page contains "dealership for more details."

#    And The page contains "No information found"
#    And The page contains "We don't hold information about manufacturer's safety recalls for {make} {model} {registration}."
#    And The page contains "Contact a {make} dealership to arrange for repairs."
