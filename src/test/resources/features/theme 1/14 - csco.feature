@regression
Feature: 14 - CSCO

  Scenario: AE Search
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "AUTHORISED_EXAMINER" as {aeNumber}, {aeName}, {slotUsage}
    And I click the "AE information" link
    When I enter {aeNumber} in the "AE Number" field
    And I press the "Search" button
    And I check the Authorised Examiner Business details AE ID is {aeNumber}


  Scenario: Site Information search - Path 1
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "SITE_LOCATION_INFORMATION" as {sid}, {sName}, {sNumber}, {aTown}, {aPostcode}
    And I click the "Site information" link
    When I enter {aTown} in the "town" field
    And I press the "Search" button
    And I check the Site city/town details city is {aTown}


  Scenario: Site Information search - Path 2
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "SITE_LOCATION_INFORMATION" as {sid}, {sName}, {sNumber}, {aTown}, {aPostcode}
    And I click the "Site information" link
    When I enter {aPostcode} in the "Postcode (full or part)" field
    Then I press the "Search" button
    And I check the Site postcode details postcode is {aPostcode}

    #Scenario: User Search - Update user's email
    #Selects user search
    #Enter First name and Last name
    #Select search
    #Select user
    #Find Email
    #Select change
    #Enter new Email address
    #Enter Re-type email address
    #Select Change email address
    #Ensure the email address has updated

  #Limits on this Scenario because it sends off an email to reset the account
  #Scenario: User Search - Reclaim Account

  #Scenario: Issue Duplicate
    #Select replacement/duplicate certificate
    #Enter registration mark
    #Select view certificate
    #Select print certificate
    #Ensure the user can be reprint the certificate

  #Scenario: MOT Test - Site
    #Select MOT tests
    #Select Site (recent tests)
    #Enter Site number/ID
    #Ensure that a previous test results can be viewed
  #Scenario: MOT Test - Site
  #  Given I login without 2FA using "CSCO_USER" as {cscouser}
  #  And I load "SITE" as {siteName}, {siteNumber}
  #  And I click the "MOT tests" link
  #  When I search for an mot by "Site (by recent tests)" with {siteNumber}
  #  And I click the first "View" link
  #  Then The page contains "MOT test summary"
  #  And I check there is a "Print certificate" link

  #Scenario: MOT Test - Tester
    #Select MOT tests
    #Select tester (recent tests)
    #Enter date range
    #Enter username
    #Select Search
    #Ensure that summary can be viewed

  Scenario: MOT Test - VIN
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I click the "MOT tests" link
    When I search for an mot by "VIN/Chassis (comparison available)" with {vin}
    And I click the first "View" link
    Then The page contains "MOT test summary"
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin}

  Scenario: MOT Test - VRM
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I click the "MOT tests" link
    When I search for an mot by "Registration (comparison available)" with {reg}
    And I click the first "View" link
    Then The page contains "MOT test summary"
    And I check the vehicle summary section of the test summary has "Registration number" of {reg}