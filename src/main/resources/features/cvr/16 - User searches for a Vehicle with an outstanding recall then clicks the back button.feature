@cvrpp @cvrint
Feature: 15 - User searches for a Vehicle with an outstanding recall then clicks the back button

  Scenario: User searches for a Vehicle with an outstanding recall then clicks the back button to return to the CVR home page
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    And I select "ALFA ROMEO" in the field with id "make"
    And I click the "Continue" button
    And I select "147" in the field with id "model"
    And I click the "Continue" button
    And I enter "2004" in the field with id "year"
    And I click the "Continue" button
    And I click the "Back" link
    And I click the "Back" link
    When I click the "Back" link
    Then I click the "Back" link