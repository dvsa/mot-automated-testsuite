@regression
Feature: 09 - AEDM and AED

  @smoke
  Scenario: AEDM buys slots
    Given I login with 2FA using "AEDM_USER" as {AEDM}, {ORGANISATION}
    And I click the first {ORGANISATION} link
    And I order 25 slots
    And I enter the card details "4462030000000000", "12/18", "654"
    And I enter the card holders name as "Jimi Hendrix"
    And I make the payment for card "4462030000000000"
    And I check that 25 slots were bought successfully


  Scenario: AEDM assigns AED role
    Given I login with 2FA using "AEDM_AND_NON_AED_USER" as {aedmUsername}, {otherUsername}, {organisationName}, {otherName}
    And I click the first {organisationName} link
    And The page title contains "Authorised Examiner"

    When I click the "Assign a role" link
    And I enter {otherUsername} in the field with id "userSearchBox"
    And I press the "Search" button

    And The page contains "Choose a role"
    And I click the "Authorised Examiner Delegate" radio button
    And I press the "Choose role" button

    And I check the role summary has a new role of "Authorised Examiner Delegate"
    And I press the "Confirm" button

    Then The page contains "A role notification has been sent to {otherName} '{otherUsername}'"
    And I check there is pending "Authorised examiner delegate" role listed for {otherName}


  Scenario: AED assigns tester to role
    Given I login with 2FA using "AED_AND_TESTER" as {aedmUsername}, {organisation}, {siteName}, {siteNumber}, {tester}, {testerName}
    And I click the first {organisation} link
    And The page title contains "Authorised Examiner"
    And I click the {siteName} site link for site reference {siteNumber}
    And The page title contains "Vehicle Testing Station"

    And I click the "Assign a role" link
    And The page title contains "Assign a role"
    And I enter {tester} in the field with id "userSearchBox"
    And I press the "Search" button

    And The page title contains "Choose a role"
    And I click the "Tester" radio button
    And I press the "Choose role" button

    And The page title contains "Review role"
    And I check the role summary has a new role of "Tester"
    And I press the "Assign role" button

    Then The page title contains "Vehicle Testing Station"
    And The page contains "You have assigned a role to {testerName}, {tester}. They have been sent a notification."
    And I check there is pending "Tester" role listed for {testerName}


  Scenario: AEDM checks today's test log at VTS
    Given I load immediately "AEDM_AND_TESTER_AT_SITE" as {aedm}, {aeName}, {siteName}, {siteNumber}, {testerUsername}, {testerName}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {odometer}
    And I login with 2FA as {testerUsername}

    And I start an MOT test for {reg}, {vin}, {siteName}
    And The page title contains "Your home"
    And I click the "Enter test results" link
    And I enter an odometer reading in miles of {odometer} plus 5000
    And I enter decelerometer results of service brake 51 and parking brake 16
    And I press the "Review test" button
    And The page title contains "MOT test summary"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click the "Sign out" link

    When I login with 2FA as {aedm}
    And I click the first {aeName} link
    And I click the {siteName} link
    And I click the "Test logs" link
    And I click the "Today" link

    Then I check the site test log has the recent test {reg}, {testerUsername}


  Scenario Outline: AEDM can view TQI for site with <status> status (via organisation)
    Given I login with 2FA using "<dataSource>" as {aedm}, {organisationName}, {siteName}
    And I click the first {organisationName} link
    When I click the "Service reports" link
    And I click the {siteName} site link with status "<status>" on the service reports
    Then The page contains "This information will help you manage the quality of testing at your site. How you use it will depend on how you manage the site, its size and number of staff."
    And I check there is a "Download the group B report as a CSV (spreadsheet) file" link

  Examples:
  |status|dataSource         |
  |Green |AEDM_AND_GREEN_SITE|
  |Amber |AEDM_AND_AMBER_SITE|
  |Red   |AEDM_AND_RED_SITE  |


  Scenario: AED can view TQI for site (via VTS)
    Given I login with 2FA using "AED_AND_GROUP_A_SITE" as {aed}, {siteName}, {siteNumber}
    And I click the first "({siteNumber}) {siteName}" link
    When I click the "Test quality information" link
    Then The page contains "This information will help you manage the quality of testing at your site. How you use it will depend on how you manage the site, its size and number of staff."
    And I check there is a "Download the group A report as a CSV (spreadsheet) file" link


  Scenario: AEDM can view TQI for each VTS tester
    Given I login with 2FA using "AEDM_AND_TESTER_AT_SITE" as {aedm}, {aeName}, {siteName}, {siteNumber}, {testerUsername}, {testerName}
    And I click the first "({siteNumber}) {siteName}" link
    And I click the "Test quality information" link
    When I click the TQI link for tester {testerUsername}
    Then The page contains "{testerName}"


  Scenario: AEDM check transaction history
    Given I login with 2FA using "AEDM_USER" as {AEDM}, {ORGANISATION}
    And I click the first {ORGANISATION} link
    And I click the "Transaction history" link

    When I click the "Today" link
    Then The summary line contains "today"

    When I click the "Last 7 days" link
    Then The summary line contains "last 7 days"

    When I click the "Last 30 days" link
    Then The summary line contains "last 30 days"

    When I click the "All Transactions" link
    Then The summary line contains "all transactions"

    When I enter "01" in the "Day" field in fieldset "From" using legend
    And I enter "10" in the "Month" field in fieldset "From" using legend
    And I enter "2017" in the "Year" field in fieldset "From" using legend

    And I enter "09" in the "Day" field in fieldset "To" using legend
    And I enter "10" in the "Month" field in fieldset "To" using legend
    And I enter "2017" in the "Year" field in fieldset "To" using legend

    When I click the "Update results" button
    Then The summary line contains "01/10/2017 and 09/10/2017"