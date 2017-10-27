@moth2 @moth-int
Feature: 11 - Check registration longer than 14 characters

  Scenario: A MOTH user searches for a reg longer than 14 characters
    Given I browse to /
    And I enter "ABCDEFGHIJ12345" in the registration field

    When I press the "Continue" button

    Then The page contains "There was a problem"
    And The page contains "The input is more than 14 characters long"
    And The page title contains "What is the vehicle's registration number"
