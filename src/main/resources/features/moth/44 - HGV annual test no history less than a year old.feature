@mothhgv @mothpp @mothint
Feature: 44 - Check HGV that has no Annual Test history and the vehicle is less then a year old

  Scenario: A MOTH user searches for a HGV that has no Annual Test history and the vehicle is less then a year old
    Given I browse to /
    And I enter "NOHIST1" in the registration field
    When I press the "Continue" button

    Then The page contains "NOHIST1"
    And The page contains "CONVERSION"
    And The page does not contain "This vehicle's first annual test is due soon"
    And The page contains "First annual test due"
    And I click the accordion section with the id "mot-history-description"

    And The page contains "This vehicle hasn't had its first annual test."