@mothpp @mothint
Feature: 02 - Check vehicle with current valid MOT

  Scenario: A MOTH user searches for a vehicle with a current valid MOT and clicks Get a reminder email
    Given I browse to /
    And I load "VEHICLE_REG_MOT_CURRENT" as {registration}, {model}, {date}, {mot_expiry}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "Colour"
    And The page contains "Fuel type"
    And The page contains "Petrol"
    And The page contains "Date registered"
    And The page contains "{date}"
    And The page contains "MOT valid until"
    And The page contains "{mot_expiry}"
    And The page contains "Get an MOT reminder"
    And The page contains " by email or text"
    When I click the "Get an MOT reminder" link
    And I go to the next tab

    Then The page title contains "Get MOT reminders - GOV.UK"
    And I close extra tabs

  Scenario: A MOTH user searches for a vehicle with a current valid MOT and clicks Contact DVSA
    Given I browse to /
    And I load "VEHICLE_REG_MOT_CURRENT" as {registration}, {model}, {date}, {mot_expiry}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "Colour"
    And The page contains "Fuel type"
    And The page contains "Petrol"
    And The page contains "Date registered"
    And The page contains "{date}"
    And The page contains "MOT valid until"
    And The page contains "{mot_expiry}"
    And The page contains "If you think the MOT expiry date or any of the vehicle details are wrong,"
    And I click the "contact" link
    And I go to the next tab

    Then The page title contains "Contact DVSA"
    And I close extra tabs

  Scenario: A MOTH user searches for a vehicle with a current valid MOT and clicks back and searches again
    Given I browse to /
    And I load "VEHICLE_REG_MOT_CURRENT" as {registration}, {model}, {date}, {mot_expiry}
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "Colour"
    And The page contains "Fuel type"
    And The page contains "Petrol"
    And The page contains "Date registered"
    And The page contains "{date}"
    And The page contains "MOT valid until"
    And The page contains "{mot_expiry}"

    And I click the "Back" link
    And The page title contains "What is the vehicle's registration number - MOT History"
    And The page contains "What is the vehicle's registration number?"
    And I enter {registration} in the registration field

    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "Date registered"
    And The page contains "{date}"
    And The page contains "MOT valid until"
    And The page contains "{mot_expiry}"

  Scenario: A MOTH user searches for a vehicle with a current valid MOT and check footer links
    Given I browse to /
    Then The page title contains "What is the vehicle's registration number - MOT History"
    And I load "VEHICLE_REG_MOT_CURRENT" as {registration}, {model}, {date}, {mot_expiry}
    And The page contains "Cookies"
    And I click the "Cookies" link

    Then The page title contains "Cookie policy - Check MOT history"
    And I go back to the previous page

    Then The page title contains "What is the vehicle's registration number - MOT History"
    And The page contains "Terms and conditions"
    And I click the "Terms and conditions" link

    Then The page title contains "Terms and conditions - Check MOT history"
    And I go back to the previous page

    Then The page title contains "What is the vehicle's registration number - MOT History"
    And The page contains "Privacy notice"
    And I click the "Privacy notice" link
    And I go to the next tab

    Then The page title contains "Personal information charter - Driver and Vehicle Standards Agency - GOV.UK"
    And I go to the next tab
    And I close extra tabs

    Then The page title contains "What is the vehicle's registration number - MOT History"
    And The page contains "MOT history API"
    And I click the "MOT history API" link
    And I go to the next tab

    Then The page title contains "MOT history API - Documentation"
    And I go to the next tab
    And I close extra tabs

    Then The page title contains "What is the vehicle's registration number - MOT History"
    And The page contains "Built by the Driver & Vehicle Standards Agency"
    And The page contains "All content is available under the Open Government Licence v3.0, except where otherwise stated"
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "Colour"
    And The page contains "Fuel type"
    And The page contains "Petrol"
    And The page contains "Date registered"
    And The page contains "{date}"
    And The page contains "MOT valid until"
    And The page contains "{mot_expiry}"
    And The page contains "Get an MOT reminder"
    And The page contains " by email or text"
    And The page contains "Cookies"
    And I click the "Cookies" link

    Then The page title contains "Cookie policy - Check MOT history"
    And I go back to the previous page

    Then The page title contains "Check MOT history"
    And The page contains "Terms and conditions"
    And I click the "Terms and conditions" link

    Then The page title contains "Terms and conditions - Check MOT history"
    And I go back to the previous page

    Then The page title contains "Check MOT history"
    And The page contains "Privacy notice"
    And I click the "Privacy notice" link
    And I go to the next tab

    Then The page title contains "Personal information charter - Driver and Vehicle Standards Agency - GOV.UK"
    And I go to the next tab
    And I close extra tabs

    Then The page title contains "Check MOT history"
    And The page contains "MOT history API"
    And I click the "MOT history API" link
    And I go to the next tab

    Then The page title contains "MOT history API - Documentation"
    And I go to the next tab
    And I close extra tabs

    Then The page title contains "Check MOT history"
    And The page contains "Built by the Driver & Vehicle Standards Agency"
    And The page contains "All content is available under the Open Government Licence v3.0, except where otherwise stated"
