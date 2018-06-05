@cpms
Feature: 02 - AEDM Orphan Payment

  @smoke
  Scenario: AEDM buys slots with an orphan payment
    Given I login with 2FA using "AEDM_USER" as {AEDM}, {ORGANISATION}
    And I click the first {ORGANISATION} link
    And I order 123 slots
    And I enter the card details "4462030000000000", "12/18", "654"
    And I enter the card holders name as "Jimi Orphan Hendrix"
    And I make an orphan payment for card "4462030000000000"
    Then I browse to /
