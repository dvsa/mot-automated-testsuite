@mothpp @mothint
Feature: 09 - Garage with address details

  Scenario: A MOTH user searches for a vehicle, enter correct v5c and test location is correct
    Given I browse to /
    And I load "VEHICLE_REG_GARAGE_ADDR" as {registration}, {testnumber}, {add1}, {add2}, {sitename}, {v5c}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    When I click the accordion section with the id "mot-history-description"
    And The page contains "{testnumber}"
    And I click the first "View test location" text
    Then The page contains "Enter latest V5C number"
    And I enter {v5c} in the field with id "v5c-input"
    And I press the first "Show test location" button

    And The page contains "{sitename}"
    And The page contains "{add1}"
    And The page contains "{add2}"

  Scenario: A MOTH user searches for a vehicle enters incorrect V5C number and an appropriate error message is displayed
    Given I browse to /
    And I load "VEHICLE_REG_GARAGE_ADDR" as {registration}, {testnumber}, {add1}, {add2}, {sitename}, {v5c}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    When I click the accordion section with the id "mot-history-description"
    And I click the first "View test location" text
    Then The page contains "Enter latest V5C number"

    Given I enter "00000000000" in the field with id "v5c-input"
    When I press the first "Show test location" button
    Then The page contains "Check that the V5C number you entered is correct"

    Given I enter " " in the field with id "v5c-input"
    When I press the first "Show test location" button
    Then The page contains "Enter the V5C number"

    Given I enter "123" in the field with id "v5c-input"
    When I press the first "Show test location" button
    Then The page contains "The V5C number must be 11 numbers"

    Given I enter "123456789012" in the field with id "v5c-input"
    When I press the first "Show test location" button
    Then The page contains "The V5C number must be 11 numbers"

    Given I enter "123123123AB" in the field with id "v5c-input"
    When I press the first "Show test location" button
    Then The page contains "The V5C number can only contain numbers"

    And I enter {v5c} in the field with id "v5c-input"
    When I press the first "Show test location" button

    Then The page contains "{sitename}"
    And The page contains "{add1}"
    And The page contains "{add2}"

  Scenario: A MOTH user searches for a vehicle, test location has min length address
    Given I browse to /
    And I load "VEHICLE_REG_GARAGE_MIN_ADDR" as {registration}, {number}, {add1}, {add2}, {v5c}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page contains "Pass"
    And The page contains "{number}"
    When I click the accordion section with the id "mot-history-description"
    And I click the first "View test location" text
    Then The page contains "Enter latest V5C number"
    And I enter {v5c} in the field with id "v5c-input"
    And I press the first "Show test location" button

    And The page contains "{add1}"
    And The page contains "{add2}"

  Scenario: A MOTH user searches for a vehicle, test location has max length address
    Given I browse to /
    And I load "VEHICLE_REG_GARAGE_MAX_ADDR" as {registration}, {number}, {add1}, {add2}, {v5c}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page contains "Pass"
    And The page contains "{number}"
    When I click the accordion section with the id "mot-history-description"
    And I click the first "View test location" text
    Then The page contains "Enter latest V5C number"
    Given I enter {v5c} in the field with id "v5c-input"
    And I press the first "Show test location" button

    And The page contains "{add1}"
    And The page contains "{add2}"