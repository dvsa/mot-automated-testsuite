@mothhgv @mothint
Feature: 45 - Check HGV that has no Annual Test history and the vehicle is more than a year old

  Scenario: A MOTH user searches for a HGV that has no Annual Test history and the vehicle is more than a year old
    Given I browse to /
    And I enter "NOHOLD2" in the registration field
    When I press the "Continue" button

    Then The page contains "NOHOLD2"
    And The page contains "CONVERSION"
    And The page contains "This vehicle's annual test has expired"
    And The page contains "If the vehicle has been tested recently, it can take up to 10 working days for us to update our records"
    And The page contains "This vehicle may be MOT exempt, for more information refer to MOT exemption guidance"

    And I click the "MOT exemption guidance" link
    And I go to the next tab
    And I go to the next tab
    And I close extra tabs

    And The page contains "Annual test expired on"
    And I click the accordion section with the id "mot-history-description"

    And The page contains "No annual test results found"