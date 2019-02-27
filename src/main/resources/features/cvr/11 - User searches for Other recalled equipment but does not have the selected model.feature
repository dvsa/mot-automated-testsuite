@cvrpp @cvrint
Feature: 10 - User searches for Other recalled equipment but does not have the selected model

  Scenario: A CVR user searches for Other recalled equipment but does not have the selected model and clicks on the "Why is my equipment model not listed?" link
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    And I select "COOPER" in the field with id "make"
    And I click the "Continue" button
    When I click the "Why is my equipment model not listed?" link
    Then I click the "Check for other vehicle or equipment recalls" link