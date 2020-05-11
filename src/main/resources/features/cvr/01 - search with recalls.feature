@cvr
Feature: 01 - User searches for a Vehicle/Equipment with recall(s)

  Scenario: A CVR user searches for a vehicle with an outstanding recall
    Given I browse to /
    And The page title contains "Check vehicle recalls"
    And The page contains "What type of recall information are you looking for?"
    And The page contains "Download vehicle and equipment recall information"
    And The page contains "For example, tyres or child seats"
    And The page contains "Cookies"
    And I click the "Cookies" link

    Then The page title contains "Cookie policy - Check vehicle recalls"
    And I go back to the previous page

    Then The page title contains "Check vehicle recalls"
    And The page contains "Terms and conditions"
    And I click the "Terms and conditions" link

    Then The page title contains "Terms and conditions - Check vehicle recalls"
    And I go back to the previous page

    Then The page title contains "Check vehicle recalls"
    And The page contains "Privacy notice"
    And I click the "Privacy notice" link
    And I go to the next tab

    Then The page title contains "Personal information charter - Driver and Vehicle Standards Agency - GOV.UK"
    And I go to the next tab
    And I close extra tabs

    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button

    Then The page title contains "What make is the vehicle?"
    And The page contains "What make is the vehicle?"
    And The page contains "This service only includes vehicles that have been recalled."
    And The page contains "Why is my vehicle make not listed?"
    And The page contains "Download vehicle and equipment recall information"
    And I select "ALFA ROMEO" in the field with id "make"
    And I click the "Continue" button

    Then The page title contains "What model is the vehicle?"
    And The page contains "What model is the vehicle?"
    And The page contains "This service only includes vehicles that have been recalled."
    And The page contains "Why is my vehicle model not listed?"
    And I select "147" in the field with id "model"
    And I click the "Continue" button

    Then The page title contains "What year was the vehicle made?"
    And The page contains "What year was the vehicle made?"
    And The page contains "For example, 2004"
    And I enter "2004" in the field with id "year"
    And I click the "Continue" button

    Then The page title contains "ALFA ROMEO 147 2004"
    And The page contains "ALFA ROMEO 147 2004"
    And The page contains "This vehicle has "
    And The page contains "1 recall."
    And The page contains "Recall number"
    And The page contains "R/2005/030"
    And The page contains "Recall date"
    And The page contains "23-05-2005"
    And The page contains "Recall type"
    And The page contains "Safety recall"
    When I click the "POSSIBLE FUEL LEAK" button
    Then The page contains "Reason for recall"
    And The page contains "A fuel leak may result due to cracking of the recirculation pipe flange on the fuel tank when the tank is filled to the maximum."
    And The page contains "How to check if the vehicle is recalled"
    And The page contains "Contact the local ALFA ROMEO dealership or manufacturer. You will not need to pay for anything involving the recall."
    And The page contains "How the manufacturer will repair"
    And The page contains "Recalled vehicles will have the fuel tank inspected for cracks and replaced if necessary."
    And The page contains "Number of affected vehicles"
    And The page contains "865"
    Then I click the "Search again" link
    And The page title contains "Check vehicle recalls"
    And The page contains "What type of recall information are you looking for?"

  Scenario: A CVR user searches for a vehicle with multiple outstanding recalls
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    And I select "FORD" in the field with id "make"
    And I click the "Continue" button
    And I select "FOCUS" in the field with id "model"
    And I click the "Continue" button
    And I enter "2005" in the field with id "year"
    And I click the "Continue" button
    When I click the "IN LOW AMBIENT TEMPERATURE HARD BRAKE PEDAL MAY OCCUR" button
    When I click the "ELECTRONIC COOLING FAN CONTROL MODULE MAY OVERHEAT" button
    Then I click the "Search again" link

  Scenario: A CVR user searches for a Vehicle with an outstanding recall then clicks the back button
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    And I select "ALFA ROMEO" in the field with id "make"
    And I click the "Continue" button
    And I select "147" in the field with id "model"
    And I click the "Continue" button
    And I enter "2004" in the field with id "year"
    And I click the "Continue" button
    And The page title contains "ALFA ROMEO 147 2004"
    And I click the "Back" link
    And The page contains "What year was the vehicle made?"
    And I click the "Back" link
    And The page title contains "What model is the vehicle?"
    And I click the "Back" link
    And The page title contains "What make is the vehicle?"
    When I click the "Back" link
    Then The page title contains "Check vehicle recalls"

  Scenario: A CVR user searches for Other recalled equipment with an outstanding recall
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button

    Then The page title contains "What make is the equipment?"
    And The page contains "What make is the equipment?"
    And The page contains "This service only includes equipment that has been recalled."
    And The page contains "Why is my equipment make not listed?"
    And The page contains "Download vehicle and equipment recall information"
    And I select "COOPER" in the field with id "make"
    And I click the "Continue" button

    Then The page title contains "What model is the equipment?"
    And The page contains "What model is the equipment?"
    And The page contains "This service only includes equipment that has been recalled."
    And The page contains "Why is my equipment model not listed?"
    And I select "LT285/75R16" in the field with id "model"
    And I click the "Continue" button

    Then The page title contains "COOPER LT285/75R16"
    And The page contains "COOPER LT285/75R16"
    And The page contains "This equipment has "
    And The page contains "1 recall."
    And The page contains "Recall number"
    And The page contains "RTW/2006/001"
    And The page contains "Recall date"
    And The page contains "04-03-2006"
    And The page contains "Recall type"
    And The page contains "Safety recall"
    When I click the "TYRE TREAD MAY SEPARATE" button
    Then The page contains "Reason for recall"
    And The page contains "It has been identified that a low tread gauge in the shoulder slot area could lead to cracking at the base of the shoulder slots. This could result in exposed belt wire and accelerated belt degradation which could result in separation."
    And The page contains "How to check if the equipment is recalled"
    And The page contains "Contact the local COOPER dealership or manufacturer. You will not need to pay for anything involving the recall."
    And The page contains "How the manufacturer will repair"
    And The page contains "Customers are to be contacted and the subject tyres changed free of charge."
    And The page contains "Number of affected equipment"
    And The page contains "81"
    Then I click the "Search again" link

    And The page title contains "Check vehicle recalls"
    And The page contains "What type of recall information are you looking for?"

  Scenario: A CVR user searches for Other recalled equipment with an outstanding recall then clicks the back button
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    And I select "COOPER" in the field with id "make"
    And I click the "Continue" button
    And I select "LT285/75R16" in the field with id "model"
    And I click the "Continue" button
    And The page title contains "COOPER LT285/75R16"
    And I click the "Back" link
    And The page title contains "What model is the equipment?"
    And I click the "Back" link
    And The page title contains "What make is the equipment?"
    When I click the "Back" link
    Then The page title contains "Check vehicle recalls"