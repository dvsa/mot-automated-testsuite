@mothtrailers @mothint
Feature: 46 - Check HGV/PSV for Trailer that has no Annual Test history and the vehicle is less than a year old

  Scenario: A MOTH user searches for a Trailer that has no Annual Test history and the vehicle is less than a year old
    Given I browse to /
    And I enter "A112230" in the registration field
    When I press the "Continue" button

    Then The page contains "A112230"
    And The page contains "First annual test due"
    And I click the accordion section with the id "mot-history-description"

    And The page contains "This vehicle hasn't had its first annual test."

