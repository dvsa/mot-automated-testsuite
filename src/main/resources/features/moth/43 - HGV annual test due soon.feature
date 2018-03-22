#@mothpp @mothint
@mothhgv
Feature: 43 - Check HGV with a Annual Test that is due soon

  Scenario: A MOTH user searches for a HGV with a Annual Test that is due soon
    Given I browse to /
    And I enter "EXMONM1" in the registration field
    When I press the "Continue" button

    Then The page contains "EXMONM1"
    And The page contains "CONVERSION"
    And The page contains "This vehicle's first annual test is due soon"
    And I click the accordion section with the id "mot-history-description"

    And The page contains "This vehicle hasn't had its first annual test."