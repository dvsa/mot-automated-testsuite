@motrpp @motrdemo
Feature: 01 - MOT Reminders vehicle check via email

  Scenario Outline: A MOTr user searches for a vehicle with an valid MOT and signs up via email
    Given I browse to /vrm
    And I load "<DATASET>" as {registration}, {make}, {model}, {manu_year}, {mot_expiry}
    Then The page title contains "What is the vehicle’s registration number? – MOT reminders"
    And The page contains "Get an annual MOT reminder"
    And The page contains "This is a new service – your "
    And The page contains "feedback"
    And The page contains " will help us to improve it."
    And The page contains "What is the vehicle’s registration number?"
    And The page contains "Registration number (number plate)"
    And The page contains "For example, CU57ABC"

    And I click the "Looking for HGV trailer reminders?" help link
    And The page contains "You can get HGV trailer annual test reminders on MOT reminders. Enter the trailer ID into the registration number input field to search for HGV trailers."
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
    And I click the "Email" radio button
    When I press the "Continue" button

    Then The page title contains "What is your email address? – MOT reminders"
    And The page contains "What is your email address?"
    And The page contains "Email address"
    And The page contains "Your reminder will be sent here"
    And I enter "karl.beckett@bjss.com" in the "Email address" field
    When I press the "Continue" button

    Then The page title contains "Check your details – MOT reminders"
    And The page contains "Vehicle details"
    And The page contains "{registration}"
    And The page contains "{make}"
    And The page contains "{model}"
    And The page contains "RED"
    And The page contains "{manu_year}"
    And The page contains "{mot_expiry}"
    And The page contains "Your details"
    And The page contains "karl.beckett@bjss.com"
    And I click the "Incorrect vehicle details?" help link
    And The page contains "If you think any of the vehicle details are wrong, "
    And The page contains "contact DVSA"
    And I click the "Incorrect vehicle details?" help link
    When I press the "Continue" button

    Then The page title contains "One more step – MOT reminders"
    And The page contains "One more step"
    And The page contains "Check your inbox "
    And The page contains "karl.beckett@bjss.com"
    And The page contains "and click the link in the email from GOV.UK to activate your MOT reminder."
    And The page contains "The link expires 24h from now."

    Examples:
      | DATASET                      |
      | VEHICLE_REG_MOT_CURRENT_MOTR |