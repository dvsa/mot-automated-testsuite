Feature: 11 - Finance

  @regression
  Scenario: View AE transaction
    Given I login without 2FA using "FINANCE_USER" as {username}
    And I click the "AE information" link
    # We are purposefully picking AE with the most transactions available so we definately have results and pagination
    And I load "AEDM_USER_WITH_TRANSACTIONS" as {aedm}, {organisation}, {count}, {aeRef}
    And I enter {aeRef} in the "Authorised Examiner ID" field
    And I press the "Search" button
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
    And I enter "2017" in the "Year" field in fieldset "From"

    And I enter "09" in the "Day" field in fieldset "To"
    And I enter "10" in the "Month" field in fieldset "To"
    And I enter "2017" in the "Year" field in fieldset "To"

    And I click the "Update results" button
    Then The summary line contains "01/10/2017 and 09/10/2017"

  # The below scenario contains a bug - BL-5595 where
  # the cheque details were not being stored
  # therefore the cheque details will not show up in full
  @regression
  Scenario: A finance user buys slots for an AE and processes the payment as a Cheque payment
    Given I login without 2FA using "FINANCE_USER" as {username}
    And I click the "AE information" link
    And I load "AE_USER" as {aeusername}, {aename}, {slots}
    And I enter {aeusername} in the "Authorised Examiner ID" field
    And I press the "Search" button

    And I click the "Buy slots" link

    And I click the "Cheque" radio button
    And I press the "Start order" button

    And I set today as {day}, {month}, {year}
    And I enter {day} in the "Day" field
    And I enter {month} in the "Month" field
    And I enter {year} in the "Year" field
    And I enter "1234" in the field with id "slipNumber"

    And I enter "12345" in the field with id "chequeNumber"
    And I enter "Automated testing" in the field with id "accountName"
    And I enter "205.00" in the field with id "amount"

    And I press the "Create order" button
    And I click the link "Confirm order" with id "confirmOrder"

    Then The page contains "Order confirmed"
    And I click the link "View purchase details" with id "purchaseDetails"
    Then The page contains "Transaction details"
    And I check the "Status" field row has value "Success"

    And I click the "purchase history" link
    And I click the link "Return to Authorised Examiner" with id "returnToExaminer"

  @smoke @cpms
  Scenario Outline: A finance user generates the <report> report for <startDate> days ago to <endDate> days ago
    Given I login without 2FA using "FINANCE_USER" as {username}
    And I click the "Generate financial reports" link
    And I select "<report>" in the field with id "input_report_type"
    And I get the date <startDate> days ago as {start_day}, {start_month}, {start_year}
    And I set the finance report from date to {start_day} {start_month} {start_year}
    And I get the date <endDate> days ago as {end_day}, {end_month}, {end_year}
    And I set the finance report to date to {end_day} {end_month} {end_year}
    And I press the "Generate report" button
    And I record the finance report URL
    And I wait "<seconds>" seconds then "<refresh>" the page until "Download Report" button displays
    And I click "Download Report" and check the CSV contains:
      | receipt_reference            |
      | total_amount                 |
      | status_description           |
      | Successful payment received  |
      | payment_type                 |

    Examples:
      | report                | startDate | endDate | seconds  | refresh |
      | All Payments          | 30        | 0       | 30       | 20      |

  @cpms
  Scenario Outline: A finance user generates the <report> finance report for <startDate> days ago to <endDate> days ago
    Given I login without 2FA using "FINANCE_USER" as {username}
    And I click the "Generate financial reports" link
    And I select "<report>" in the field with id "input_report_type"
    And I get the date <startDate> days ago as {start_day}, {start_month}, {start_year}
    And I set the finance report from date to {start_day} {start_month} {start_year}
    And I get the date <endDate> days ago as {end_day}, {end_month}, {end_year}
    And I set the finance report to date to {end_day} {end_month} {end_year}
    And I press the "Generate report" button
    And I record the finance report URL
    And I wait "<seconds>" seconds then "<refresh>" the page until "Download Report" button displays
    And I click "Download Report" and check the CSV contains:
      | receipt_reference            |
      | total_amount                 |
      | status_description           |
      | payment_type                 |

    Examples:
      | report                | startDate | endDate | seconds  | refresh |
      | All Payments          | 14        | 7       | 30       | 20      |
      | All Payments          | 212       | 182     | 30       | 20      |

  @cpms
  Scenario Outline: A finance user generates the <report> finance report for <startDate> days ago to <endDate> days ago
    Given I login without 2FA using "FINANCE_USER" as {username}
    And I click the "Generate financial reports" link
    And I select "<report>" in the field with id "input_report_type"
    And I get the date <startDate> days ago as {start_day}, {start_month}, {start_year}
    And I set the finance report from date to {start_day} {start_month} {start_year}
    And I get the date <endDate> days ago as {end_day}, {end_month}, {end_year}
    And I set the finance report to date to {end_day} {end_month} {end_year}
    And I press the "Generate report" button
    And I record the finance report URL
    And I wait "<seconds>" seconds then "<refresh>" the page until "Download Report" button displays
    And I click "Download Report" and check the CSV contains:
      | Co                     |
      | CC                     |
      | SPARE                  |
      | Debit                  |
      | Credit                 |
      | Line Description       |

    Examples:
      | report                | startDate | endDate | seconds  | refresh |
      | General Ledger Sales  | 30        | 0       | 30       | 20      |
      | General Ledger Sales  | 14        | 7       | 30       | 20      |
      | General Ledger Sales  | 212       | 182     | 30       | 20      |

  @cpms
  Scenario Outline: A finance user generates the <report> report for <startDate> days ago to <endDate> days ago
    Given I login without 2FA using "FINANCE_USER" as {username}
    And I click the "Generate financial reports" link
    And I select "<report>" in the field with id "input_report_type"
    And I get the date <startDate> days ago as {start_day}, {start_month}, {start_year}
    And I set the finance report from date to {start_day} {start_month} {start_year}
    And I get the date <endDate> days ago as {end_day}, {end_month}, {end_year}
    And I set the finance report to date to {end_day} {end_month} {end_year}
    And I press the "Generate report" button
    And I record the finance report URL
    And I wait "<seconds>" seconds then "<refresh>" the page until "Download Report" button displays
    And I click "Download Report" and check the CSV contains:
      | Order No                   |
      | Product                    |
      | VAT Rate                   |
      | Net Amount                 |
      | VAT Charged                |
      | Total Value                |

    Examples:
      | report                | startDate | endDate | seconds  | refresh |
      | Transaction Breakdown | 30        | 0       | 30       | 20      |
      | Transaction Breakdown | 14        | 7       | 30       | 20      |
      | Transaction Breakdown | 212       | 182     | 30       | 20      |
