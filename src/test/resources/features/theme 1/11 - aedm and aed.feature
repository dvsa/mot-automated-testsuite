@regression
Feature: 11 - AEDM and AED

  @smoke
  Scenario: AEDM Buys slots
    Given I login with 2FA using "AEDM_USER" as {AEDM}, {ORGANISATION}
    And I click the organisation link {ORGANISATION}
    And I order 25 slots
    And I enter the card details "4462030000000000", "12/18", "654"
    And I enter the card holders name as "Jimi Hendrix"
    And I make the payment for card "4462030000000000"
    And I check that 25 slots were bought successfully

  Scenario: AEDM assigns AED role
    Given I login with 2FA using "AEDM_AND_NON_AED_USER" as {aedmUsername}, {otherUsername}, {organisationName}, {otherName}
    And I click the {organisationName} link
    And The page title contains "Authorised Examiner"

    When I click the "Assign a role" link
    And I enter {otherUsername} in the field with id "userSearchBox"
    And I press the "Search" button

    And The page contains "Choose a role"
    And I click the "Authorised Examiner Delegate" radio button
    And I press the "Choose role" button

    And I check the role summary has a new role of "Authorised Examiner Delegate"
    And I press the "Confirm" button

    Then I check the organisation role assignment confirmation message for {otherUsername}, {otherName}
    And I check there is pending "Authorised examiner delegate" role listed for {otherName}

  Scenario: AED assigns tester to role
    Given I login with 2FA using "AED_AND_TESTER" as {aedmUsername}, {organisation}, {site}, {tester}, {testerName}
    And I click the {organisation} link
    And I click the {site} link
    And The page title contains "Vehicle Testing Station"

    And I click the "Assign a role" link
    And The page title contains "Assign a role"
    And I enter {tester} in the field with id "userSearchBox"
    And I press the "Search" button

    And The page title contains "Choose a role"
    And I click the "Tester" radio button
    And I press the "Choose role" button

    And The page title contains "Review role"
    And I check the role summary has a new role of "Tester"
    And I press the "Assign role" button

    Then The page title contains "Vehicle Testing Station"
    And I check there is a role assignment confirmation message for {tester}, {testerName}
    And I check there is pending "Tester" role listed for {testerName}

  Scenario: Aedm checks todays test log at vts
    Given I load "AEDM_AND_TESTER_AT_SITE" as {aedm}, {aeName}, {siteName}, {tester}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {odometer}
    And I login with 2FA as {tester}

    And I start an MOT test for {reg}, {vin}
    And The page title contains "Your home"
    And I click the "Enter test results" link
    And I enter an odometer reading in miles of {odometer} plus 5000
    And I enter decelerometer results of service brake 51 and parking brake 16
    And I press the "Review test" button
    And The page title contains "MOT test summary"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click the "Sign out" link

    When I login with 2FA as {aedm}
    And I click the {aeName} link
    And I click the {siteName} link
    And I click the "Test logs" link
    And I click the "Today" link

    Then I check the site test log has the recent test {reg}, {tester}


  # Scenario: AED assigns tester role
    # Sign in as a AED (2FA active)
    # Select VTS via the home page
    # Select 'assign a role'
    # Enter valid username (user without tester role) and search
    # Select tester role and confirm
    # Expected results:
    #   - User is directed /vehicle-testing-station/<VTS> page
    #   - Message displayed: You have assigned a role to Evie Rubin Dykes, DYKE8547. They have been sent a notification.
    #   - User nominated is listed as Tester with Pending role status
    # Checks:
    #   - User nominated cannot be assigned the nominated role for the VTS
    #   - User search for a user who is not a tester

  # Scenario: AEDM can view today's MOT Test Log information for associated VTS
    # Sign in as a AEDM (2FA active)
    # Select Your Profile
    # Select Test Logs
    # Select 'Today' date link (depending on test data)
    # Expected results: Test results displayed within Test Log table
    # Checks:
    #   - AEDM associated VTS's must have conducted and completed  MOT's
    #   - Could be too brittle as needs specific MOT conducted data (unless its created beforehand for this site)

  # Scenario: AEDM can view TQI - VTS List with RAG status
    # Pre-req: Pre req: RAG available / calculated...?
    # Sign in as a AEDM (2FA active)
    # Select AE via the home page
    # Select Test Quality Information
    # Select Month
    # Expected results: RAG status per VTS displayed

  # Scenario: AEDM can view TQI for each VTS
    # Pre req: VTS must have MOT's completed for selected month
    # Pre req: National averages calculated....""National stats populate once a month on its own there is a case when automatic job fails to populate them then first user that clicks every single month starts the job""
    # "ign in as a AEDM (2FA active)
    # Select VTS via the home page
    # Select Test Quality Information
    # Select Month
    # Expected results: VTS TQI populated

  # Scenario: AED can view TQI for each VTS tester
    # Sign in as a AED (2FA active)
    # Select VTS via the home page
    # Select Test Quality Information
    # Select Month
    # Expected results: VTS TQI populated per tester