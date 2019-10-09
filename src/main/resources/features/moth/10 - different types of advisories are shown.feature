@mothpp @mothint
Feature: 10 - Dangerous and Major RFR's types are shown

  Scenario: A MOTH user searches for an MOT test with dangerous advisories after 20th May 2018 (EU Road Worthiness)
    Given I browse to /
    And I load "VEHICLE_REG_DANGEROUS_ADVISE_EURW" as {registration}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "Pass"
    And The page contains "Dangerous"
    And The page contains "Monitor and repair if necessary (advisories):"
    And The page contains "What are advisories?"
    And I click the "What are advisories?" help link
    And The page contains "Advisories are given for guidance. Some of these may need to be monitored in case they become more serious and need immediate repairs."

  Scenario: A MOTH user searches for an MOT test with dangerous advisories before 20th May 2019 (EU Road Worthiness)
    Given I browse to /
    And I load "VEHICLE_REG_DANGEROUS_ADVISE" as {registration}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "Pass"
    And The page contains "Dangerous"
    And The page contains "What are advisories?"
    And I click the "What are advisories?" help link
    And The page contains "Advisory items are provided for advice. For some of these, if they became more serious, your vehicle may no longer be roadworthy and could require immediate attention."

  Scenario: A MOTH user searches for an MOT test with dangerous failures after 20th May 2018 (EU Road Worthiness)
    Given I browse to /
    And I load "VEHICLE_REG_DANGEROUS_EURW" as {registration}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "Fail"
    And The page contains "Do not drive until repaired (dangerous defects):"
    And The page contains "What are defects?"
    And I click the "What are defects?" help link
    And The page contains "Dangerous defects are a direct and immediate risk to the road safety or the environment. A vehicle with a dangerous defect will fail the test."
    And The page title contains "Check MOT history"

  Scenario: A MOTH user searches for an MOT test with dangerous failures before 20th May 2019 (EU Road Worthiness)
    Given I browse to /
    And I load "VEHICLE_REG_DANGEROUS" as {registration}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "Fail"
    And The page contains "Dangerous"
    And The page contains "What are failures?"
    And I click the "What are failures?" help link
    And The page contains "Failure items must be fixed before the vehicle can pass its MOT."
    And The page title contains "Check MOT history"

  Scenario: A MOTH user searches for an MOT test with dangerous PRS after 20th May 2019 (EU Road Worthiness)
    Given I browse to /
    And I load "VEHICLE_REG_DANGEROUS_PRS_EURW" as {registration}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "Fail"
    And The page contains "Do not drive until repaired (dangerous defects):"
    And The page contains "What are defects?"
    And I click the "What are defects?" help link
    And The page contains "Dangerous defects are a direct and immediate risk to the road safety or the environment. A vehicle with a dangerous defect will fail the test."
    And The page title contains "Check MOT history"

  Scenario: A MOTH user searches for an MOT test with dangerous PRS before 20th May 2018 (EU Road Worthiness)
    Given I browse to /
    And I load "VEHICLE_REG_DANGEROUS_PRS" as {registration}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "Pass"
    And The page contains "Fail"
    And The page contains "Reason(s) for failure"
    And The page contains "Dangerous"
    And I click the "What are failures?" help link
    And The page contains "Failure items must be fixed before the vehicle can pass its MOT."
    And The page contains "Dangerous The tester has considered a part of the vehicle to be so defective that it is dangerous to be driven on the road."
    And The page title contains "Check MOT history"

  Scenario: A MOTH user searches for an MOT test with major failures after 20th May 2018 (EU Road Worthiness)
    Given I browse to /
    And I load "VEHICLE_REG_MAJOR_EURW" as {registration}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "Fail"
    And The page contains "Repair immediately (major defects):"
    And The page contains "What are defects?"
    And I click the "What are defects?" help link
    And The page contains "Major defects may compromise the safety of the vehicle, put other road users at risk, or harm the environment. A vehicle with a major defect will fail the test."
    And The page title contains "Check MOT history"

  Scenario: A MOTH user searches for an MOT test with advisory after 20th May 2019 (EU Road Worthiness)
    Given I browse to /
    And I load "VEHICLE_REG_ADVISORIES_EURW" as {registration}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page contains "{registration}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "Pass"
    And The page contains "Monitor and repair if necessary (advisories):"
    And The page contains "What are advisories?"
    And I click the "What are advisories?" help link
    And The page contains "Advisories are given for guidance. Some of these may need to be monitored in case they become more serious and need immediate repairs."
    And The page title contains "Check MOT history"

  Scenario: A MOTH user searches for an MOT test with dangerous manual advisories after 20th May 2018 (EU Road Worthiness)
    Given I browse to /
    And I load "VEHICLE_REG_DANGEROUS_MANUAL_EURW" as {registration}, {rfr_description}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "Fail"
    And The page contains "Dangerous"
    And The page contains "Monitor and repair if necessary (advisories):"
    And The page contains "{rfr_description}"

  Scenario: A MOTH user searches for an MOT test with dangerous manual advisories before 20th May 2019 (EU Road Worthiness)
    Given I browse to /
    And I load "VEHICLE_REG_DANGEROUS_MANUAL" as {registration}, {rfr_description}
    And I enter {registration} in the registration field
    When I press the "Continue" button

    Then The page title contains "Check MOT history"
    And The page contains "{registration}"
    And I click the accordion section with the id "mot-history-description"
    And The page contains "Fail"
    And The page contains "Dangerous"
    And The page contains "Advisory notice item(s)"
    And The page contains "{rfr_description}"