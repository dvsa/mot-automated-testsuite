@mothtrailers @mothint
Feature: 51 - Check HGV/PSV for Trailer with lowercase registration

  Scenario: A MOTH user searches for a Trailer with lowercase registration
    Given I browse to /
    And I enter "a112233" in the registration field
    When I press the "Continue" button

    And I click the "MOT exemption guidance" link
    And I go to the next tab
    And I go to the next tab
    And I close extra tabs

    And The page contains "A112233"
    And The page contains "The mileage recorded at test is currently not available for heavy goods and public service vehicles."
    And The page contains "It can take up to 10 working days for the latest annual test results to appear."
    And The page contains "If you think the annual test expiry date or any of the vehicle details are wrong, contact DVSA."
