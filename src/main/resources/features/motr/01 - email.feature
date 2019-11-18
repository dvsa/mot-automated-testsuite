@motrpp @motrint @motrdemo
Feature: 01 - MOT Reminders vehicle check via email

  Scenario: A MOTr user searches for a vehicle with an valid MOT and signs up via email
    Given I browse to /
    And I load "VEHICLE_REG_MOT_CURRENT" as {registration}, {model}, {date}, {mot_expiry}
    Then The page title contains "What is the vehicle’s registration number? – MOT reminders"
    And The page contains "What is the vehicle’s registration number?"
    And The page contains "Registration number (number plate)"
    And The page contains "For example, CU57ABC"

    And I click the "Looking for HGV trailer reminders?" help link
    And The page contains "You can get HGV trailer annual test reminders on MOT reminders.Enter the trailer ID into the registration number input field to search for HGV trailers."
    And The page contains "For example, C236718 or A787121."
    And I click the "Looking for HGV trailer reminders?" help link

    And The page contains "Cookies"
    And I click the "Cookies" link
    Then The page title contains "Cookie policy – MOT reminders"
    And I go back to the previous page

    Then The page title contains "What is the vehicle’s registration number? – MOT reminders"
    And The page contains "Terms and conditions"
    And I click the "Terms and conditions" link
    Then The page title contains "Terms and conditions – MOT reminders"
    And I go back to the previous page

    Then The page title contains "What is the vehicle’s registration number? – MOT reminders"
    And The page contains "Privacy notice"
    And I click the "Privacy notice" link
    And I go to the next tab

    Then The page title contains "Personal information charter - Driver and Vehicle Standards Agency - GOV.UK"
    And I go to the next tab
    And I close extra tabs

    Then The page title contains "What is the vehicle’s registration number? – MOT reminders"
    And I enter {registration} in the registration number field
    When I press the "Continue" button

    Then The page title contains "What type of reminder do you want to get? – MOT reminders"
    And The page contains "What type of reminder do you want to get?"