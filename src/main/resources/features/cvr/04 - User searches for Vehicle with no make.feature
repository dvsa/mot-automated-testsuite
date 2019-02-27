@cvrpp @cvrint
Feature: 03 - User searches for Vehicle but does not have the selected make

  Scenario: A CVR user On the page What make is the equipment? The user clicks on the "Why is my equipment make not listed?" link.
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    When I click the "Why is my vehicle make not listed?" link
    Then I click the "Check for other vehicle or equipment recalls" link