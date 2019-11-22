@cvrpp @cvrint @cvrdemo
Feature: 17 - User searches for Other recalled equipment with an outstanding clicks the back button

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