@mothtrailers @mothint
Feature: 47 - Check HGV/PSV for Trailer that has no Annual Test history and the vehicle is more than a year old

  Scenario: A MOTH user searches for a Trailer that has no Annual Test history and the vehicle is more than a year old
    Given I browse to /
    And I enter "A234567" in the registration field
    When I press the "Continue" button

    Then The page contains "A234567"
    And The page contains "Trailers must be tested one year after they were first sold or supplied."
    And The page contains "The mileage recorded at test is currently not available for heavy goods and public service vehicles."
    And The page contains "It can take up to 10 working days for the latest annual test results to appear."
    And The page contains "If you think the annual test expiry date or any of the vehicle details are wrong, contact DVSA."

    And The page contains "First annual test due"
    And I click the accordion section with the id "mot-history-description"

    And The page contains "No annual test results found"
