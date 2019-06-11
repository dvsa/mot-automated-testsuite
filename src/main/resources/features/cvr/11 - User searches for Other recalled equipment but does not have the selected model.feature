@cvrpp @cvrint
<<<<<<< HEAD
Feature: 10 - User searches for Other recalled equipment but does not have the selected model
=======
Feature: 11 - User searches for Other recalled equipment but does not have the selected model
>>>>>>> c4a64622ba7293b3c239329936ecf1782b785cb7

  Scenario: A CVR user searches for Other recalled equipment but does not have the selected model and clicks on the "Why is my equipment model not listed?" link
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    And I select "COOPER" in the field with id "make"
    And I click the "Continue" button
    When I click the "Why is my equipment model not listed?" link
    Then I click the "Check for other vehicle or equipment recalls" link