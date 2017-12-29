@mothpp @mothint
Feature: 38 - Check HGV with an expired Annual Test

  Scenario: A MOTH user searches for a HGV with an expired Annual Test
    Given I browse to /
    And I enter "M5H4S20" in the registration field
    When I press the "Continue" button

    Then The page contains "This vehicle's MOT has expired"
    And The page contains "You can be fined up to £1000 for driving without a valid MOT"
    And The page contains "M5H4S20"
    And The page contains "CONVERSION"
#    And The page contains "Check another vehicle" <-- this doesn't work due to the text being on 2 lines in the html code
    And The page contains "Check another"
    And The page does not contain "Colour"
    And The page does not contain "Fuel type"
    And The page does not contain "Date registered"
    And The page contains "Annual test expired on"
    And The page contains "21 April 2017"

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
    And The page contains "22 April 2016"
    And The page contains "Test location"
    #    And The page contains "unavailable until further notice" <-- uncomment later once locale testing is complete
    And The page contains "Pass"
    And The page contains "Expiry date"
    And The page does not contain "MOT test number"
    And The page does not contain "Mileage"

    And The page contains "21 April 2016"
    And The page contains "Fail"
    And The page contains "Reason(s) for failure"
    And The page contains "Advisory notice item(s)"

    And The page contains "What are failures and advisories?"
    And I click the "What are failures and advisories?" help link
    And The page contains "Failure items must be fixed before the vehicle can pass its MOT."
    And The page contains "Advisory items are provided for advice. For some of these, if they became more serious, your vehicle may no longer be roadworthy and could require immediate attention."

    And The page contains "What are failures?"
    And I click the "What are failures?" help link
    And The page contains "Failure items must be fixed before the vehicle can pass its MOT."

    And The page contains "What are advisories?"
    And I click the "What are advisories?" help link
    And The page contains "Advisory items are provided for advice. For some of these, if they became more serious, your vehicle may no longer be roadworthy and could require immediate attention."
