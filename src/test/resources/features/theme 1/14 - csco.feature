@regression
Feature: 14 - CSCO

  Scenario: CSCO user performs AE search, then views AE details
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "AUTHORISED_EXAMINER" as {aeNumber}, {aeName}, {slotUsage}
    And I click the "AE information" link
    When I enter {aeNumber} in the "AE Number" field
    And I press the "Search" button
    And I check the "Authorised Examiner ID" field row has value {aeNumber}


  Scenario: CSCO user performs Site Information search by town, then views site details
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "SITE_LOCATION_INFORMATION" as {sid}, {sName}, {sNumber}, {aTown}, {aPostcode}
    And I click the "Site information" link
    When I perform a site search for "town" as {aTown} and select {sNumber}
    And I check the "Address" field row has value {aTown}


  Scenario: CSCO user performs Site Information search by postcode, then views site details
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "SITE_LOCATION_INFORMATION" as {sid}, {sName}, {sNumber}, {aTown}, {aPostcode}
    And I click the "Site information" link
    When I perform a site search for "Postcode (full or part)" as {aPostcode} and select {sNumber}
    And I check the "Address" field row has value {aPostcode}


  Scenario: CSCO user performs a user search, then edits the email address
    Given I load immediately "TESTER_GROUP_B_AND_NOT_A" as {testerUsername}, {testerName}
    And I generate a unique email address as {email}
    And I login without 2FA using "CSCO_USER" as {cscouser}
    And I click the "User search" link
    And The page title contains "User search"
    And I enter {testerUsername} in the "Username" field
    And I press the "Search" button
    And I click the first {testerName} link
    And The page title contains "User profile"
    When I click the "Change" link for the "Email" field row
    And I enter {email} in the "Email address" field
    And I enter {email} in the "Re-type your email address" field
    And I press the "Change email address" button
    And I check the "Email" field row has value {email}


  #Limits on this Scenario because it sends off an email to reset the account
  #Scenario: User Search - Reclaim Account

  Scenario: CSCO user performs certificate search by vrm/registration, and is able to print duplicate certificate
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I search for certificates with reg {reg}
    And I click the first "View certificate" link
    And I check there is a "Print certificate" link


  Scenario: CSCO user performs certificate search by vin, and is able to print duplicate certificate
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I search for certificates with vin {vin}
    And I click the first "View certificate" link
    And I check there is a "Print certificate" link


  Scenario: CSCO user performs MOT test search by site, and is able to view test certificates
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "SITE" as {siteNumber}, {siteName}
    And I click the "MOT tests" link
    When I search for an mot by "Site (recent tests)" with {siteNumber}
    And I click the first "View" link
    Then The page contains "MOT test summary"
    And I check there is a "Print certificate" link


  Scenario: CSCO user performs MOT test search by tester, and is able to view test certificates
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "TESTER_WITH_2_MONTH_HISTORY" as {tester}
    And I click the "MOT tests" link
    When I search for an mot by "Tester (by date range)" with {tester} from 2 months ago
    And I click the first "View" link
    Then The page contains "MOT test summary"
    And I check there is a "Print certificate" link


  Scenario: CSCO user performs MOT test search by vin, and view the test details
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I click the "MOT tests" link
    When I search for an mot by "VIN/Chassis (comparison available)" with {vin}
    And I click the first "View" link
    Then The page contains "MOT test summary"
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin}


  Scenario: CSCO user performs MOT test search by vrm/registration, and view the test details
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I click the "MOT tests" link
    When I search for an mot by "Registration (comparison available)" with {reg}
    And I click the first "View" link
    Then The page contains "MOT test summary"
    And I check the vehicle summary section of the test summary has "Registration number" of {reg}