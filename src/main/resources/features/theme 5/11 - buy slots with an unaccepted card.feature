@cpms
Feature: 11 - buy slots with an unaccepted card

  Scenario: AEDM tries to buy slots with a card that's not accepted
    Given I login with 2FA using "AEDM_USER" as {AEDM}, {ORGANISATION}
    And I get the slot count for organisation {ORGANISATION}
    And I order 100 slots
    And I enter card details from csv "UNACCEPTED_CARD_DETAILS"
    And The page contains "Card type not accepted"
    And I click the "Cancel" link
    And I click the "Home" link
    And I check a slot was not used for organisation {ORGANISATION}