@cvrpp @cvrint
Feature: 16 - User searches for Other recalled equipment with an outstanding recall then clicks the back button

  Scenario: A CVR user searches for Other recalled equipment with an outstanding recall then clicks the back button to return to the CVR home page
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    And I select "COOPER" in the field with id "make"
    And I click the "Continue" button
    And I select "LT285/75R16" in the field with id "model"
    And I click the "Continue" button
    And I click the "Back" link
    When I click the "Back" link
    Then I click the "Back" link