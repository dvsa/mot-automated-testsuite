@cvrpp @cvrint @cvrdemo
Feature: 16 - User searches for a Vehicle with an outstanding recall then clicks the back button

  Scenario: User searches for a Vehicle with an outstanding recall then clicks the back button returns to CVR home page
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

