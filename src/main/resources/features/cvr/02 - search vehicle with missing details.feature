@cvr
Feature: 02 - User searches for Vehicles with missing options

  Scenario: A CVR user clicks on the "Why is my equipment make not listed?" link
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    When I click the "Why is my vehicle make not listed?" link
    Then I click the "Check for other vehicle or equipment recalls" link

  Scenario: A CVR user clicks on the "Why is my vehicle make not listed?" link
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    And I select "ALFA ROMEO" in the field with id "make"
    And I click the "Continue" button
    When I click the "Why is my vehicle model not listed?" link
    Then I click the "Check for other vehicle or equipment recalls" link

  Scenario: A CVR user searches for a Vehicle recall but does not have the correct year
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

  Scenario: A CVR user searches for a Vehicle recall but tries to continue without selecting a make
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    When I click the "Continue" button
    Then I click the "Select the vehicle make" link
    # need to add checks that the error is shown.......

  Scenario: A CVR user searches for a Vehicle recall but tries to continue without selecting a model
    Given I browse to /
    And I click the "Vehicle safety recalls" radio button
    And I click the "Continue" button
    And I select "ALFA ROMEO" in the field with id "make"
    And I click the "Continue" button
    And I click the "Continue" button
    Then I click the "Select the vehicle model" link
    # need to add checks that the error is shown.......

  Scenario: A CVR user searches for Vehicle with make and model with no year and tries to continue
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

