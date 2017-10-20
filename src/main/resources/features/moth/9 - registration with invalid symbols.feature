@moth2
Feature: 09 - Check registration with invalid symbols

  Scenario: A MOTH user searches for a reg with invalid symbols 1
    Given I browse to /
    And I enter "AB!?ABC" in the registration field

    When I press the "Continue" button

    Then The page contains "There was a problem"
    And The page contains "The registration can only contain letters and numbers"
    And The page title contains "What is the vehicle's registration number"

  Scenario: A MOTH user searches for a reg with invalid symbols 2
    Given I browse to /
    And I enter "A<>,." in the registration field

    When I press the "Continue" button

    Then The page contains "There was a problem"
    And The page contains "The registration can only contain letters and numbers"
    And The page title contains "What is the vehicle's registration number"