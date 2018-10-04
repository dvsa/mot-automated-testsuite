@mothtrailers @mothint
Feature: 46 - Check HGV/PSV for Trailer that has no Annual Test history and the vehicle is less than a year old

  Scenario: A MOTH user searches for a Trailer that has no Annual Test history and the vehicle is less than a year old
    Given I browse to /
    And I enter "A112230" in the registration field
    When I press the "Continue" button

    Then The page contains "A112230"
    And The page contains "First annual test due"

    And The page contains "Trailers must be tested one year after they were first sold or supplied. "
    And The page contains "The mileage recorded at test is currently not available for heavy goods and public service vehicles."
    And The page contains "It can take up to 10 working days for the latest annual test results to appear."
    And The page contains "If you think the annual test expiry date or any of the vehicle details are wrong, contact DVSA."
    And The page contains "Check test expiry date, defects and advisories"

    And I click the accordion section with the id "mot-history-description"

    And The page contains "This vehicle hasn't had its first annual test."
    And The page contains "The annual test changed on 20 May 2018"
    And The page does not contain "Defects are now categorised according to their severity – dangerous, major, and minor."
    And The page contains "Find out more"

    And I click the last "Find out more" link
    And I go to the next tab
    Then The page title contains "Heavy goods vehicle (HGV) inspection manual - GOV.UK"
    And I go to the next tab
    And I close extra tabs

