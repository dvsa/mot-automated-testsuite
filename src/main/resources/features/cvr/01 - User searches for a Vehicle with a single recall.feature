@cvrpp @cvrint
Feature: 01 - User searches for a Vehicle with a single recall

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