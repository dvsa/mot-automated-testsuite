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
    And The page title contains "Enter content, audience and publish date - MOT testing service"
    And I click the id "dvsa-roles" checkbox
    And I click the id "vts-roles" checkbox
    And I click the id "testers" checkbox
    And I click the id "class-1" checkbox
    And I click the id "class-2" checkbox
    And I click the id "class-3" checkbox
    And I enter {day} in the field with id "internal-day"
    And I enter {month} in the field with id "internal-month"
    And I enter {year} in the field with id "internal-year"
    And I enter {day} in the field with id "external-day"
    And I enter {month} in the field with id "external-month"
    And I enter {year} in the field with id "external-year"
    And I enter "10" in the "Number of days to acknowledge by" field
    And I enter "Test Special Notice" in the "Message title" field
    And I enter "This is a test special notice." in the field with id "noticeText"
    And I press the "Save and continue to review" button
    Then The page title contains "Review and publish the message - MOT testing service"
    And I click the "Return to home" link
    And I click the first "Special notices" link

    And I click the last "View" link
    And I click the last "Edit" link
    And I enter "Edited Test Special Notice" in the "Message title" field
    And I enter "This is an edited test special notice." in the field with id "noticeText"
    And I press the "Save and continue to review" button
    Then The page title contains "Review and publish the message - MOT testing service"
    And I press the "Publish" button
    And I click the last "View" link
    And I click the last "Remove" link
    And I check the alert popup contains "This action will delete this notice from the library."
    And I accept the alert popup


  Scenario: Scheme user performs AE search, then views AE details
    Given I load "AE_NOT_REJECTED" as {aeReference}, {aeName}
    And I login without 2FA using "SCHEME_USER" as {schemeUser}

    When I click the "AE information" link
    And I enter {aeReference} in the "Authorised Examiner ID" field
    And I press the "Search" button

    Then The page title contains "Authorised Examiner"
    And I check the "Name" field row has value {aeName}
    And I check the "Authorised Examiner ID" field row has value {aeReference}
