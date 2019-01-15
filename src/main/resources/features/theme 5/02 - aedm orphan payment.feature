@cpms
Feature: 02 - AEDM Orphan Payment

  @smoke
  Scenario: AEDM buys slots with an orphan payment
    Given I login with 2FA using "AEDM_USER" as {AEDM}, {ORGANISATION}
    And I click the first {ORGANISATION} link
    And I order 123 slots
    And I enter card details from csv "CARD_DETAILS"
    And I enter the card holders name as "Jimi Orphan Hendrix"
    And I make an orphan payment for card from csv "CARD_DETAILS"
    And The page contains "Please confirm that you wish to save the following card details for future use."
    Then I browse to /
