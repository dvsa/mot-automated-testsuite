@cvrpp @cvrint
<<<<<<< HEAD
Feature: 07 - User searches for Vehicle with make but no model and tries continue
=======
Feature: 08 - User searches for Vehicle with make but no model and tries continue
>>>>>>> c4a64622ba7293b3c239329936ecf1782b785cb7

  Scenario: User searches for Vehicle but does not have the selected model and tries to continue but will see the There was a problem banner
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    And I select "ALFA ROMEO" in the field with id "make"
    And I click the "Continue" button
    And I click the "Continue" button
<<<<<<< HEAD
    When // Check the 'There was a problem' banner text
=======
#    When I Check the 'There was a problem' banner text
>>>>>>> c4a64622ba7293b3c239329936ecf1782b785cb7
    Then I click the "Select the vehicle model" link