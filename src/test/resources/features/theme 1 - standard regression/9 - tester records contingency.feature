@regression
Feature: 9 - Tester records Contingency Test

  # Scenario: Record contingency test (Pass)
    # Pre-req: Date in past/Contingency code specified correctly(select number from emergency_log eg. CC14862C)
    # Conduct a PASS test and check that the VT30 has the date the test was conducted (specified by tester)
    # Validation
    #   - Date/time not in the past
    #   - No contingency code specified
    #   - Generate CT blank certs (CT20, CT30, CT32)

  # Scenario: Record contingency test (Fail)
    # Pre-req: Date in past/Contingency code specified correctly(select number from emergency_log eg. CC14862C)
    # Conduct a FAIL test and check that the VT30 has the date the test was conducted (specified by tester)
    # Validation
    #   - Date/time not in the past
    #   - No contingency code specified
    #   - Generate CT blank certs (CT20, CT30, CT32)

  # Scenario: Record contingency test (Retest)
    # Pre-req: Date in past/Contingency code specified correctly(select number from emergency_log eg. CC14862C)
    # Conduct a RETEST test and check that the VT30 has the date the test was conducted (specified by tester - note only a retest if it is within the 10 day retest period since the FAIL)
    # Validation
    #   - Date/time not in the past
    #   - No contingency code specified
    #   - Generate CT blank certs (CT20, CT30, CT32)

  # Scenario: Record contingency test (Cancel)
    # Pre-req: Date in past/Contingency code specified correctly(select number from emergency_log eg. CC14862C)
    # Cancel a Contingency Test
    # Validation
    #   - Date/time not in the past
    #   - No contingency code specified
    #   - Generate CT blank certs (CT20, CT30, CT32)