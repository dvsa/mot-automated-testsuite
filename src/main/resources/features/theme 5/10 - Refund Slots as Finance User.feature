@cpms
Feature: 10 - Refund slots as Finance User


  Scenario: A finance user performs slot refunds
    Given I login with 2FA using "AEDM_USER_WITH_TRANSACTIONS" as {AEDM}, {ORGANISATION}, {TRANSACTIONS}, {AEREF}
    And I click the first {ORGANISATION} link
    And I order 100 slots
    And I enter card details from csv "CARD_DETAILS"
    And I enter the card holders name as "Purchase One"
    And I make the payment for card from csv "CARD_DETAILS"
    And I check that 100 slots were bought successfully
    And I click the "Back to Authorised Examiner" link
    And I order 150 slots
    And I enter card details from csv "CARD_DETAILS"
    And I enter the card holders name as "Purchase Two"
    And I make the payment for card from csv "CARD_DETAILS"
    And I check that 150 slots were bought successfully
    And I click the "Sign out" link

    Then I login without 2FA using "FINANCE_USER" as {username}
    And I click the "AE information" link
    And I enter {AEREF} in the "Authorised Examiner ID" field
    And I press the "Search" button

    # Partial batch refund 25 slots out of 150
    Then I record the current slot count as "slotCount"
    And I click the "Refund slots" link
    And I enter "25" in the "Number of slots to be refunded" field
    And I select "User requested" in the field with id "inputReason"
    And I press the "Continue" button
    Then The page contains "Slot refund summary"
    And The table field "Number of slots available" has value {slotCount}
    And The table field "Number of slots to be refunded" has value "25"
    And The table field "Total amount of refund" has value "£51.25"
    And The page contains "Partial"
    And I press the "Refund slots" button
    Then The page contains "Slot refund confirmation"
    And The page contains "successful"
    And The page contains "25 slots"
    And The page contains "£51.25"
    And I click the "Return to Authorised Examiner" link
    And I check the slot count "slotCount" is reduced by 25

    # Partial and full batch refund the remaining 125 slots plus 100 slots
    Then I record the current slot count as "slotCount2"
    And I click the "Refund slots" link
    And I enter "225" in the "Number of slots to be refunded" field
    And I select "User requested" in the field with id "inputReason"
    And I press the "Continue" button
    Then The page contains "Slot refund summary"
    And The table field "Number of slots available" has value {slotCount2}
    And The table field "Number of slots to be refunded" has value "225"
    And The table field "Total amount of refund" has value "£461.25"
    And The page contains "Partial"
    And The page contains "Full"
    And I press the "Refund slots" button
    Then The page contains "Slot refund confirmation"
    And The page contains "successful"
    And The page contains "225 slots"
    And The page contains "£461.25"
    And I click the "Return to Authorised Examiner" link
    And I check the slot count "slotCount2" is reduced by 225
