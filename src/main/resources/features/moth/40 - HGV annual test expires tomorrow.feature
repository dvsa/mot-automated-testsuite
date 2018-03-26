#@mothpp @mothint
@mothhgv
Feature: 40 - Check HGV with a current Annual Test that expires tomorrow

  Scenario: A MOTH user searches for a HGV with a current Annual Test that expires tomorrow
    Given I browse to /
    And I enter "EXTOMOR" in the registration field
    When I press the "Continue" button

    Then The page contains "EXTOMOR"
    And The page contains "CONVERSION"
    And The page contains "This vehicle's annual test expires soon"

