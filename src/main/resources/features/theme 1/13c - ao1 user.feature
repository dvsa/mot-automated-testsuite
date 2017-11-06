@regression
Feature: 13c - A01 user

  Scenario: AO1 user performs user search, then changes tester group qualification status
    Given I load uniquely "TESTER_GROUP_B_AND_NOT_A" as {testerUsername}, {testerName}
    And I login without 2FA using "AO1_USER" as {ao1User}
    And I click the "User search" link
    And The page title contains "User search"
    And I enter {testerUsername} in the "Username" field
    And I press the "Search" button
    And I press the first {testerName} button
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
    Given I load uniquely "TESTER_WITH_LICENCE" as {testerUsername}, {testerName}, {licenceNumber}
    And I choose a new driving licence number for {licenceNumber} as {newLicenceNumber}
    And I login without 2FA using "AO1_USER" as {ao1User}
    And I click the "User search" link
    And The page title contains "User search"
    And I enter {testerUsername} in the "Username" field
    And I press the "Search" button
    And I press the first {testerName} button
    And The page title contains "User profile"

    When I click the "Change" link for the "Driving licence" field row
    And I enter {newLicenceNumber} in the "Driving licence number" field
    And I press the "Review driving licence" button
    And I press the "Change driving licence" button
    Then The page title contains "User profile"
    And The page contains "Driving licence has been changed successfully."
    And I check the "Driving licence" field row has value {newLicenceNumber}


  Scenario: AO1 user performs user search, and removes then adds driver licence number
    Given I load uniquely "TESTER_WITH_LICENCE" as {testerUsername}, {testerName}, {licenceNumber}
    And I login without 2FA using "AO1_USER" as {ao1User}
    And I click the "User search" link
    And The page title contains "User search"
    And I enter {testerUsername} in the "Username" field
    And I press the "Search" button
    And I press the first {testerName} button
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
    And I press the first {testerName} button
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
