@regression
Feature: 17 - Scheme user

  Scenario: Scheme user creates special notice
    Given I login without 2FA using "SCHEME_USER" as {schemeUser}
    And I set today as {day}, {month}, {year}
    When I click the first "Special notices" link
    And The page title contains "Special Notices"

    Then I click the "Create special notice" link
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

    And The page title contains "Special Notice Preview"
    And I press the "Publish special notice" button

    Then The page title contains "Special Notices"
    And The page contains "Special notice created"


  # Scenario: Edit Special Notice
    # After special notice is published from above
    # Select Special Notices
    # Click special notice published in Create Special Notice
    # Click Edit
    # Preview
    # Publish Special Notice

  # Scenario: Retract Special Notice
    # Select Special Notices
    # Click Special Notice already published
    # Click Remove


  Scenario: Scheme user performs AE search, then views AE details
    Given I load "AE_NOT_REJECTED" as {aeReference}, {aeName}
    And I login without 2FA using "SCHEME_USER" as {schemeUser}

    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button

    Then The page title contains "Authorised Examiner"
    And I check the "Name" field row has value {aeName}
    And I check the "Authorised Examiner ID" field row has value {aeReference}
