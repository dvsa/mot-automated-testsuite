@regression
Feature: 14a - CSCO

  Scenario: CSCO user performs AE search, then views AE details
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "AE_NOT_REJECTED" as {aeNumber}, {aeName}
    And I click the "AE information" link
    When I enter {aeNumber} in the "AE Number" field
    And I press the "Search" button
    And I check the "Authorised Examiner ID" field row has value {aeNumber}


  Scenario: CSCO user performs Site Information search by town, then views site details
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "SITE_LOCATION_INFORMATION" as {sNumber}, {sid}, {sName}, {aTown}, {aPostcode}
    And I click the "Site information" link
    When I perform a site search for "town" as {aTown} and select {sNumber}
    And I check the "Address" field row has value {aTown}


  Scenario: CSCO user performs Site Information search by postcode, then views site details
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "SITE_LOCATION_INFORMATION" as {sNumber}, {sid}, {sName}, {aTown}, {aPostcode}
    And I click the "Site information" link
    When I perform a site search for "Postcode (full or part)" as {aPostcode} and select {sNumber}
    And I check the "Address" field row has value {aPostcode}


  @browserstack
  Scenario: CSCO user performs a user search, then edits the email address
    Given I load uniquely "TESTER_GROUP_B_AND_NOT_A" as {testerUsername}, {testerName}
    And I generate a unique email address as {email}
    And I login without 2FA using "CSCO_USER" as {cscouser}
    And I click the "User search" link
    And The page title contains "User search"
    And I enter {testerUsername} in the "Username" field
    And I press the "Search" button
    And I press the first {testerName} button
    And The page title contains "User profile"
    When I click the "Change" link for the "Email" field row
    And I enter {email} in the "Email address" field
    And I enter {email} in the "Re-type your email address" field
    And I press the "Change email address" button
    And I check the "Email" field row has value {email}


  #Limits on this Scenario because it sends off an email to reset the account
  #Scenario: User Search - Reclaim Account
  @browserstack
  Scenario: CSCO user performs certificate search by vrm/registration, and is able to print duplicate certificate
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I search for certificates with reg {reg}
    And I click the first "View certificate" link
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |
