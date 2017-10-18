@regression
Feature: 09b - AEDM and AED

  Scenario Outline: AEDM can view TQI for site with <status> status (via organisation)
    Given I login with 2FA using "<dataSource>" as {aedm}, {organisationName}, {siteName}
    And I click the first {organisationName} link
    When I click the "Service reports" link
    And I click the {siteName} site link with status "<status>" on the service reports
    Then The page contains "This information will help you manage the quality of testing at your site."
    And I check there is a "Download the group B report as a CSV (spreadsheet) file" link

  Examples:
  |status|dataSource         |
  |Green |AEDM_AND_GREEN_SITE|
  |Amber |AEDM_AND_AMBER_SITE|
  |Red   |AEDM_AND_RED_SITE  |


  Scenario: AED can view TQI for site (via VTS)
    Given I login with 2FA using "AED_AND_GROUP_A_SITE" as {aed}, {siteName}, {siteNumber}
    And I click the first "({siteNumber}) {siteName}" link
    When I click the "Test quality information" link
    Then The page contains "This information will help you manage the quality of testing at your site."
    And I check there is a "Download the group A report as a CSV (spreadsheet) file" link


  Scenario: AEDM can view TQI for each VTS tester
    Given I login with 2FA using "AEDM_AND_TESTER_AT_SITE" as {aedm}, {aeName}, {siteName}, {siteNumber}, {testerUsername}, {testerName}
    And I click the first "({siteNumber}) {siteName}" link
    And I click the "Test quality information" link
    When I click the TQI link for tester {testerUsername}
    Then The page contains "{testerName}"
