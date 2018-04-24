@cpms
Feature: 01 - Manage Direct Debit as AEDM

  Scenario: AEDM should be able to setup direct debit
    Given I login with 2FA using "AEDM_USER_NO_DD" as {AEDM}, {ORGANISATION}
    And I click the first {ORGANISATION} link
    And The page title contains "Authorised Examiner - MOT testing service"
    And I click the "Setup Direct Debit" link
    And I enter "100" in the "How many slots would you like each month?" field
    And I click the "5th of the month" radio button
    And I press the "Continue" button
    And The page contains "100 slots"
    And The page contains "£205.00 per month"
    And The page contains "5th of the month"
    And I click the "Continue to GoCardless" link
    And I enter "Andrew" in the "First name" field
    And I enter "Lai" in the "Last name" field
    And I enter "andrew.lai@bjss.com" in the "Email" field
    And I enter "20-00-00" in the "Your sort code" field
    And I enter "55779911" in the "Your account number" field
    And I click the button which contains text "or enter your address manually"
    And I enter "123 Derby Road" in the "Billing address line 1" field
    And I enter "Oakwood" in the "Billing address line 2" field
    And I enter "Derby" in the "Town or City" field
    And I enter "DE1 1DE" in the field with id "customer_postal_code"
    # And I click the "Set up Direct Debit" button
    And I click the button with class name "btn btn--large u-sm-full-width"
    And The page contains "20-00-00"
    And The page contains "55779911"
    # And I click the "Confirm" button
    And I click the button with class name "btn btn--large btn--primary u-sm-full-width"
    And The page contains "Direct debit confirmation"
    And The page contains "Your direct debit has been successfully created"
    And I click the "Return to Authorised Examiner" link
    And The page title contains "Authorised Examiner - MOT testing service"
    And The page contains "Direct debit"
    And The page contains "100 slots"
    And The page contains "£205.00 per month"
    And The page contains "5th of the month"
    And I click the "Manage Direct Debit" link
    And I click the "Cancel your direct debit" link
    And I click the "Cancel your direct debit" link
    And The page contains "Direct debit cancelled"
    And I click the "Return to Authorised Examiner" link
    And The page title contains "Authorised Examiner - MOT testing service"
    And The page does not contain "Direct debit"
    And The page does not contain "100 slots"
    And The page does not contain "£205.00 per month"
    And The page does not contain "5th of the month"

