@regression
Feature: 13 - Finance

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

  # Scenario: Cheque payment
    # Search for AE - AE085610
    # Noted down the number of slots for AE - 200
    # Made cheque payment - 01/06/2017,123456,123456, Andrew Lai, 205.00
    # Confirmed order
    # Viewed purchase details
    # Printed transaction
    # Returned to AE screen and verified the slot count had been increased to 300

  # Scenario: Generate finance reports
    # Generated all payments report - Waited and refreshed page until link was displayed (Can take many minutes)
    # Generated general ledger sales report  - Waited and refreshed page until link was displayed (Can take many minutes)
    # Reports generated for default dates, last month, and last 6 months