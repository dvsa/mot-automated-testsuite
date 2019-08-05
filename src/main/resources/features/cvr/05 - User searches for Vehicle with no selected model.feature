@cvrpp @cvrint

Feature: 05 - User searches for Vehicle but does not have the selected model


  Scenario: A CVR user On the page What model is the vehicle? The user clicks on the "Why is my vehicle make not listed?" link.
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    And I select "ALFA ROMEO" in the field with id "make"
    And I click the "Continue" button
    When I click the "Why is my vehicle model not listed?" link
    Then I click the "Check for other vehicle or equipment recalls" link