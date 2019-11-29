@cvr
Feature: 02 - User searches for Vehicles with missing options

  Scenario: A CVR user clicks on the "Why is my vehicle make not listed?" link
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    When I click the "Why is my vehicle make not listed?" link
    Then The page title contains "Check vehicle recalls"
    And The page contains "Why is my make or model not listed?"
    And The page contains "The make or model you are looking for has not been recalled."
    And The page contains "If you think the vehicle or equipment you are looking for may have been recalled, contact the manufacturer or its dealership."
    Then I click the "Check for other vehicle or equipment recalls" link
    And The page title contains "Check vehicle recalls"
    And The page contains "What type of recall information are you looking for?"

  Scenario: A CVR user clicks on the "Why is my vehicle model not listed?" link
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    And I select "ALFA ROMEO" in the field with id "make"
    And I click the "Continue" button
    When I click the "Why is my vehicle model not listed?" link
    Then The page title contains "Check vehicle recalls"
    And The page contains "Why is my make or model not listed?"
    And The page contains "The make or model you are looking for has not been recalled."
    And The page contains "If you think the vehicle or equipment you are looking for may have been recalled, contact the manufacturer or its dealership."
    Then I click the "Check for other vehicle or equipment recalls" link
    And The page title contains "Check vehicle recalls"
    And The page contains "What type of recall information are you looking for?"

  Scenario: A CVR user searches for a Vehicle recall but does not have the correct year
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    And I select "AUDI" in the field with id "make"
    And I click the "Continue" button
    And I select "A3" in the field with id "model"
    And I click the "Continue" button
    And I enter "2004" in the field with id "year"
    When I click the "Continue" button
    Then The page title contains "AUDI A3 2004 - Check vehicle recalls"
    And The page contains "AUDI A3 2004"
    And The page contains "This vehicle has no recalls."
    And The page contains "no recalls."
    And The page contains "If you think this vehicle may have been recalled, contact the manufacturer's dealership."
    Then I click the "Check for other vehicle or equipment recalls" link
    And The page title contains "Check vehicle recalls"
    And The page contains "What type of recall information are you looking for?"

  Scenario: A CVR user searches for a Vehicle recall but tries to continue without selecting a make, model and incorrect year
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    When I click the "Continue" button
    And The page contains "There was a problem"
    And The page contains "Select the vehicle make"
    Then I click the "Select the vehicle make" link
    And I select "ALFA ROMEO" in the field with id "make"
    And I click the "Continue" button
    And I click the "Continue" button
    And The page contains "There was a problem"
    And The page contains "Select the vehicle model"
    Then I click the "Select the vehicle model" link
    And I select "147" in the field with id "model"
    And I click the "Continue" button
    And I click the "Continue" button
    And The page contains "There was a problem"
    And The page contains "Enter the year the vehicle was made"
    And I click the "Enter the year the vehicle was made" link
    Then I enter "2001" in the field with id "year"
    And I click the "Continue" button
    Then The page title contains "ALFA ROMEO 147 2001"