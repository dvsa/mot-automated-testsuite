@cpms
Feature: 5 - Payment Search as Finance User

   Scenario: A finance user searches for payments by Payment reference
     Given I login without 2FA using "FINANCE_USER" as {username}
     And I load immediately "FINANCE_PAYMENTS" as {payment_ref}, {payment_date}, {payment_type}, {amount}, {invoice_ref}, {ae_ref}
     And I click the "Payments" link
     And I click the "Payment reference" radio button
     And I enter {payment_ref} in the field with id "inputReference"
     When I press the "Search" button
     Then The page contains "{payment_ref}"
     And The page contains "{payment_date}"
     And The page contains "{payment_type}"
     And The page contains "{amount}"
     And I click the "{payment_ref}" link
     Then The page contains "{payment_ref}"
#     And The page contains "{payment_date}"
     And The page contains "{payment_type}"
     And The page contains "{amount}"
     And The page contains "{invoice_ref}"
     And The page contains "{ae_ref}"

  Scenario: A finance user searches for payments by Payment reference using the last 8 characters
    Given I login without 2FA using "FINANCE_USER" as {username}
    And I load immediately "FINANCE_PAYMENTS" as {payment_ref}, {payment_date}, {payment_type}, {amount}, {invoice_ref}, {ae_ref}
    And I click the "Payments" link
    And I click the "Payment reference" radio button
    And I enter the last 8 characters of {payment_ref} in the field with id "inputReference"
    When I press the "Search" button
    Then The page contains "{payment_ref}"
    And The page contains "{payment_date}"
    And The page contains "{payment_type}"
    And The page contains "{amount}"
    And I click the "{payment_ref}" link
    Then The page contains "{payment_ref}"
#    And The page contains "{payment_date}"
    And The page contains "{payment_type}"
    And The page contains "{amount}"
    And The page contains "{invoice_ref}"
    And The page contains "{ae_ref}"

  Scenario: A finance user searches for payments by Invoice reference
    Given I login without 2FA using "FINANCE_USER" as {username}
    And I load immediately "FINANCE_PAYMENTS" as {payment_ref}, {payment_date}, {payment_type}, {amount}, {invoice_ref}, {ae_ref}
    And I click the "Payments" link
    And I click the "Invoice reference" radio button
    And I enter {invoice_ref} in the field with id "inputReference"
    When I press the "Search" button
    Then The page contains "{invoice_ref}"
    And The page contains "{payment_date}"
    And The page contains "{payment_type}"
    And The page contains "{amount}"
    And I click the "{invoice_ref}" link
    Then The page contains "{payment_ref}"
#    And The page contains "{payment_date}"
    And The page contains "{payment_type}"
    And The page contains "{amount}"
    And The page contains "{invoice_ref}"
    And The page contains "{ae_ref}"

  Scenario: A finance user searches for payments by Invoice reference using the last 8 characters
    Given I login without 2FA using "FINANCE_USER" as {username}
    And I load immediately "FINANCE_PAYMENTS" as {payment_ref}, {payment_date}, {payment_type}, {amount}, {invoice_ref}, {ae_ref}
    And I click the "Payments" link
    And I click the "Invoice reference" radio button
    And I enter the last 8 characters of {invoice_ref} in the field with id "inputReference"
    When I press the "Search" button
    Then The page contains "{invoice_ref}"
    And The page contains "{payment_date}"
    And The page contains "{payment_type}"
    And The page contains "{amount}"
    And I click the "{invoice_ref}" link
    Then The page contains "{payment_ref}"
#    And The page contains "{payment_date}"
    And The page contains "{payment_type}"
    And The page contains "{amount}"
    And The page contains "{invoice_ref}"
    And The page contains "{ae_ref}"

  Scenario: A finance user searches for payments with an invalid references returns validation errors
    Given I login without 2FA using "FINANCE_USER" as {username}
    And I click the "Payments" link
    And I click the "Payment reference" radio button
    When I press the "Search" button
    Then The page contains "Value is required and can't be empty"
    And I enter "ABCDEF" in the field with id "inputReference"
    When I press the "Search" button
    Then The page contains "The input is less than 8 characters long"
    And I enter "ABCDEFGH" in the field with id "inputReference"
    When I press the "Search" button
    Then The page contains "No item match the reference provided was found"
