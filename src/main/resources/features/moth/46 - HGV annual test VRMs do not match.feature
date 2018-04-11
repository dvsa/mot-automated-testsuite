@mothhgv @mothint
Feature: 46 - Check HGV where the VRM entered does not match the VRM returned by the HGV API

  Scenario: A MOTH user searches for a HGV where the VRM entered does not match the VRM returned by the HGV API
    Given I browse to /
    And I enter "VRMNOMA" in the registration field
    When I press the "Continue" button

    Then The page contains "VRMNOMA"
    And The page contains "DAF FA LF45.130 08"
    And I click the accordion section with the id "mot-history-description"

    And The page contains "There is something wrong with the vehicle details in our database."
    And The page contains "We can't show you the vehicle's annual test history until the vehicle details are corrected."
    And The page contains "Review what information DVSA has on your vehicle and let us know which information is wrong or missing by "

    And I click the "contacting DVSA" link
    And I go to the next tab
    Then The page title contains "Contact DVSA - GOV.UK"
    And I close extra tabs