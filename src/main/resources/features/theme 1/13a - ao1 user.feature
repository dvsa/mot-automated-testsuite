@regression
Feature: 13a - A01 user

  Scenario: AO1 user performs AE search, then changes AE status and Area Office
    Given I load "AE_NOT_REJECTED" as {aeReference}, {aeName}
    And I login without 2FA using "AO1_USER" as {ao1User}

    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button
    And I check the "Authorised Examiner ID" field row has value {aeReference}
    And I click the "Change" link for the "Status" field row
    And I select "Rejected" in the "Status" field
    And I press the "Change status" button
    Then The page contains "Status has been successfully changed"
    And I check the "Status" field row has value "Rejected"

    And I click the "Change" link for the "DVSA Area Office" field row
    And I select "08" in the "Area office" field
    And I press the "Change area office" button
    And The page contains "Area office has been successfully changed"
    And I check the "DVSA Area Office" field row has value "08"


  Scenario: AO1 user performs AE search, then views and records an AE event
    Given I load "AE_NOT_REJECTED" as {aeReference}, {aeName}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button
    And I click the "Event history" link
    And The page title contains "Events history"
    And I click the "Record an event" link

    And The page title contains "Record an event"
    And I select "Memo" in the field with id "eventType"
    And I enter "30" in the "Day" field
    And I enter "06" in the "Month" field
    And I enter "2017" in the "Year" field
    And I press the "Continue" button

    And The page title contains "Record an event outcome"
    And I select "Closed" in the field with id "outcomeCode"
    And I enter "Test event" in the field with id "notes"
    And I press the "Continue" button

    And The page title contains "Event summary"
    And I press the "Record event" button

    Then The page title contains "Events history"
    And The page contains "A new event has been recorded."


  Scenario: AO1 user performs AE search, then assigns a role
    Given I load uniquely "AE_WITH_NO_AEDM" as {aeReference}, {aeName}, {unassignedTesterUsername}, {unassignedTesterName}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button
    And I click the "Assign a role" link
    And I enter {unassignedTesterUsername} in the field with id "userSearchBox"
    And I press the "Search" button
    And I click the "Authorised Examiner Designated Manager" radio button in fieldset "Select a role"
    And I press the "Choose role" button
    And I press the "Confirm" button
    Then The page contains "A role notification has been sent to {unassignedTesterName} '{unassignedTesterUsername}'."


  Scenario: AO1 user performs AE search, then removes a role
    Given I load uniquely "AE_WITH_AEDM" as {aeReference}, {aeName}, {aedmUsername}, {aedmName}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button
    And I click the remove AE role link for {aedmName}
    And I press the "Remove role" button
    Then The page contains "You have removed the role of Authorised examiner designated manager from {aedmName}"


  Scenario: AO1 user performs AE search, then adds a site association
    Given I load uniquely "AE_WITH_UNASSIGNED_SITE" as {aeReference}, {aeName}, {siteReference}, {siteName}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button
    And I click the "Add a site association" link
    And I enter {siteReference} in the "Site ID" field
    And I press the "Associate this site" button
    Then I check for site association for {siteReference}, {siteName}

