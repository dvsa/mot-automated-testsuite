#@mothpp @mothint
@mothhgv
Feature: 40 - Check HGV with a just expired Annual Test

  Scenario: A MOTH user searches for a HGV with a Annual Test that is just about to expire
    Given I browse to /
    And I enter "EXYESTE" in the registration field
    When I press the "Continue" button

    Then The page contains "EXYESTE"
    And The page contains "DAF FA LF45.130 08"
    And The page contains "This vehicle’s annual test has expired"
    And The page contains "ou can be fined up to £1,000 for driving without a valid annual test"