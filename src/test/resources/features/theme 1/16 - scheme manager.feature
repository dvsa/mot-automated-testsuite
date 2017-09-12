@regression
Feature: 16 - Scheme manager

  Scenario: Scheme manager performs AE search, then views AE details
    Given I load "AE_NOT_REJECTED" as {aeReference}, {aeName}
    And I login without 2FA using "SCHEME_MANAGER_USER" as {schemeManager}

    When I click the "AE information" link
    And I enter {aeReference} in the "AE Number" field
    And I press the "Search" button

    Then The page title contains "Authorised Examiner"
    And I check the "Name" field row has value {aeName}
    And I check the "Authorised Examiner ID" field row has value {aeReference}


  Scenario: Scheme manager performs user search, then adds AO1 role
    Given I load immediately "VE_AND_NOT_AO1_USER" as {veUsername}, {veName}
    And I login without 2FA using "SCHEME_MANAGER_USER" as {schemeManager}
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


  # Scenario: Vehicle Information Search
    # Select Vehicle Information
    # Select Registration BN47ZXL
    # Check Name PIAGGIO, MP3 SPORT TOURING LT 500


  # Scenario: Select Vehicle Information
    # Select VIN/Chassis  LXZNBJAAAAA635357
    # Check Name PIAGGIO, MP3 SPORT TOURING LT 500