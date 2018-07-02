@moth
Feature: 22a - Inverted/Statutory appeal should show blank location

  Scenario: A MOTH user searches for a vehicle with inverted appeal, test location should be blank
    Given I browse to /
#    Need to replace with details from DB
    And I enter "TF74PMM" in the registration field

    When I press the "Continue" button
#    Replace these checks based on values from the DB
    Then The page contains "TF74PMM"
    And The page contains "PIAGGIO ZIP 50 2T"
    And The page contains "Pass"
    And The page contains "7653 5249 9655"
    And The page does not contain "NAME OF DVSA INSPECTION SITE"
#    Add extra checks based on details extracted from the DB
    And The page title contains "Check MOT history"

  Scenario: A MOTH user searches for a vehicle with statutory appeal, test location should be blank
    Given I browse to /
#    Need to replace with details from DB
    And I enter "TF74PMM" in the registration field

    When I press the "Continue" button
#    Replace these checks based on values from the DB
    Then The page contains "TF74PMM"
    And The page contains "PIAGGIO ZIP 50 2T"
    And The page contains "Pass"
    And The page contains "8481 0956 5039"
    And The page does not contain "NAME OF DVSA INSPECTION SITE"
#    Add extra checks based on details extracted from the DB
    And The page title contains "Check MOT history"