@extended
Feature: MTS Notification - Home Page Alerts

  Scenario: Verify that the new homepage alerts are correctly linked to the inbox and the selected filter is chosen by default
    #Login as a scheme user and click the 'View all messages' link on homepage
    Given I login without 2FA using "SCHEME_USER" as {schemeUser}
    And I click the "View all messages" link

    And I click the "Home" link
    And I press the "Special Notices" button
    // and i check that the noti and rn checkboxes are not selected

    And I click the "Home" link
    And I press the "Notifications" button
    //and i check the sn and rn checkboxes are not selected

    And I click the "Home" link
    And I press the "Role Nominations" button
    //and i check the sn and noti checkboxes are not selected

