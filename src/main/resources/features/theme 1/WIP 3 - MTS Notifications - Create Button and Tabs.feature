@extended
Feature: MTS Notification - Verify 'Create Button' and Correct Tabs Visible

  Scenario: Verify that the scheme user is able to see the 'Create Message' button in the new inbox and only a scheme user can see the published/draft/removed tabs

      #Login as a scheme user and click the 'View all messages' link on homepage. Verify that the create a message button and inbox/archived/draft/removed/published tabs are visible for scheme user
    Given I login without 2FA using "SCHEME_USER" as {schemeUser}
    And I click the "View all messages" link
    And The page contains "Inbox"
    And The "Create a message" link is visible
    And The "Archived" link is visible
    And The "Published" link is visible
    And The "Draft" link is visible
    And The "Removed" link is visible
    And I click the "Sign out" link

    #Login as a tester and verify that the inbox/archived tabs are visible and there are no published/draft/removed tabs.
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    And I click the "View all messages" link
    And The page contains "Inbox"
    And The "Archived" link is visible
    And The page does not contain "Published"
    And The page does not contain "Draft"
    And The page does not contain "Removed"
    And I click the "Sign out" link







