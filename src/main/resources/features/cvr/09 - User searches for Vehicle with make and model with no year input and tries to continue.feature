@cvrpp @cvrint
Feature: 08 - User searches for Vehicle with make and model with no year input and tries to continue.feature

  Scenario: User searches for Vehicle with make and model with no year input and tries to continue but will see the There was a problem banner
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    And I select "ALFA ROMEO" in the field with id "make"
    And I click the "Continue" button
    And I select "147" in the field with id "model"
    And I click the "Continue" button
    And I click the "Continue" button
    And I click the "Enter the year the vehicle was made" link
    Then I enter "2001" in the field with id "year"





    # Then  // Check the 'There was a problem' banner text