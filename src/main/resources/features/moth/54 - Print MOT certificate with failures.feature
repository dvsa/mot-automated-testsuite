@mothprint @mothint
Feature: 54 - Check whether user is notified when he enters an incorrect V5C number

  Scenario: A MOTH user enters an incorrect V5C number and an appropriate error message is displayed
    Given I browse to /
    And I enter "CGSENER" in the registration field
    When I press the "Continue" button

    When I click the accordion section with the id "mot-history-description"
    And I click the first "View test certificate" text
    Then The page contains "Enter latest V5C number"

    Given I enter "00000000000" in the field with id "v5c-print-cert-input"
    When I press the first "Show test certificate" button
    Then The page contains "Check that the V5C number you entered is correct"

    Given I enter " " in the field with id "v5c-print-cert-input"
    When I press the first "Show test certificate" button
    Then The page contains "Enter the V5C number"

    Given I enter "123" in the field with id "v5c-print-cert-input"
    When I press the first "Show test certificate" button
    Then The page contains "The V5C number must be 11 numbers"

    Given I enter "123123123AB" in the field with id "v5c-print-cert-input"
    When I press the first "Show test certificate" button
    Then The page contains "The V5C number can only contain numbers"

    Given I enter "12312312312" in the field with id "v5c-print-cert-input"
    When I press the first "Show test certificate" button
    Then The page contains "View certificate"
    And I check the "Return to MOT test history" button is enabled

    When I click the button with id "cert-download-link"
    And I go to the next tab
    Then The page contains "The certificate is currently unavailable"

