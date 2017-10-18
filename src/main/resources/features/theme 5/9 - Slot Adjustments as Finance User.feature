@cpms
Feature: 09 - Slot Adjustments as Finance User


  Scenario: A finance user increases an AE's slots
    Given I login without 2FA using "FINANCE_USER" as {username}
    And I click the "AE information" link
    And I load "AE_USER_SLOTADJUST" as {aeusername}, {aename}, {slots}, {slotsup}, {slotsdown}
    And I enter {aeusername} in the "AE Number" field
    And I press the "Search" button
    And I click the "Slot adjustment" link
    And I click the "Add slots" radio button
    And I enter "100" in the "Number of slots" field
    And I select "Other" in the field with id "inputReason"
    And I enter "9 - slot adjustment positive" in the field with id "inputComment"
    And I press the "Review slot adjustment" button

    When The page contains "Add 100 slots"
    And The page contains "new balance will be {slotsup} slots"
    And The page contains "MOT04"
    And The page contains "9 - slot adjustment positive"

    And I press the "Adjust slots" button
    And I click the "Transaction history" link
    Then The page contains "Manual Adjustment"
    And The page contains "100"
    And The page contains "£0.00"


  Scenario: A finance user decreases an AE's slots
    Given I login without 2FA using "FINANCE_USER" as {username}
    And I click the "AE information" link
    And I load "AE_USER_SLOTADJUST" as {aeusername}, {aename}, {slots}, {slotsup}, {slotsdown}
    And I enter {aeusername} in the "AE Number" field
    And I press the "Search" button
    And I click the "Slot adjustment" link
    And I click the "Remove slots" radio button
    And I enter "100" in the "Number of slots" field
    And I select "Other" in the field with id "inputReason"
    And I enter "9 - slot adjustment negative" in the field with id "inputComment"
    And I press the "Review slot adjustment" button

    When The page contains "Remove 100 slots"
    And The page contains "new balance will be {slotsdown} slots"
    And The page contains "MOT04"
    And The page contains "9 - slot adjustment negative"

    And I press the "Adjust slots" button
    And I click the "Transaction history" link
    Then The page contains "Manual Adjustment"
    And The page contains "-100"
    And The page contains "£0.00"

