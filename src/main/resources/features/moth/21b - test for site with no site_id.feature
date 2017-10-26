@moth
Feature: 21a - Removed contact details, garage details still shown

  Scenario: A MOTH user searches for a vehicle with inverted appeal pass
    Given I browse to /
#    Need to replace with details from DB
    And I enter "CO32HSK" in the registration field

    When I press the "Continue" button
#    Replace these checks based on values from the DB
    Then The page contains "CO32HSK"
    And The page contains "IVECO DAILY 35C12 MWB"
    And The page contains "Pass"
    And The page contains "7008 5950 0334"
    And The page does not contain "NAME OF SITE WITH NO SITE_ID"
#    Add extra checks based on details extracted from the DB
    And The page title contains "Check MOT history"