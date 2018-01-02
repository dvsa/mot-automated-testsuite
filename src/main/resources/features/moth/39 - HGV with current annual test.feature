@mothpp @mothint
Feature: 39 - Check HGV with a current Annual Test


  Scenario: A MOTH user searches for a HGV with a current a current Annual Test
    Given I browse to /
    And I enter "P8X7L16" in the registration field
    When I press the "Continue" button

    Then The page does not contain "This vehicle's MOT has expired"
    And The page contains "P8X7L16"
    And The page contains "NEW TRANSIT"
#    And The page contains "Check another vehicle" <-- this doesn't work due to the text being on 2 lines in the html code
    And The page contains "Check another"
    And The page does not contain "Colour"
    And The page does not contain "Fuel type"
    And The page does not contain "Date registered"
    And The page contains "Annual test valid until"
    And The page contains "16 March 2018"

    And The page contains "Get an annual test reminder"
    And The page contains "by email or text."
    When I click the "Get an annual test reminder" link
    And I go to the next tab
    Then The page title contains "Get MOT reminders - GOV.UK"
    And I go to the next tab
    And I close extra tabs

    And The page contains "If you think the annual test expiry date or any of the vehicle details are wrong,"
    And I click the "contact" link
    And I go to the next tab
    Then The page title contains "Contact DVSA"
    And I go to the next tab
    And I close extra tabs

    And The page contains "Annual test results"
    And The page contains "Check test expiry date, defects and advisories"
    And The page does not contain "Outstanding vehicle recalls"
    And I click the accordion section with the id "mot-history-description"

    And The page contains "Date tested"
    And The page contains "7 March 2017"
    And The page contains "Test location"
    #    And The page contains "unavailable until further notice" <-- uncomment later once local testing is complete
    And The page contains "Pass"
    And The page contains "Expiry date"
    And The page contains "16 March 2018"
    And The page contains "Fail"
    And The page contains "Reason(s) for failure"
    And The page contains "Advisory notice item(s)"
    And The page does not contain "MOT test number"
    And The page does not contain "Mileage"

    And The page contains "What are failures?"
    And I click the "What are failures?" help link
    And The page contains "Failure items must be fixed before the vehicle can pass its MOT."

    And The page contains "What are advisories?"
    And I click the "What are advisories?" help link
    And The page contains "Advisory items are provided for advice. For some of these, if they became more serious, your vehicle may no longer be roadworthy and could require immediate attention."

  Scenario: A MOTH user searches for a HGV with a current a current Annual Test
    Given I browse to /
    And I enter "M7X9G27" in the registration field
    When I press the "Continue" button

    Then The page does not contain "This vehicle's MOT has expired"
    And The page contains "M7X9G27"
    And The page contains "IVECO FORD CARGO ML75E17"
    And The page contains "Check another"
    And The page does not contain "Colour"
    And The page does not contain "Fuel type"
    And The page does not contain "Date registered"
    And The page contains "Annual test valid until"
    And The page contains "28 February 2018"
    And The page does not contain "Outstanding vehicle recalls"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "What are failures and advisories?"
    And I click the "What are failures and advisories?" help link
    And The page contains "Failure items must be fixed before the vehicle can pass its MOT."
    And The page contains "Advisory items are provided for advice. For some of these, if they became more serious, your vehicle may no longer be roadworthy and could require immediate attention."
