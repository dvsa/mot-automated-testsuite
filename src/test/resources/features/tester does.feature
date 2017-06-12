# Theme 1 (Standard Regression), Test 7:
# Tester does:
#   MOT test
#   Retest
#   Abort
#   Add RFR Failures to a FAIL
#   Add PRS Failures to a PASS
#   Add advisory tests to both FAIL and PASS
#   Add, Edit, Remove a defect.
Feature: Tester does...

  Scenario: Tester enters an MOT test pass
    Given I login with 2FA as username JOEN5622
    When I click the "Start MOT test" link
    And The page title contains "Find a vehicle"
    And I enter "HO94LFW" in the "Registration mark" field
    And I enter "452484" in the "VIN" field
    And I press the "Search" button
    And The page title contains "Find a vehicle"
