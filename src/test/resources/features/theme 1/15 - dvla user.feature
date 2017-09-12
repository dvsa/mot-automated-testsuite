@regression
Feature: 15 - DVLA user (DO)

  Scenario: DO user performs site search by site id, then views site information
    Given I load "SITE" as {siteName}, {siteReference}
    And I login without 2FA using "DVLA_OPERATIVE_USER" as {doUser}
    When I search for site by reference {siteReference}
    Then I check the "Name" field row has value {siteName}