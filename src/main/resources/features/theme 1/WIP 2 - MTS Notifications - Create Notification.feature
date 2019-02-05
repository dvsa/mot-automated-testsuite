@extended
Feature: MTS Notification - Create and remove notification

  Scenario: Verify that the scheme user is able to create a new notification from the new inbox, preview the notification and then save as a draft and then publish and finally delete the notification from the new inbox.
    #Login as a scheme user and click the 'View all messages' link on homepage
    Given I login without 2FA using "SCHEME_USER" as {schemeUser}
    And I set today as {day}, {month}, {year}
    And I click the "View all messages" link
    And I click the "Create a message" link

    #Select the notification radio button and continue, populate all details within the page
    And I click the "Notification" radio button
    And I press the "Continue" button
    And The page title contains "Enter content, audience and publish date - MOT testing service"
    And I click the id "dvsa-roles" checkbox
    And I click the id "testers" checkbox
    And I click the id "class-1" checkbox
    And I click the id "class-2" checkbox
    And I click the id "class-3" checkbox
    And I enter {day} in the field with id "internal-day"
    And I enter {month} in the field with id "internal-month"
    And I enter {year} in the field with id "internal-year"
    And I enter "Test Notification 1" in the "Message title" field
    And I enter "This is a test notification 1." in the field with id "noticeText"

    #Continue through journey and save as a draft and verify the correct values are shown on screen
    And I press the "Save and continue to review" button
    And The page title contains "Review and publish the message - MOT testing service"
    And The page contains "Test Notification 1"
    And The page contains "This is a test notification 1"
    And The page contains "Message successfully saved as a draft"

    #Verify that the notification can be previewed
    And I click the "Preview" link
    And The page contains "Test Notification 1"
    And The page contains "This is a test notification 1"
    And The page contains "This is a preview of the message"

    #Return to review message screen, go to the homepage and return to the new inbox
    And I click the "Go back to the review page" link
    And The page title contains "Review and publish the message - MOT testing service"
    And I click the "Return to home" link
    And I click the "View all messages" link

    #Click the 'draft' tab and verify the user can see the created notification and by clicking this, they're taken to the review screen. Verify the page contains the correct content
    And I click the "Draft" link
    And I click the "Test Notification 1" link
    And The page contains "This is a test notification"
    And The page title contains "Review and publish the message - MOT testing service"
    And The page contains "This is a test notification"

    #Publish the notification and verify success page is shown
    And I press the "Publish" button
    And The page title contains "Message scheduled for publishing - MOT testing service"
    And The page contains "Message scheduled for publishing"

    #Return to new inbox and check the notification is within the 'Published' tab and clicking it takes user to review screen
    And I click the "View messages" link
    And I click the "Published" link
    And I click the "Test Notification 1" link
    And The page contains "View a published message"

    #Remove the notification and verify the removal success page
    And I click the "Remove" link
    And I press the "Remove message" button
    And The page contains "Successfully removed the published message"

    #Return to new inbox and click the removed tab and verify the notification is shown there
    And I click the "View messages" link
    And I click the "Removed" link
    And The page contains "Test Notification 1"




