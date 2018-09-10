@regression
Feature: 17 - Scheme user

  Scenario: Scheme user creates draft special notice, edits it, then retracts it
    Given I login without 2FA using "SCHEME_USER" as {schemeUser}
    And I set today as {day}, {month}, {year}
    When I click the first "Special notices" link
    And The page title contains "Special Notices"

    Then I click the "Create special notice" link
    And The page title contains "Select a message type"
    And I click the "Special notice" radio button
    And I press the "Continue" button
    And The page title contains "Create Special Notice"
    And I enter "Test Special Notice" in the "Subject title" field
    And I enter {day} in the "Day" field in fieldset "Internal publish date"
    And I enter {month} in the "Month" field in fieldset "Internal publish date"
    And I enter {year} in the "Year" field in fieldset "Internal publish date"
    And I enter {day} in the "Day" field in fieldset "External publish date"
    And I enter {month} in the "Month" field in fieldset "External publish date"
    And I enter {year} in the "Year" field in fieldset "External publish date"
    And I enter "10" in the "Acknowledgement period" field
    And I click the "Class 1" checkbox
    And I click the "Class 2" checkbox
    And I click the "Class 3" checkbox
    And I click the "DVSA Roles" checkbox
    And I click the "VTS Roles" checkbox
    And I enter "This is a test special notice." in the field with id "notice-text-input"
    And I press the "Preview" button
    Then The page title contains "Special Notice Preview"
    And I click the "Home" link
    And I click the first "Special notices" link

    And I click the last "View" link
    And I click the last "Edit" link
    And I enter "Edited Test Special Notice" in the "Subject title" field
    And I enter "This is an edited test special notice." in the field with id "notice-text-input"
    And I press the "Preview" button
    Then The page title contains "Special Notice Preview"
    And I press the "Publish special notice" button
    And I click the last "View" link
    And I click the last "Remove" link
    And I check the alert popup contains "This action will delete this notice from the library."
    And I accept the alert popup


  Scenario: Scheme user performs AE search, then views AE details
    Given I load "AE_NOT_REJECTED" as {aeReference}, {aeName}
    And I login without 2FA using "SCHEME_USER" as {schemeUser}

    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button

    Then The page title contains "Authorised Examiner"
    And I check the "Name" field row has value {aeName}
    And I check the "Authorised Examiner ID" field row has value {aeReference}
