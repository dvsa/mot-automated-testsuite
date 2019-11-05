@mothpp @mothint
Feature: 01 - MOT History vehicle check with expired/expiring test

  @moth_pp_test
  Scenario: A MOTH user searches for a vehicle with an expired MOT
    Given I browse to /
    And I load "VEHICLE_REG_MOT_EXPIRED" as {registration}, {model}, {date}, {mot_expiry}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "This vehicle's MOT has expired"
    And The page contains "You can be fined up to £1000 for driving without a valid MOT"
    And The page contains "This vehicle may be MOT exempt, for more information refer to MOT exemption guidance"
    And The page contains "Colour"
    And The page contains "Fuel type"
    And The page contains "Petrol"
    And The page contains "Date registered"
    And The page contains "{date}"
    And The page contains "MOT expired on"
    And The page contains "{mot_expiry}"
    And The page contains "MOT history"
    And The page contains "Check mileage recorded at test, MOT expiry date, defects and advisories"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "View test location"

    And The page contains "The MOT test changed on 20 May 2018"
    And The page contains "Defects are now categorised according to their severity – dangerous, major, and minor. Find out more"

    And I click the last "Find out more" link
    And I go to the next tab
    Then The page title contains "MOT rule changes: 20 May 2018 - GOV.UK"
    And I go to the next tab
    And I close extra tabs

  Scenario: A MOTH user searches for a vehicle with an MOT about to expire tomorrow
    Given I browse to /
    And I load "VEHICLE_REG_DUE_TOMORROW" as {registration}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "This vehicle's MOT expires soon"

  Scenario: A MOTH user searches for a vehicle with just expired MOT yesterday
    Given I browse to /
    And I load "VEHICLE_REG_JUST_EXPIRED" as {registration}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "This vehicle's MOT has expired"
    And The page contains "You can be fined up to £1000 for driving without a valid MOT"
    And The page contains "This vehicle may be MOT exempt, for more information refer to MOT exemption guidance"

  Scenario: A MOTH user searches for a vehicle with an MOT that expires today
    Given I browse to /
    And I load "VEHICLE_REG_EXPIRES_TODAY" as {registration}, {model}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And The page contains "{model}"
    And The page contains "This vehicle's MOT expires soon"

  Scenario: A MOTH user searches for a vehicle with an expired MOT and check footer links
    Given I browse to /
    Then The page title contains "What is the vehicle's registration number - MOT History"
    And I load "VEHICLE_REG_MOT_EXPIRED" as {registration}, {model}, {date}, {mot_expiry}
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
    And The page contains "This vehicle's MOT has expired"
    And The page contains "You can be fined up to £1000 for driving without a valid MOT"
    And The page contains "This vehicle may be MOT exempt, for more information refer to MOT exemption guidance"
    And The page contains "Colour"
    And The page contains "Fuel type"
    And The page contains "Petrol"
    And The page contains "Date registered"
    And The page contains "{date}"
    And The page contains "MOT expired on"
    And The page contains "{mot_expiry}"
    And The page contains "MOT history"
    And The page contains "Check mileage recorded at test, MOT expiry date, defects and advisories"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "View test location"
    And The page contains "The MOT test changed on 20 May 2018"
    And The page contains "Defects are now categorised according to their severity – dangerous, major, and minor. Find out more"
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