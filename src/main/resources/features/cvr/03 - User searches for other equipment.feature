@cvrpp @cvrint @cvrdemo
Feature: 03 - User searches for Other recalled equipment

  Scenario: A CVR user searches for equipment with an outstanding recall
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    And I select "COOPER" in the field with id "make"
    And I click the "Continue" button
    And I select "LT285/75R16" in the field with id "model"
    And I click the "Continue" button
    When I click the "TYRE TREAD MAY SEPARATE" button
    Then I click the "Search again" link
    Then I click the "Safety recall" button
