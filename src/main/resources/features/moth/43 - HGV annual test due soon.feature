@mothhgv @mothint
Feature: 43 - Check HGV with a Annual Test that is due soon

  Scenario: A MOTH user searches for a HGV with a Annual Test that is due soon
    Given I browse to /
    And I enter "NOHOLD1" in the registration field
    When I press the "Continue" button

    Then The page contains "NOHOLD1"
    And The page contains "IVECO FORD CARGO ML75E17"
    And The page contains "This vehicle hasn't had its first annual test"
    And The page contains "If the vehicle has been tested recently, it can take up to 10 working days for us to update our records"
    And I click the accordion section with the id "mot-history-description"

    And The page contains "This vehicle hasn't had its first annual test."