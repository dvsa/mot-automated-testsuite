@mothhgv @mothint
Feature: 44 - Check HGV that has no Annual Test history and the vehicle is less than a year old

  Scenario: A MOTH user searches for a HGV that has no Annual Test history and the vehicle is less than a year old
    Given I browse to /
    And I enter "NOHIST1" in the registration field
    When I press the "Continue" button

    Then The page contains "NOHIST1"
    And The page contains "CONVERSION"
    And The page does not contain "This vehicle's first annual test is due soon"
    And The page contains "First annual test due"
    And I click the accordion section with the id "mot-history-description"

    And The page contains "This vehicle hasn't had its first annual test."
    And The page contains "The annual test changed on 20 May 2018"
    And The page does not contain "Defects are now categorised according to their severity – dangerous, major, and minor."
    And The page contains "Find out more"

    And I click the last "Find out more" link
    And I go to the next tab
    Then The page title contains "Public service vehicle (PSV) inspection manual - GOV.UK"
    And I go to the next tab
    And I close extra tabs