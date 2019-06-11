@cvrpp @cvrint
<<<<<<< HEAD
Feature: 03 - User searches for Vehicle but does not have the selected make
=======
Feature: 04 - User searches for Vehicle but does not have the selected make
>>>>>>> c4a64622ba7293b3c239329936ecf1782b785cb7

  Scenario: A CVR user On the page What make is the equipment? The user clicks on the "Why is my equipment make not listed?" link.
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    When I click the "Why is my vehicle make not listed?" link
    Then I click the "Check for other vehicle or equipment recalls" link