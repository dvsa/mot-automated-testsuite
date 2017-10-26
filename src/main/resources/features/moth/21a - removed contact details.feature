@moth
Feature: 21a - Removed contact details, garage details still shown

  Scenario: A MOTH user searches for a vehicle where the contact details are removed
    Given I browse to /
#    Need to replace with details from DB
    And I enter "CO32HSK" in the registration field

    When I press the "Continue" button
#    Replace these checks based on values from the DB
    Then The page contains "CO32HSK"
    And The page contains "IVECO DAILY 35C12 MWB"
    And The page contains "Pass"
    And The page contains "1261 4466 2211"
    And The page contains "MILLOM MOTOR ENGINEERS"
#    Add extra checks based on details extracted from the DB
    And The page title contains "Check MOT history"