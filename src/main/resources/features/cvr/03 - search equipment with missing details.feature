@cvr
Feature: 03 - User searches for Other recalled equipment with missing options

  Scenario: A CVR user searches for Other recalled equipment but does not have the selected make
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    When I click the "Why is my equipment make not listed?" link
    Then I click the "Check for other vehicle or equipment recalls" link

  Scenario: A CVR user searches for Other recalled equipment but does not have the selected model and clicks on the "Why is my equipment model not listed?" link
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    And I select "COOPER" in the field with id "make"
    And I click the "Continue" button
    When I click the "Why is my equipment model not listed?" link
    Then I click the "Check for other vehicle or equipment recalls" link

  Scenario: User searches for Other recalled equipment but does not have the selected make and tries to continue and will see the "There was a problem banner".
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    When I click the "Continue" button
    Then I click the "Select the equipment make" link

  Scenario: User searches for Other recalled equipment they have the make but does not have the selected model
    Given I browse to /
    And I click the "Other recalled equipment" radio button
    And I click the "Continue" button
    And I select "BELRON" in the field with id "make"
    And I click the "Continue" button
    When I click the "Continue" button
    Then I click the "Select the equipment model" link