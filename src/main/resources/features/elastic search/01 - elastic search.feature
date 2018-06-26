@elasticSearch
Feature: 01 - elastic search

  Scenario Outline: Login as a tester and Query elastic search
    Given I load "<vehicle>" as {registration}, {vin}, {mileage}
    And I login with 2FA using "<tester>" as {username}, {site}
    And I set the starting url key as secondEnvironment

    When I start an MOT test for {registration}, {vin}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I click the "Add a defect" link
    And I query elastic search with "<searchTerm>" as "original"
    And I click the "Finish and return to MOT test results" link

    And I click the "Cancel test" link
    And I click the "Vehicle registered in error" radio button
    And I press the "Cancel test" button

    And I set the starting url key as secondEnvironment
    And I login with 2FA using "<tester>" as {username}, {site}
    And I start an MOT test for {registration}, {vin}, {site}

    And The page title contains "Your home"
    And I click the "Enter test results" link
    And I click the "Add a defect" link

    And I query elastic search with "<searchTerm>" as "changed"
    And I click the "Finish and return to MOT test results" link
    And I click the "Cancel test" link

    And I click the "Vehicle registered in error" radio button
    And I press the "Cancel test" button
    And I compare the search results for "original" and "changed" with data "<searchTerm>"

    Examples:
    |tester                  |vehicle               |searchTerm        |
    |MOT_TESTER_CLASS_4      |VEHICLE_CLASS_4       |ES_VEHICLE_CLASS_4|