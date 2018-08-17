@mothhgv @mothint
Feature: 53 - Check whether HGV with test after eu go live date looks like pre eu test and handles PG9 tests correctly

  Scenario: A MOTH user searches for a HGV that has no Annual Test history and the vehicle is more than a year old
    Given I browse to /
    And I enter "PROHIBH" in the registration field
    When I press the "Continue" button

    Then The page contains "RENAULT"
    And The page contains "SCANIA"
    And The page contains "The annual test changed on 20 May 2018"
    And The page does not contain "Defects are now categorised according to their severity – dangerous, major, and minor."
    And The page contains "Find out more"

    And I click the accordion section with the id "mot-history-description"
    And The page contains "7 July 2018"
    And The page contains "Brake Systems and Components - Prohibition"
    And The page contains "Brake Systems and Components - Failure"

    And The page contains "PG9PARTRE"
    And The page contains "PG9FULL"
    And The page does not contain "Condition of Body - PG9 FULL INSPECTION & FEE - Fail"
    And The page does not contain "Condition of Body - PG9 PART PAID RETEST - Prohibition"