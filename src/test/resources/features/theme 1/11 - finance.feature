@regression
Feature: 11 - Finance

  # Logged in as finance user HERA5734

  # Scenario: View AE transaction
    # Search for AE with lots of transactions - AE085610
    # Selected transaction history
    # Downloaded CSV, compared to screen results
    # Selected Today filter - verified transactions displayed were relevant to filter
    # Selected Last 7 days filter - verified transactions displayed were relevant to filter
    # Selected Last 30 days filter - verified transactions displayed were relevant to filter
    # Selected All transactions - verified transactions displayed were relevant to filter
    # Tested pagination
    # Viewed and printed various transactions
    # Tried various custom date range filters

  # The below scenario contains a bug - BL-5595 where
  # the cheque details were not being stored
  # therefore the cheque details will not show up in full
   Scenario: A finance user buys slots for an AE and processes the payment as a Cheque payment

     Given I login without 2FA using "FINANCE_USER" as {username}
     And I click the "AE information" link
     And I load "AE_USER" as {aeusername}, {aename}, {slots}
     And I enter {aeusername} in the "AE Number" field
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

  # Scenario: Generate finance reports
    # Generated all payments report - Waited and refreshed page until link was displayed (Can take many minutes)
    # Generated general ledger sales report  - Waited and refreshed page until link was displayed (Can take many minutes)
    # Reports generated for default dates, last month, and last 6 months

