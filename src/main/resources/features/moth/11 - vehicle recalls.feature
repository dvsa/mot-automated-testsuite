@mothrecall @mothpp @mothint
Feature: 11 - Vehicle without an Outstanding Recall

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
    And The page contains "This information is provided by the vehicle manufacturer. If you think the information is wrong, contact the vehicle manufacturer's dealership. Please do not contact the DVSA, as we are not able to change the recall status."
    And The page contains "Was this vehicle recall information useful to you? Tell us your views so we can improve the service."

  Scenario: A MOTH user searches for a vehicle with an Outstanding Recall
    Given I browse to /
    And I load "VEHICLE_RECALL_OUTSTANDING" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Check if {make} {model} {registration} has outstanding recalls"
    And I click the accordion section with the id "vehicle-recalls-description"
    And I wait for "6" seconds
    And The page contains "Outstanding recall found"
    And The page contains "This vehicle has been recalled since at least"
    And The page contains "Contact a {make} dealership to arrange for repairs."
    And The page contains "If you've had the recalled component repaired recently, it can take up to 3 weeks for the manufacturer to update their records."
    And The page contains "This information is provided by the vehicle manufacturer. If you think the information is wrong, contact the vehicle manufacturer's dealership. Please do not contact the DVSA, as we are not able to change the recall status."
    And The page contains "Was this vehicle recall information useful to you? Tell us your views so we can improve the service."

  Scenario: A MOTH user searches for a class 1 vehicle which will not display any Recalls
    Given I browse to /
    And I load "VEHICLE_RECALL_CLASS_1" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Check if {make} {model} {registration} has outstanding recalls"
    And I click the accordion section with the id "vehicle-recalls-description"
    And The page contains "We don't hold information about manufacturer's safety recalls for {make} {model} {registration}."
    And The page contains "To find out if your vehicle has any outstanding safety recalls, contact a {make} dealership."
    And The page contains "Was this vehicle recall information useful to you? Tell us your views so we can improve the service."

  Scenario: A MOTH user searches for a Non Participating Manufacturer vehicle which will not display any Recalls
    Given I browse to /
    And I load "VEHICLE_RECALL_NOT_PARTICIPATING" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Check if {make} {model} {registration} has outstanding recalls"
    And I click the accordion section with the id "vehicle-recalls-description"
    And The page contains "We don't hold information about manufacturer's safety recalls for {make} {model} {registration}."
    And The page contains "To find out if your vehicle has any outstanding safety recalls, contact a {make} dealership."
    And The page contains "Was this vehicle recall information useful to you? Tell us your views so we can improve the service."

  Scenario: A MOTH user searches for a DVLA vehicle without an Outstanding Recall
    Given I browse to /
    And I load "VEHICLE_RECALL_DVLA_NOT_OUTSTANDING" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "First MOT due"
    And The page contains "Check mileage recorded at test, MOT expiry date, defects and advisories"
    And The page contains "Check if {make} {model} {registration} has outstanding recalls"
    And I click the "Open All" button
    And I wait for "6" seconds
    And The page contains "This vehicle hasn't had its first MOT."
    And The page contains "No outstanding safety recalls found"
    And The page contains "This information is provided by the vehicle manufacturer. If you think the information is wrong, contact the vehicle manufacturer's dealership. Please do not contact the DVSA, as we are not able to change the recall status."
    And The page contains "Was this vehicle recall information useful to you? Tell us your views so we can improve the service."

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
    And The page contains "This vehicle has been recalled since at least"
    And The page contains "Contact a {make} dealership to arrange for repairs."
    And The page contains "If you've had the recalled component repaired recently, it can take up to 3 weeks for the manufacturer to update their records."
    And The page contains "This information is provided by the vehicle manufacturer. If you think the information is wrong, contact the vehicle manufacturer's dealership. Please do not contact the DVSA, as we are not able to change the recall status."
    And The page contains "Was this vehicle recall information useful to you? Tell us your views so we can improve the service."

  Scenario: A MOTH user searches for a class 1 DVLA vehicle which will not display any Recalls
    Given I browse to /
    And I load "VEHICLE_RECALL_DVLA_CLASS_1" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "First MOT due"
    And The page contains "Check if {make} {model} {registration} has outstanding recalls"
    And I click the accordion section with the id "vehicle-recalls-description"
    And The page contains "We don't hold information about manufacturer's safety recalls for {make} {model} {registration}."
    And The page contains "To find out if your vehicle has any outstanding safety recalls, contact a {make} dealership."
    And The page contains "Was this vehicle recall information useful to you? Tell us your views so we can improve the service."

  Scenario: A MOTH user searches for a Non Participating Manufacturer DVLA vehicle which will not display any Recalls
    Given I browse to /
    And I load "VEHICLE_RECALL_DVLA_NOT_PARTICIPATING" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "First MOT due"
    And The page contains "Check if {make} {model} {registration} has outstanding recalls"
    And I click the accordion section with the id "vehicle-recalls-description"
    And The page contains "We don't hold information about manufacturer's safety recalls for {make} {model} {registration}."
    And The page contains "To find out if your vehicle has any outstanding safety recalls, contact a {make} dealership."
    And The page contains "Was this vehicle recall information useful to you? Tell us your views so we can improve the service."

  Scenario: A MOTH user searches for a vehicle and the Recall Request times out
    Given I browse to /
    And I load "VEHICLE_RECALL_TIME_OUT" as {registration}, {make}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button
    Then The page contains "{registration}"
    And The page contains "{make} {model}"
    And The page contains "Check mileage recorded at test, MOT expiry date, defects and advisories"
    And The page contains "Check if {make} {model} {registration} has outstanding recalls"
    And I click the accordion section with the id "vehicle-recalls-description"
    And The page contains "DVSA are checking for any recalls on this vehicle"
    And The page contains "This may take a few seconds or so, please bear with us."
    And I wait for "17" seconds

    And The page contains "Sorry, something went wrong with the search."
    And The page contains "Please try again later."
    And The page contains "Was this vehicle recall information useful to you? Tell us your views so we can improve the service."

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
    And The page contains "We don't hold information about manufacturer's safety recalls for {make} {model} {registration}."
    And The page contains "Contact a {make} dealership to find out about outstanding safety recalls."
    And The page contains "Was this vehicle recall information useful to you? Tell us your views so we can improve the service."