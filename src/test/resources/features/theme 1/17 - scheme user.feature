@regression
Feature: 17 - Scheme user

  # Scenario: Create Special Notice
    # Select Special Notices
    # Create Special Notice
    # Preview
    # Publish Special Notice

  # Scenario: Edit Special Notice
    # After special notice is published from above
    # Select Special Notices
    # Click special notice published in Create Special Notice
    # Click Edit
    # Preview
    # Publish Special Notice

  # Scenario: Retract Special Notice
    # Select Special Notices
    # Click Special Notice already published
    # Click Remove


  Scenario: Scheme user performs AE search, then views AE details
    Given I load "AE_NOT_REJECTED" as {aeReference}, {aeName}
    And I login without 2FA using "SCHEME_USER" as {schemeUser}

    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button

    Then The page title contains "Authorised Examiner"
    And I check the "Name" field row has value {aeName}
    And I check the "Authorised Examiner ID" field row has value {aeReference}


  Scenario: Scheme user performs user search, then adds AO1 role
    Given I load immediately "VE_AND_NOT_AO1_USER" as {veUsername}, {veName}
    And I login without 2FA using "SCHEME_USER" as {schemeUser}
    And I click the "User search" link
    And The page title contains "User search"
    And I enter {veUsername} in the "Username" field
    And I press the "Search" button
    And I click the first {veName} link
    And The page title contains "User profile"
    And I check the "Name" field row has value {veName}
    And I check the "User ID" field row has value {veUsername}

    When I click the "Manage roles" link
    And I click the "Add role" link for the "Area office 1" field row
    And I press the "Add role" button
    Then The page contains "Area office 1 role has been added"
