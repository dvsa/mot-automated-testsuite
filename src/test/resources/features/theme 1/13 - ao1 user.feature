@regression
Feature: 13 - A01 user

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
    Given I load immediately "AE_WITH_NO_AEDM" as {aeReference}, {aeName}, {unassignedTesterUsername}, {unassignedTesterName}
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
    Then The page contains "A role notification has been sent to {unassignedTesterName} '{unassignedTesterName}'."


  Scenario: AO1 user performs AE search, then removes a role
    Given I load immediately "AE_WITH_AEDM" as {aeReference}, {aeName}, {aedmUsername}, {aedmName}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button
    And I click the remove AE role link for {aedmName}
    And I press the "Remove role" button
    Then The page contains "You have removed the role of Authorised examiner designated manager from {aedmName}"


  Scenario: AO1 user performs AE search, then adds a site association
    Given I load immediately "AE_WITH_UNASSIGNED_SITE" as {aeReference}, {aeName}, {siteReference}, {siteName}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button
    And I click the "Add a site association" link
    And I enter {siteReference} in the "Site ID" field
    And I press the "Associate this site" button
    Then I check for site association for {siteReference}, {siteName}


  Scenario: AO1 user performs AE search, then remove a site association
    Given I load immediately "AE_WITH_ASSIGNED_SITE" as {aeReference}, {aeName}, {siteReference}, {siteName}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button
    And I click the remove site association link for {siteName}
    And I select "Surrendered" in the "Choose a status" field
    And I press the "Remove association" button
    Then I check there is no site association for {siteReference}, {siteName}


  Scenario: AO1 user performs site search, then changes site details
    Given I load "SITE" as {siteName}, {siteReference}
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
    Given I load immediately "SITE_MGR_AND_OTHER_TESTER" as {mgrUsername}, {siteName}, {siteReference}, {unassignedTesterUsername}, {unassignedTesterName}
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
    Given I load immediately "SITE_MGR_AND_TESTER_CLASS_4" as {testerUsername}, {testerName}, {mgrUsername}, {siteName}, {siteReference}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I search for site by reference {siteReference}
    And I click the remove site role link for {testerName}, {testerUsername}
    And The page title contains "Remove a role"
    And I press the "Remove role" button
    And The page title contains "Vehicle Testing Station"
    And The page contains "You have removed the role of Tester from {testerName}"


  Scenario: AO1 user performs site search, then views and records a site event
    Given I load "SITE" as {siteName}, {siteReference}
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


  Scenario: AO1 user performs user search, then changes tester group qualification status
    Given I load immediately "TESTER_GROUP_B_AND_NOT_A" as {testerUsername}, {testerName}
    And I login without 2FA using "AO1_USER" as {ao1User}
    And I click the "User search" link
    And The page title contains "User search"
    And I enter {testerUsername} in the "Username" field
    And I press the "Search" button
    And I click the first {testerName} link
    And The page title contains "User profile"
    When I click the "Change Group A qualification status" link
    And I click the "Qualified" radio button
    And I press the "Change status" button
    And I press the "Confirm qualification status" button
    Then The page contains "Group A tester qualification status has been changed to Qualified"

    And I click the "Change Group B qualification status" link
    And I click the "Suspended" radio button
    And I press the "Change status" button
    And I press the "Confirm qualification status" button
    And The page contains "Group B tester qualification status has been changed to Suspended"


  Scenario: AO1 user performs user search, then edits driver licence number
    Given I load immediately "TESTER_WITH_LICENCE" as {testerUsername}, {testerName}, {licenceNumber}
    And I choose a new driving licence number for {licenceNumber} as {newLicenceNumber}
    And I login without 2FA using "AO1_USER" as {ao1User}
    And I click the "User search" link
    And The page title contains "User search"
    And I enter {testerUsername} in the "Username" field
    And I press the "Search" button
    And I click the first {testerName} link
    And The page title contains "User profile"

    When I click the "Change" link for the "Driving licence" field row
    And I enter {newLicenceNumber} in the "Driving licence number" field
    And I press the "Review driving licence" button
    And I press the "Change driving licence" button
    Then The page title contains "User profile"
    And The page contains "Driving licence has been changed successfully."
    And I check the "Driving licence" field row has value {newLicenceNumber}


  Scenario: AO1 user performs user search, and removes then adds driver licence number
    Given I load immediately "TESTER_WITH_LICENCE" as {testerUsername}, {testerName}, {licenceNumber}
    And I login without 2FA using "AO1_USER" as {ao1User}
    And I click the "User search" link
    And The page title contains "User search"
    And I enter {testerUsername} in the "Username" field
    And I press the "Search" button
    And I click the first {testerName} link
    And The page title contains "User profile"

    When I click the "Change" link for the "Driving licence" field row
    And I click the "Remove driving licence" link
    And I press the "Remove driving licence" button
    Then The page title contains "User profile"
    And The page contains "Driving licence has been successfully removed."
    And I check the "Driving licence" field row has value "None recorded"

    And I click the "Change" link for the "Driving licence" field row
    And I enter {licenceNumber} in the "Driving licence number" field
    And I click the "Great Britain (England, Scotland and Wales)" radio button
    And I press the "Review driving licence" button
    And I press the "Change driving licence" button
    And The page title contains "User profile"
    And The page contains "Driving licence has been changed successfully."
    And I check the "Driving licence" field row has value {licenceNumber}


  Scenario: AO1 user performs user search, then views and records a person event
    Given I load "TESTER_WITH_LICENCE" as {testerUsername}, {testerName}, {licenceNumber}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I click the "User search" link
    And The page title contains "User search"
    And I enter {testerUsername} in the "Username" field
    And I press the "Search" button
    And I click the first {testerName} link
    And The page title contains "User profile"

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


  Scenario: AO1 user performs vehicle search by vin, then views vehicle details
    Given I load "VEHICLE_CLASS_4" as {registration}, {vin}, {mileage}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I click the "Vehicle information" link
    And I select "VIN/Chassis" in the field with id "type"
    And I enter {vin} in the field with id "vehicle-search"
    And I click the "search" icon
    Then The page title contains "Vehicle Details"
    And I check the "Registration mark" field row has value {registration}
    And I check the "VIN" field row has value {vin}


  Scenario: AO1 user performs MOT tests search by site id, then views tests
    Given I load "SITE" as {siteName}, {siteReference}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I click the "MOT tests" link
    And I select "Site (recent tests)" in the field with id "type"
    And I enter {siteReference} in the field with id "vts-search"
    And I click the "search" icon
    Then The page title contains "Vehicle Testing Station Search Results"
    And I check the table with heading "Test date/time" has at least 1 rows


  Scenario: AO1 user performs MOT tests search by vin, then views tests
    Given I load "VEHICLE_CLASS_4" as {registration}, {vin}, {mileage}
    And I login without 2FA using "AO1_USER" as {ao1User}
    When I click the "MOT tests" link
    And I select "VIN/Chassis (comparison available)" in the field with id "type"
    And I enter {vin} in the field with id "vts-search"
    And I click the "search" icon
    Then The page title contains "Mot Test Search Results"
    And I check the table with heading "Test date/time" has at least 1 rows


  Scenario: AO1 user creates a new AE
    Given I login without 2FA using "AO1_USER" as {ao1User}

    When I click the "Create an Authorised Examiner" link
    And I enter "Example MOT Testers" in the "Business name" field
    And I enter "Example MOT Testers Ltd" in the "Trading name" field
    And I select "Sole trader" in the "Business type" field
    And I enter "10 Example Street" in the "Address line" field
    And I enter "Example Town" in the "Town or city" field
    And I enter "AB1 2CD" in the "Postcode" field
    And I enter "01234567890" in the "Phone number" field
    And I enter "no_one@example.com" in the "Email address" field
    And I enter "no_one@example.com" in the "Re-type email address" field
    And I click the "Yes" radio button in fieldset "Are they the same as the business contact details?"
    And I select "01" in the "DVSA Area Office" field
    And I press the "Continue" button
    And I press the "Create Authorised Examiner" button

    Then The page title contains "Authorised Examiner"
    And I check the "Name" field row has value "Example MOT Testers"
    And I check the "Trading name" field row has value "Example MOT Testers Ltd"
    And I check the "Business type" field row has value "Sole trader"
    And I check the "DVSA Area Office" field row has value "01"
    And I check the "Status" field row has value "Applied"


  Scenario: AO1 user creates a new VTS
    Given I login without 2FA using "AO1_USER" as {ao1User}

    When I click the "Create a site" link
    And I enter "Example Site" in the "Site name" field
    And I select "Vehicle Testing Station" in the "Site type" field
    And I enter "10 Example Street" in the "Address line" field
    And I enter "Example Town" in the "Town or city" field
    And I enter "AB1 2CD" in the "Postcode" field
    And I click the "England" radio button in fieldset "Country"
    And I enter "no_one@example.com" in the "Email address" field
    And I enter "no_one@example.com" in the "Re-type email address" field
    And I enter "01234567890" in the "Telephone number" field
    And I select "2" in the "How many two-person test lanes (TPTL) does the site have?" field
    And I select "3" in the "How many one-person test lanes (OPTL) does the site have?" field
    And I click the "Class 1" checkbox
    And I click the "Class 2" checkbox
    And I click the "Class 3" checkbox
    And I click the "Class 4" checkbox
    And I press the "Continue" button
    And I press the "Create site" button

    Then The page title contains "Vehicle Testing Station"
    And I check the "Name" field row has value "Example Site"
    And I check the "Classes" field row has value "1,2,3,4"
    And I check the "Type" field row has value "Vehicle Testing Station"
    And I check the "Status" field row has value "Approved"
    And I check the "Two Person Test Lane (TPTL)" field row has value "2"
    And I check the "One Person Test Lane (OPTL)" field row has value "3"
