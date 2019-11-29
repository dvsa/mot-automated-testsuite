@cvr
Feature: 03 - User searches for Other recalled equipment with missing options

  Scenario: A CVR user clicks on the "Why is my equipment make not listed?" link
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    When I click the "Why is my equipment make not listed?" link
    Then The page title contains "Check vehicle recalls"
    And The page contains "Why is my make or model not listed?"
    And The page contains "The make or model you are looking for has not been recalled."
    And The page contains "If you think the vehicle or equipment you are looking for may have been recalled, contact the manufacturer or its dealership."
    Then I click the "Check for other vehicle or equipment recalls" link
    And The page title contains "Check vehicle recalls"
    And The page contains "What type of recall information are you looking for?"

  Scenario: A CVR user clicks on the "Why is my equipment model not listed?" link
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    And I select "COOPER" in the field with id "make"
    And I click the "Continue" button
    When I click the "Why is my equipment model not listed?" link
    Then The page title contains "Check vehicle recalls"
    And The page contains "Why is my make or model not listed?"
    And The page contains "The make or model you are looking for has not been recalled."
    And The page contains "If you think the vehicle or equipment you are looking for may have been recalled, contact the manufacturer or its dealership."
    Then I click the "Check for other vehicle or equipment recalls" link
    And The page title contains "Check vehicle recalls"
    And The page contains "What type of recall information are you looking for?"

  Scenario: A CVR user searches for Other recalled equipment but tries to continue without selecting a make and model
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    When I click the "Continue" button
    And The page contains "There was a problem"
    And The page contains "Select the equipment make"
    Then I click the "Select the equipment make" link
    And I select "BELRON" in the field with id "make"
    And I click the "Continue" button
    When I click the "Continue" button
    And The page contains "There was a problem"
    And The page contains "Select the equipment model"
    Then I click the "Select the equipment model" link
    And I select "SPLINTEX" in the field with id "model"
    And I click the "Continue" button
    Then The page title contains "BELRON SPLINTEX"