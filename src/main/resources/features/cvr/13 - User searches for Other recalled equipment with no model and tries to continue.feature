@cvrpp @cvrint
<<<<<<< HEAD
Feature: 12 - User searches for Other recalled equipment with no model and tries to continue
=======
Feature: 13 - User searches for Other recalled equipment with no model and tries to continue
>>>>>>> c4a64622ba7293b3c239329936ecf1782b785cb7

  Scenario: User searches for Other recalled equipment they have the make but does not have the selected model and tries to continue and will see the "There was a problem banner".
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    And I select "BELRON" in the field with id "make"
    And I click the "Continue" button
    When I click the "Continue" button
    Then I click the "Select the equipment model" link