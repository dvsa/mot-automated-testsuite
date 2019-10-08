@mothhgv @mothint
Feature: 13 - Check HGV vehicle with current valid Annual Test

  Scenario: A MOTH user searches for a HGV with a current a current Annual Test
    Given I browse to /
    And I enter "HISTVAL" in the registration field
    When I press the "Continue" button

    Then The page does not contain "This vehicle's MOT has expired"
    And The page contains "HISTVAL"
    And The page contains "DAF FA LF45.130 08"
    And The page contains "Check another vehicle"
    And The page contains "Colour"
    And The page contains "Fuel type"
    And The page contains "Date registered"
    And The page contains "Annual test valid until"
    And The page contains "31 October 2018"
    And The page does not contain "reminders"
    And The page contains "If you think the annual test expiry date or any of the vehicle details are wrong, contact DVSA."

    And I click the "contact" link
    And I go to the next tab
    Then The page title contains "Contact DVSA - GOV.UK"
    And I go to the next tab
    And I close extra tabs

    And The page contains "Annual test results"
    And The page contains "Check test expiry date, defects and advisories"
    And The page does not contain "Outstanding vehicle recalls"
    And I click the accordion section with the id "mot-history-description"

    And The page contains "Date tested"
    And The page contains "17 October 2017"
    And The page contains "Test Certificate number"
    And The page contains "GG80715"
    And The page contains "Test location"
    And The page contains "unavailable until further notice"
    And The page contains "Pass"
    And The page contains "Expiry date"
    And The page contains "9 December 2014"
    And The page contains "Fail"
    And The page contains "Reason(s) for failure"
    And The page contains "Advisory notice item(s)"
    And The page does not contain "MOT test number"
    And The page does not contain "Mileage"

    And The page contains "The annual test changed on 20 May 2018"
    And The page does not contain "Defects are now categorised according to their severity – dangerous, major, and minor."
    And The page contains "Find out more"

    And I click the last "Find out more" link
    And I go to the next tab
    Then The page title contains "MOT inspection manual: heavy goods vehicles (HGVs) - GOV.UK"
    And I go to the next tab
    And I close extra tabs

    And The page contains "What are failures?"
    And I click the "What are failures?" help link
    And The page contains "Failure items must be fixed before the vehicle can pass its annual test."

    And The page contains "What are advisories?"
    And I click the "What are advisories?" help link
    And The page contains "Advisory items are provided for advice. For some of these, if they became more serious, your vehicle may no longer be roadworthy and could require immediate attention."

    And The page contains "What are failures and advisories?"
    And I click the "What are failures and advisories?" help link
    And The page contains "Failure items must be fixed before the vehicle can pass its annual test."
    And The page contains "Advisory items are provided for advice. For some of these, if they became more serious, your vehicle may no longer be roadworthy and could require immediate attention."

  Scenario: A MOTH user searches for a HGV with test after eu go live date looks like pre eu test and handles PG9 tests correctly
    Given I browse to /
    And I enter "PROHIBH" in the registration field
    When I press the "Continue" button

    Then The page contains "RENAULT"
    And The page contains "SCANIA"
    And The page contains "The annual test changed on 20 May 2018"
    And The page does not contain "Defects are now categorised according to their severity – dangerous, major, and minor."
    And The page contains "Find out more"

    And I click the accordion section with the id "mot-history-description"
    And The page contains "7 July 2018"
    And The page contains "Brake Systems and Components - Prohibition"
    And The page contains "Brake Systems and Components - Failure"

    And The page contains "PG9PARTRE"
    And The page contains "PG9FULL"
    And The page does not contain "Condition of Body - PG9 FULL INSPECTION & FEE - Fail"
    And The page does not contain "Condition of Body - PG9 PART PAID RETEST - Prohibition"