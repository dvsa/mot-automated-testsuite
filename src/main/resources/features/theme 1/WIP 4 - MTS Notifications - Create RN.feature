@extended
Feature: MTS Notification - Create RN a

  Scenario: Verify that a RN can be created for a tester and then can be clicked from the RN alert on the homepage and then accepted. When accepted it can then be archived and viewed in the correct archived tab.

      #Login as a scheme user and navigate to the assign a role page
    Given I login without 2FA using "SCHEME_USER" as {schemeUser}
    And I load uniquely "TESTER_GROUP_B_AND_NOT_A" as {testerUsername}, {testerName}
    And I load "SITE_ALL" as {siteReference}, {siteName}
    And I search for site by reference {siteReference}
    And I click the "Assign a role" link

    #Search for the tester's username and assign a role to them
    And I enter {testerUsername} in the field with id "userSearchBox"
    And I click the "Search" button
    And I select the first radio button
    And I click the "Choose role" button
    And I click the "Assign role" button

    #Verify the page contains the correct content and the scheme user then logs out
    And The page contains "You have assigned a role to"
    And The page contains "They have been sent a notification"
    And I click the "Sign out" link

    #Verify the tester is able to login and see the role nomination notification and accept this
    And I login with 2FA as {testerUsername}
    And I click the link "role nominations" with id "role-nomination-alert"
    And I click the first "Tester nomination" link
    And I click the "Accept" button
    And The page contains "Notification successfully accepted"

    #Click home, then the 'view all messages' link
    And I click the first "Home" link
    And I click the "View all messages" link

    #Click the tester nomination link and verify the content on page. Then archive the message
    And I click the first "Tester nomination" link
    And The page contains "You have accepted the role of"
    And I click the "Archive this message" button

    #Verify the content is correct when archiving the message and then click the 'Archive' tab
    And The page contains "Notification successfully archived"
    And I click the "Archived" link
    And the page contains "Tester nomination"



    