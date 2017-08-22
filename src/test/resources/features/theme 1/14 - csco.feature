@regression
Feature: 14 - CSCO

  Scenario: AE Search
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "AUTHORISED_EXAMINER" as {aeNumber}, {aeName}, {slotUsage}
    And I click the "AE information" link
    When I enter {aeNumber} in the "AE Number" field
    And I press the "Search" button
    And I check the Authorised Examiner Business details AE ID is {aeNumber}

  #Scenario: Site Information search - Path 1
    #Selects site information
    #Enters City or town
    #Selects search
    #Ensure the city matches the City/town entered

  Scenario: Site Information search - Path 1
    Given I login without 2FA using "CSCO_USER" as {cscouser}
    And I load "SITE_LOCATION_INFORMATION" as {sid}, {sName}, {sNumber}, {atown}
    When I search for city/town with {town}
    And The page contains "city/postcode={town}"


   #Scenario: Site Information search - Path 2
    #Selects site information
    #Enters Postcode (full or part)
    #Selects search

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

  #Scenario: MOT Test - Tester
    #Select MOT tests
    #Select tester (recent tests)
    #Enter date range
    #Enter username
    #Select Search
    #Ensure that summary can be viewed

  #Scenario: MOT Test - VRM / VIN
    #Select MOT tests
    #Select VIM/Chassis
    #Enter VIM/Chassis
    #Select Search
    #Ensure that summary can be viewed
