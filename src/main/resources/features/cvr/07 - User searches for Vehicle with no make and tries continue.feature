@cvrpp @cvrint
Feature: 06 - User searches for Vehicle with no make and tries continue

  Scenario: User searches for Vehicle but does not have the selected make tries to continue and see the There was a problem banner
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    When I click the "Continue" button
    Then I click the "Select the vehicle make" link