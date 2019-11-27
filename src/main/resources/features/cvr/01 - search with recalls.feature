@cvr
Feature: 01 - User searches for a Vehicle/Equipment with recall(s)

  Scenario: A CVR user searches for a vehicle with an outstanding recall
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    And I select "ALFA ROMEO" in the field with id "make"
    And I click the "Continue" button
    And I select "147" in the field with id "model"
    And I click the "Continue" button
    And I enter "2004" in the field with id "year"
    And I click the "Continue" button
    When I click the "POSSIBLE FUEL LEAK" button
    Then I click the "Search again" link

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
    And I select "COOPER" in the field with id "make"
    And I click the "Continue" button
    And I select "LT285/75R16" in the field with id "model"
    And I click the "Continue" button
    When I click the "TYRE TREAD MAY SEPARATE" button
    Then I click the "Search again" link

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