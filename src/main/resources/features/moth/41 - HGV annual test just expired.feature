@mothhgv @mothint
Feature: 41 - Check HGV with a just expired Annual Test

  Scenario: A MOTH user searches for a HGV with a Annual Test that is just about to expire
    Given I browse to /
    And I enter "EXYESTE" in the registration field
    When I press the "Continue" button

    Then The page contains "EXYESTE"
    And The page contains "CONVERSION"
    And The page contains "This vehicle's annual test has expired"
    And The page contains "If the vehicle has been tested recently, it can take up to 10 working days for us to update our records"
    And The page contains "This vehicle may be MOT exempt, for more information refer to MOT exemption guidance"

    And I click the "MOT exemption guidance" link
    And I go to the next tab
    And I go to the next tab
    And I close extra tabs