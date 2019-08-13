@cvrpp @cvrint @cvrdemo
Feature: 02 - User searches for a Vehicle with multiple recalls

  Scenario: A CVR user searches for a vehicle with multiple outstanding recalls
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    And I select "FORD" in the field with id "make"
    And I click the "Continue" button
    And I select "FOCUS" in the field with id "model"
    And I click the "Continue" button
    And I enter "2005" in the field with id "year"
    And I click the "Continue" button
    When I click the "IN LOW AMBIENT TEMPERATURE HARD BRAKE PEDAL MAY OCCUR" button
    When I click the "ELECTRONIC COOLING FAN CONTROL MODULE MAY OVERHEAT" button
    Then I click the "Search again" link