@cvrpp @cvrint
<<<<<<< HEAD
Feature: 05 - User searches for Vehicle but does not have the correct year
=======
Feature: 06 - User searches for Vehicle but does not have the correct year
>>>>>>> c4a64622ba7293b3c239329936ecf1782b785cb7

Scenario: User searches for Vehicle recall but does not have the correct year
  Given I browse to /
  And I click the "Vehicle safety recalls" radio button
  And I click the "Continue" button
  And I select "AUDI" in the field with id "make"
  And I click the "Continue" button
  And I select "A3" in the field with id "model"
  And I click the "Continue" button
  And I enter "2004" in the field with id "year"
  When I click the "Continue" button
  Then I click the "Check for other vehicle or equipment recalls" link