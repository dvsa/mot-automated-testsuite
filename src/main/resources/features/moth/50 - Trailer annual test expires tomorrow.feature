@mothtrailers @mothint
Feature: 50 - Check HGV/PSV for Trailer with a current Annual Test that expires tomorrow

  Scenario: A MOTH user searches for a Trailer with a current Annual Test that expires tomorrow
    Given I browse to /
    And I enter "C121212" in the registration field
    When I press the "Continue" button

    Then The page contains "C121212"
    And The page contains "This vehicle's annual test expires soon"

