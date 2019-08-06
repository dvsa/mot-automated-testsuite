@cvrpp @cvrint @cvrdemo
Feature: 10 - User searches for Other recalled equipment but does not have the selected make

  Scenario: A CVR user searches for Other recalled equipment but does not have the selected make and clicks on the "Why is my equipment make not listed?" link
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    When I click the "Why is my equipment make not listed?" link
    Then I click the "Check for other vehicle or equipment recalls" link