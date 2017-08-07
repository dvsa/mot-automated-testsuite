@regression
Feature: 12 - Site Manager and Site Admin

  Scenario: Site Manager assigns Site Admin role
    Given I login with 2FA using "SITE_MGR_AND_OTHER_TESTER" as {managerUsername1}, {sitename1}, {sitenumber1}, {otherUsername1}, {otherName1}
    And I click on the {sitename1}, {sitenumber1} site
    And The page title contains "Vehicle Testing Station"

    When I click the "Assign a role" link
    And The page title contains "Assign a role"
    And I enter {otherUsername1} in the field with id "userSearchBox"
    And I press the "Search" button

    And The page title contains "Choose a role"
    And I click the "Site admin" radio button
    And I press the "Choose role" button

    And The page title contains "Review role"
    And I check the role summary has a new role of "Site admin"
    And I press the "Assign role" button

    Then The page title contains "Vehicle Testing Station"
    And I check there is a role assignment confirmation message for {otherUsername1}, {otherName1}
    And I check there is pending "Site admin" role listed for {otherName1}


  Scenario: Site Admin assigns Tester role
    Given I login with 2FA using "SITE_MGR_AND_OTHER_TESTER" as {managerUsername1}, {sitename1}, {sitenumber1}, {otherUsername1}, {otherName1}
    And I click on the {sitename1}, {sitenumber1} site
    And The page title contains "Vehicle Testing Station"

    When I click the "Assign a role" link
    And The page title contains "Assign a role"
    And I enter {otherUsername1} in the field with id "userSearchBox"
    And I press the "Search" button

    And The page title contains "Choose a role"
    And I click the "Tester" radio button
    And I press the "Choose role" button

    And The page title contains "Review role"
    And I check the role summary has a new role of "Tester"
    And I press the "Assign role" button

    Then The page title contains "Vehicle Testing Station"
    And I check there is a role assignment confirmation message for {otherUsername1}, {otherName1}
    And I check there is pending "Tester" role listed for {otherName1}


  Scenario: Site Manager views and aborts an active MOT
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}

    And I login with 2FA using "SITE_MGR_AND_TESTER_CLASS_4" as {testerUsername1}, {managerUsername1}, {sitename1}, {sitenumber1}
    And I start an MOT test for {registration1}, {vin1}
    And The page title contains "Your home"
    And I click the "Sign out" link

    When I login with 2FA as {managerUsername1}
    And I click on the {sitename1}, {sitenumber1} site
    And The page title contains "Vehicle Testing Station"
    And I click the {registration1} link
    And The page title contains "Vehicle Testing Station"
    And I click the "Abort MOT Test" link
    And The page title contains "Abort MOT test"
    And I click the "Vehicle registered in error" radio button
    And I press the "Abort MOT test" button

    Then The page contains "You have successfully aborted MOT test"
    And The page contains "with a reason of Vehicle registered in error."


  # Scenario: Site Admin/Mgr updates VTS details
    # Pre-req: Test can be repeated for 'Change test hours'
    # Sign in as a Site Manager/Admin (2FA active)
    # Select VTS via the home page
    # Select 'Change default test settings'
    # Select service and brake options and confirm
    # Expected results: Selected service and brake options displayed

  # Scenario: Site Manager can view TQI statistics for testers associated with VTS
    # Pre-req: VTS must have MOT's completed for selected month
    # Sign in as a Site Manager (2FA active)
    # Select VTS via the home page
    # Select Test Quality Information
    # Select Month
    # Expected results: VTS TQI populated per tester