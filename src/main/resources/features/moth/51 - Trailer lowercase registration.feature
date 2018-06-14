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
