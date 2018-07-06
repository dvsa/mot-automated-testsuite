@mothtrailers @mothint
Feature: 50 - Check HGV/PSV for Trailer with a current Annual Test that expires tomorrow

  Scenario: A MOTH user searches for a Trailer with a current Annual Test that expires tomorrow
    Given I browse to /
    And I enter "C121212" in the registration field
    When I press the "Continue" button

    Then The page contains "C121212"
    And The page contains "This vehicle's annual test expires soon"
    And The page contains "The mileage recorded at test is currently not available for heavy goods and public service vehicles."
    And The page contains "It can take up to 10 working days for the latest annual test results to appear."
    And The page contains "If you think the annual test expiry date or any of the vehicle details are wrong, contact DVSA."

