@mothpp @mothint
Feature: 09 - Check registration with invalid symbols

  Scenario Outline: A MOTH user searches for a reg with invalid symbols
    Given I browse to /
    And I enter "<reg_number>" in the registration field

    When I press the "Continue" button

    Then The page contains "There was a problem"
    And The page contains "The registration can only contain letters and numbers"
    And The page title contains "What is the vehicle's registration number"

    Examples:
      | reg_number |
      | AB!?ABC    |
      | A<>,.      |