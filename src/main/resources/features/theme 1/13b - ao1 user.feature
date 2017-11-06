@regression
Feature: 13b - A01 user

  Scenario: AO1 user performs AE search, then remove a site association
    Given I load uniquely "AE_WITH_ASSIGNED_SITE" as {aeReference}, {aeName}, {siteReference}, {siteName}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button
    And I click the remove site association link for {siteName}
    And I select "Surrendered" in the "Choose a status" field
    And I press the "Remove association" button
    Then I check there is no site association for {siteReference}, {siteName}


  Scenario: AO1 user performs site search, then changes site details
    Given I load "SITE" as {siteReference}, {siteName}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I search for site by reference {siteReference}

    And I click the "Change" link for the "Name" field row
    And The page title contains "Change site name"
    And I enter "Example Site" in the "Site name" field
    And I press the "Change site name" button
    And The page title contains "Vehicle Testing Station"
    Then The page contains "Site name has been successfully changed."
    And I check the "Name" field row has value "Example Site"

    And I click the "Change" link for the "Status" field row
    And The page title contains "Change status"
    And I select "Lapsed" in the "Site status" field
    And I press the "Change site status" button
    And The page title contains "Vehicle Testing Station"
    And The page contains "Site status has been successfully changed."
    And I check the "Status" field row has value "Lapsed"

    And I click the "Change" link for the "Classes" field row
    And The page title contains "Change classes"
    And I clear the "Class 1" checkbox
    And I clear the "Class 2" checkbox
    And I clear the "Class 3" checkbox
    And I click the "Class 4" checkbox
    And I click the "Class 5" checkbox
    And I click the "Class 7" checkbox
    And I press the "Review classes" button
    And I press the "Change classes" button
    And The page title contains "Vehicle Testing Station"
    And The page contains "Classes have been successfully changed."
    And I check the "Classes" field row has value "4,5,7"


  Scenario: AO1 user performs site search, then assigns a role
    Given I load uniquely "SITE_MGR_AND_OTHER_TESTER" as {mgrUsername}, {siteName}, {siteReference}, {unassignedTesterUsername}, {unassignedTesterName}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I search for site by reference {siteReference}
    And I click the "Assign a role" link
    And The page title contains "Assign a role"
    And I enter {unassignedTesterUsername} in the field with id "userSearchBox"
    And I press the "Search" button
    And The page title contains "Choose a role"
    And I click the "Site admin" radio button
    And I press the "Choose role" button
    And The page title contains "Review role"
    And I press the "Assign role" button
    Then The page contains "You have assigned a role to {unassignedTesterName}, {unassignedTesterUsername}. They have been sent a notification."
    And I check for a "Site admin" role assignment for {unassignedTesterUsername}, {unassignedTesterName}


  Scenario: AO1 user performs site search, then removes a role
    Given I load uniquely "SITE_MGR_AND_TESTER_CLASS_4" as {testerUsername}, {testerName}, {mgrUsername}, {siteName}, {siteReference}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I search for site by reference {siteReference}
    And I click the remove site role link for {testerName}, {testerUsername}
    And The page title contains "Remove a role"
    And I press the "Remove role" button
    And The page title contains "Vehicle Testing Station"
    And The page contains "You have removed the role of Tester from {testerName}"


  Scenario: AO1 user performs site search, then views and records a site event
    Given I load "SITE" as {siteReference}, {siteName}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I search for site by reference {siteReference}
    And I click the "Events history" link
    And The page title contains "Events history"
    And I click the "Record an event" link

    And The page title contains "Record an event"
    And I select "VTS complaint" in the field with id "eventType"
    And I enter "30" in the "Day" field
    And I enter "06" in the "Month" field
    And I enter "2017" in the "Year" field
    And I press the "Continue" button

    And The page title contains "Record an event outcome"
    And I select "No further action" in the field with id "outcomeCode"
    And I enter "Test event" in the field with id "notes"
    And I press the "Continue" button

    And The page title contains "Event summary"
    And I press the "Record event" button

    Then The page title contains "Events history"
    And The page contains "A new event has been recorded."
