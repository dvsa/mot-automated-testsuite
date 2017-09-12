@regression
Feature: 16 - Scheme manager

  Scenario: Scheme Manager performs AE search, then views AE details
    Given I load "AE_NOT_REJECTED" as {aeReference}, {aeName}
    And I login without 2FA using "SCHEME_MANAGER_USER" as {schemeManager}

    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button

    Then The page title contains "Authorised Examiner"
    And I check the "Name" field row has value {aeName}
    And I check the "Authorised Examiner ID" field row has value {aeReference}

  # Scenario: User Search
    # Select User Search
    # Username TERR4521
    # Select Reed Eleonore Terrall
    # Check Name Mr Reed Eleonore Terrall
    # Select Manage Roles
    # Add Role Area Office 1
    # Add Role
    # Check Current Roles Area Office 1

  # Scenario: Vehicle Information Search
    # Select Vehicle Information
    # Select Registration BN47ZXL
    # Check Name PIAGGIO, MP3 SPORT TOURING LT 500

  # Scenario: Select Vehicle Information
    # Select VIN/Chassis  LXZNBJAAAAA635357
    # Check Name PIAGGIO, MP3 SPORT TOURING LT 500