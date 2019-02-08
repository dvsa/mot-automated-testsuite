@regression
Feature: 17 - Scheme user

  Scenario: Verify that the scheme user is able to create a new SN from the new inbox, preview the SN and then save as a draft and then publish and finally delete the SN from the new inbox.
    #Login as a scheme user and click the 'View all messages' link on homepage and then 'Create a message'
    Given I login without 2FA using "SCHEME_USER" as {schemeUser}
    And I set today as {day}, {month}, {year}
    And I click the "View all messages" link
    And I click the "Create a message" link

    #Select the SN radio button and continue, populate all details within the page
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
<<<<<<< HEAD
    And I enter "Test Special Notice 1" in the "Message title" field
=======
    And I enter "Test SN 1" in the "Message title" field
>>>>>>> changes to new messages feature
    And I enter "This is a test special notice." in the field with id "noticeText"

    #Continue through journey and save as a draft and verify the page contains the correct values on the review page
    And I press the "Save and continue to review" button
    And The page title contains "Review and publish the message - MOT testing service"
<<<<<<< HEAD
    And The page contains "Test Special Notice 1"
    And The page contains "This is a test special notice"
    And The page contains "Message successfully saved as a draft"

    #Verify that the special notice can be previewed by clicking the button
    And I click the "Preview" link
    And The page contains "Test Special Notice 1"
    And The page contains "This is a test special notice"
    And The page contains "By acknowledging this message you agree to test to the standards detailed in this special notice"

    #Return to review message screen, then go to the homepage
    And I click the "Back to review message" link
    And The page title contains "Review and publish the message - MOT testing service"
    And I click the "Return to home" link

    #Logout and then log back in and click 'View all messages'
    And I click the "Sign out" link
    And I login without 2FA using "SCHEME_USER" as {schemeUser}
    And I click the "View all messages" link

    #Click the 'draft' tab and verify the user can see the created special notice and by clicking this, they're taken to the review screen
    And I click the "Draft" link
    And I click the "Test Special Notice 1" link
    And The page contains "Test Special Notice 1"
    And The page title contains "Review and publish the message - MOT testing service"
    And The page contains "This is a test special notice"

    #Publish the special notice and verify success page is shown
=======
    And The page contains "Test SN 1"
    And The page contains "This is a test special notice"
    And The page contains "Message successfully saved as a draft"

    #Verify that the SN can be previewed by clicking the button
    And I click the "Preview" link
    And The page contains "Test SN 1"
    And The page contains "This is a test special notice"
    And The page contains "By acknowledging this message you agree to test to the standards detailed in this special notice"

    #Return to review message screen, go to the homepage and return to the new inbox (saves as a draft)
    And I click the "Back to review message" link
    And The page title contains "Review and publish the message - MOT testing service"
    And I click the "Return to home" link
    And I click the "View all messages" link

    #Click the 'draft' tab and verify the user can see the created SN and by clicking this, they're taken to the review screen
    And I click the "Draft" link
    And I click the "Test SN 1" link
    And The page contains "Test SN 1"
    And The page title contains "Review and publish the message - MOT testing service"
    And The page contains "This is a test special notice"

    #Publish the SN and verify success page is shown
>>>>>>> changes to new messages feature
    And I press the "Publish" button
    And The page title contains "Message scheduled for publishing - MOT testing service"
    And The page contains "Message scheduled for publishing"
    And The page contains "Special notice issue number:"

<<<<<<< HEAD
    #Return to new inbox and check the special notice is within the 'Published' tab and clicking it takes user to review screen
    And I click the "View messages" link
    And I click the "Published" link
    And I click the "Test Special Notice 1" link
    And The page contains "View a published message"

    #Remove the special notice and verify the removal success page
=======
    #Return to new inbox and check the SN is within the 'Published' tab and clicking it takes user to review screen
    And I click the "View messages" link
    And I click the "Published" link
    And I click the "Test SN 1" link
    And The page contains "View a published message"

    #Remove the SN and verify the removal success page
>>>>>>> changes to new messages feature
    And I click the "Remove" link
    And I press the "Remove message" button
    And The page contains "Successfully removed the published message"

<<<<<<< HEAD
    #Return to new inbox and click the removed tab and verify the special notice is shown there
    And I click the "View messages" link
    And I click the "Removed" link
    And The page contains "Test Special Notice 1"
=======
    #Return to new inbox and click the removed tab and verify the SN is shown there
    And I click the "View messages" link
    And I click the "Removed" link
    And The page contains "Test SN 1"
>>>>>>> changes to new messages feature


  Scenario: Scheme user performs AE search, then views AE details
    Given I load "AE_NOT_REJECTED" as {aeReference}, {aeName}
    And I login without 2FA using "SCHEME_USER" as {schemeUser}

    When I click the "AE information" link
    And I enter {aeReference} in the "Authorised Examiner ID" field
    And I press the "Search" button

    Then The page title contains "Authorised Examiner"
    And I check the "Name" field row has value {aeName}
    And I check the "Authorised Examiner ID" field row has value {aeReference}
