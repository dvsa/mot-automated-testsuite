@cvrpp @cvrint
Feature: 12 - User searches for Other recalled equipment with no make and tries to continue

  Scenario: User searches for Other recalled equipment but does not have the selected make and tries to continue and will see the "There was a problem banner".
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    When I click the "Continue" button
    Then I click the "Select the equipment make" link