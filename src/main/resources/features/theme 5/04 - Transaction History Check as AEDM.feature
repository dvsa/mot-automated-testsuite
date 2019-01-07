@cpms
Feature: 04 - Transaction History Check as AEDM


  Scenario: AEDM can view transaction history
    # We are purposefully picking AE with the most transactions available so we definately have results and pagination
    Given I login with 2FA using "AEDM_USER_WITH_TRANSACTIONS" as {AEDM}, {ORGANISATION}, {COUNT}, {AEREF}
    And I click the first {ORGANISATION} link
    And I click the "Transaction history" link

    When I click the "Today" link
    Then The summary line contains "today"

    When I click the "Last 7 days" link
    Then The summary line contains "last 7 days"

    When I click the "Last 30 days" link
    Then The summary line contains "last 30 days"

    # All transactions is the only link we are guaranteed to have CSV link and pagination
    When I click the "All Transactions" link
    Then The summary line contains "all transactions"
    And I check there is a "CSV (excel)" link
    And I check there is a "2" link
    And I check there is a "3" link
    And I check there is no "Previous" link
    And I check there is a "Next" link

    When I click the "Next" link
    Then I check there is a "1" link
    And I check there is a "3" link
    And I check there is a "Previous" link
    And I check there is a "Next" link

    When I click the "Previous" link
    And I check there is a "2" link
    And I check there is a "3" link
    And I check there is no "Previous" link
    And I check there is a "Next" link

    When I enter "01" in the "Day" field in fieldset "From"
    And I enter "10" in the "Month" field in fieldset "From"
    And I enter "2018" in the "Year" field in fieldset "From"
    And I enter "09" in the "Day" field in fieldset "To"
    And I enter "10" in the "Month" field in fieldset "To"
    And I enter "2018" in the "Year" field in fieldset "To"

    And I click the "Update results" button
    Then The summary line contains "01/10/2018 and 09/10/2018"


  Scenario: AEDM can see Print button on the Transaction Details page
    Given I login with 2FA using "AEDM_USER_WITH_TRANSACTIONS" as {AEDM}, {ORGANISATION}, {COUNT}, {AEREF}
    And I click the first {ORGANISATION} link
    And I click the "Transaction history" link
    And I click the "All Transactions" link

    When I click the first link in the transaction table
    Then The "Print" button is visible on the page
