@cpms
Feature: 04 - Transaction History Check as AEDM


  Scenario: AEDM can view transaction history
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

    When I enter "01" in the "Day" field in fieldset "From"
    And I enter "10" in the "Month" field in fieldset "From"
    And I enter "2017" in the "Year" field in fieldset "From"

    And I enter "09" in the "Day" field in fieldset "To"
    And I enter "10" in the "Month" field in fieldset "To"
    And I enter "2017" in the "Year" field in fieldset "To"

    And I click the "Update results" button
    Then The summary line contains "01/10/2017 and 09/10/2017"


  Scenario: AEDM can see Print button on the Transaction Details page
    Given I login with 2FA using "AEDM_USER_WITH_TRANSACTIONS" as {AEDM}, {ORGANISATION}, {COUNT}
    And I click the last {ORGANISATION} link
    And I click the "Transaction history" link
    And I click the "All Transactions" link

    When I click the first link in the transaction table
    Then The "Print" button is visible on the page
