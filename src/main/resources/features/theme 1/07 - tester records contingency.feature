@regression
Feature: 07 - Tester records Contingency Test

  Scenario: A tester record a pass contingency test and confirms a slot is consumed
    Given I login with 2FA using "MOT_TESTER_CLASS_4_WITH_ONLY_ONE_SITE" as {username1}, {site}
    And I get the slot count from the homepage for site {site}

    Then I start a contingency MOT test at site {site}
    And I load "CONTINGENCY_CODE" as {code}, {issueDate}, {dateOfTest}, {day}, {month}, {year}
    And I enter {day} in the "Day" field
    And I enter {month} in the "Month" field
    And I enter {year} in the "Year" field
    And I enter "9" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I click the "Other" radio button
    And I enter "Automated Testing" in the "If other, please enter a reason" field
    And I enter {code} in the "Contingency code" field
    And I press the "Confirm contingency test" button

    And I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I enter {registration1} in the "Registration mark" field
    And I enter {vin1} in the "VIN" field
    And I press the "Search" button

    And I click the "Select vehicle" link
    And I press the "Confirm and start test" button
    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 58 and parking brake 16
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the Test Information summary section of the test summary has "Issue date" of {issueDate}
    And I check the registration number {registration1} is shown within the Registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the brake results section of the test summary is "Pass"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20            |
      | {registration1} |
      | {vin1}          |
      | {dateOfTest}    |
    And I click the "Back to user home" link
    And I check a slot was successfully used for site {site}

  @regressiondata @OpenInterfaceTests
  Scenario: A tester record a pass contingency test 1 day ago
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then I start a contingency MOT test at site {site}
    And I load "CONTINGENCY_CODE_1_DAY" as {code}, {issueDate}, {dateOfTest}, {day}, {month}, {year}
    And I enter {day} in the "Day" field
    And I enter {month} in the "Month" field
    And I enter {year} in the "Year" field
    And I enter "9" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I click the "Other" radio button
    And I enter "Automated Testing" in the "If other, please enter a reason" field
    And I enter {code} in the "Contingency code" field
    And I press the "Confirm contingency test" button

    And I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I enter {registration1} in the "Registration mark" field
    And I enter {vin1} in the "VIN" field
    And I press the "Search" button

    And I click the "Select vehicle" link
    And I press the "Confirm and start test" button
    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 58 and parking brake 16
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the Test Information summary section of the test summary has "Issue date" of {issueDate}
    And I check the registration number {registration1} is shown within the Registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the brake results section of the test summary is "Pass"
    And I record the MOT test number
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20            |
      | {registration1} |
      | {vin1}          |
      | {dateOfTest}    |
    And I click the "Back to user home" link

  Scenario: A tester records a fail contingency test
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then I start a contingency MOT test at site {site}
    And I load "CONTINGENCY_CODE" as {code}, {issueDate}, {dateOfTest}, {day}, {month}, {year}
    And I enter {day} in the "Day" field
    And I enter {month} in the "Month" field
    And I enter {year} in the "Year" field
    And I enter "9" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I click the "Other" radio button
    And I enter "Automated Testing" in the "If other, please enter a reason" field
    And I enter {code} in the "Contingency code" field
    And I press the "Confirm contingency test" button

    And I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I enter {registration1} in the "Registration mark" field
    And I enter {vin1} in the "VIN" field
    And I press the "Search" button

    And I click the "Select vehicle" link
    And I press the "Confirm and start test" button
    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 58 and parking brake 10
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the vehicle summary section of the test summary has "Result" of "FAIL"
    And I check the Test Information summary section of the test summary has "Issue date" of {issueDate}
    And I check the registration number {registration1} is shown within the Registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the brake results section of the test summary is "Fail"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30            |
      | {registration1} |
      | {vin1}          |
      | {dateOfTest}    |
    And I click the "Back to user home" link


  Scenario: A tester records a fail contingency test then a contingency retest pass 10 working days later
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then I start a contingency MOT test at site {site}
    And I load "CONTINGENCY_CODE_14_DAY" as {code}, {issueDate}, {dateOfTest}, {day}, {month}, {year}
    And I enter {day} in the "Day" field
    And I enter {month} in the "Month" field
    And I enter {year} in the "Year" field
    And I enter "9" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I click the "Communication problem" radio button
    And I enter {code} in the "Contingency code" field
    And I press the "Confirm contingency test" button

    And I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I enter {registration1} in the "Registration mark" field
    And I enter {vin1} in the "VIN" field
    And I press the "Search" button

    And I click the "Select vehicle" link
    And I press the "Confirm and start test" button
    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Dangerous" defect of ("Steering", "Steering column", "Fractured, steering affected") with comment "Test defect 1"
    And I search for a "Advisory" defect of "Fuel pipe/s corroded" with comment "Test advisory 2"
    And I enter decelerometer results of service brake 60 and parking brake 30
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the vehicle summary section of the test summary has "Result" of "FAIL"
    And I check the Test Information summary section of the test summary has "Issue date" of {issueDate}
    And I check the registration number {registration1} is shown within the Registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the brake results section of the test summary is "Pass"
    And I check the dangerous failures section of the test summary has "Steering column fractured to the extent that steering is affected"
    And I check the dangerous failures section of the test summary has "Test defect 1"
    And I check the advisory section of the test summary has "Fuel Pipe/s corroded"
    And I check the advisory section of the test summary has "Test advisory 2"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                                              |
      | {registration1}                                                   |
      | {vin1}                                                            |
      | Steering column fractured to the extent that steering is affected |
      | Test defect 1                                                     |
      | Fuel pipe/s corroded                                              |
      | Test advisory 2                                                   |
      | {dateOfTest}                                                      |
    And I click the "Back to user home" link

    Then I start a contingency MOT test at site {site}
    And I load "CONTINGENCY_CODE_4_DAY" as {code2}, {issueDate2}, {dateOfTest2}, {day2}, {month2}, {year2}
    And I enter {day2} in the "Day" field
    And I enter {month2} in the "Month" field
    And I enter {year2} in the "Year" field
    And I enter "11" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I click the "Communication problem" radio button
    And I enter {code2} in the "Contingency code" field
    And I press the "Confirm contingency test" button

    And I enter {registration1} in the "Registration mark" field
    And I enter {vin1} in the "VIN" field
    And I press the "Search" button
    And I click the "Select vehicle" link

    And I press the "Confirm and start retest" button

    And I enter an odometer reading in miles of {mileage1} plus 5005
    And I mark the defect "Steering column fractured to the extent that steering is affected" as repaired
    And I press the "Review test" button

    Then The page title contains "MOT re-test summary"
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the Test Information summary section of the test summary has "Issue date" of {issueDate2}
    And I check the registration number {registration1} is shown within the Registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the brake results section of the test summary is "None Recorded"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the major failures section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "Fuel Pipe/s corroded"
    And I check the advisory section of the test summary has "Test advisory 2"
    And I press the "Save test result" button
    And The page title contains "MOT re-test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20                      |
      | {registration1}           |
      | {vin1}                    |
      | Fuel pipe/s corroded      |
      | Test advisory 2           |
      | {dateOfTest2}             |

  Scenario: A tester records a failed contingency test two day's ago and performs retest pass now
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then I start a contingency MOT test at site {site}
    And I load "CONTINGENCY_CODE_2_DAY" as {code}, {issueDate}, {dateOfTest}, {day}, {month}, {year}
    And I enter {day} in the "Day" field
    And I enter {month} in the "Month" field
    And I enter {year} in the "Year" field
    And I enter "9" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I click the "Communication problem" radio button
    And I enter {code} in the "Contingency code" field
    And I press the "Confirm contingency test" button

    And I load uniquely "VEHICLE_CLASS_4_NOT_UPDATED_WEEK" as {registration1}, {vin1}, {mileage1}
    And I enter {registration1} in the "Registration mark" field
    And I enter {vin1} in the "VIN" field
    And I press the "Search" button

    And I click the "Select vehicle" link
    And I press the "Confirm and start test" button
    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Major" defect of ("Identification of the vehicle", "Vehicle identification number", "Incomplete") with comment "Test defect 1"
    And I search for a "Advisory" defect of "Standard fitment seat belt missing" with comment "Test advisory 2"
    And I enter decelerometer results of service brake 60 and parking brake 30
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the vehicle summary section of the test summary has "Result" of "FAIL"
    And I check the Test Information summary section of the test summary has "Issue date" of {issueDate}
    And I check the registration number {registration1} is shown within the Registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the brake results section of the test summary is "Pass"
    And I check the major failures section of the test summary has "Vehicle Identification Number incomplete"
    And I check the major failures section of the test summary has "Test defect 1"
    And I check the advisory section of the test summary has "Standard fitment seat belt missing"
    And I check the advisory section of the test summary has "Test advisory 2"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                     |
      | {registration1}                          |
      | {vin1}                                   |
      | Vehicle identification number incomplete |
      | Test defect 1                            |
      | Standard fitment seat belt missing       |
      | Test advisory 2                          |
      | {dateOfTest}                             |
    And I click the "Back to user home" link

    And I start an MOT retest for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter retest results" link

    And I enter an odometer reading in miles of {mileage1} plus 5005
    And I mark the defect "Vehicle Identification Number incomplete" as repaired
    And I mark the defect " Standard fitment seat belt missing" as repaired
    And I press the "Review test" button

    Then The page title contains "MOT re-test summary"
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the registration number {registration1} is shown within the Registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the brake results section of the test summary is "None Recorded"
    And I check the major failures section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT re-test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20            |
      | {registration1} |
      | {vin1}          |

  Scenario: A tester record contingency test and then cancels the contingency test
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    Then I start a contingency MOT test at site {site}
    And I load "CONTINGENCY_CODE" as {code}, {issueDate}, {dateOfTest}, {day}, {month}, {year}
    And I enter {day} in the "Day" field
    And I enter {month} in the "Month" field
    And I enter {year} in the "Year" field
    And I enter "09" in the field with id "contingency_time-hour"
    And I enter "00" in the field with id "contingency_time-minutes"
    And I select "am" in the field with id "contingency_time-ampm"
    And I click the "Other" radio button
    And I enter "Automated Testing" in the "If other, please enter a reason" field
    And I enter {code} in the "Contingency code" field
    And I press the "Confirm contingency test" button

    And I load "VEHICLE_CLASS_4" as {registration}, {vin}, {mileage}
    And I enter {registration} in the "Registration mark" field
    And I enter {vin} in the "VIN" field
    And I press the "Search" button
    And I click the "Select vehicle" link
    And I press the "Confirm and start test" button

    And I enter an odometer reading in miles of {mileage} plus 5000
    And I enter decelerometer results of service brake 51 and parking brake 10
    And I click the "Cancel test" link

    And I click the "Aborted by VE" radio button
    And I press the "Cancel test" button
    And I click "Print certificate" and check the PDF contains:
      | VT30           |
      | {registration} |
      | {vin}          |
    And I click the "Return to home" link

  @smoke
  Scenario: A tester prints CT certificates
    Given I login with 2FA using "MOT_TESTER_CLASS_4_WITH_ONLY_ONE_SITE" as {username}, {site}
    And I click "CT20 Pass Certificate" and check the PDF contains:
      | Contingency MOT Test Certificate |
      | CT20                             |
      | {site}                           |
    And I click "CT30 Refusal Notice" and check the PDF contains:
      | Contingency Refusal of an MOT Test Certificate |
      | CT30                                           |
      | {site}                                         |

  Scenario: A tester records a contingency retest of a failed normal MOT test
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I browse for a "Dangerous" defect of ("Steering", "Steering column", "Deformed, steering affected") with comment "Test defect 1"
    And I browse for a "Major" defect of ("Body, chassis, structure", "Exhaust system", "System insecure") with comment "Test defect 2"
    And I browse for a "Major" defect of ("Lamps, reflectors and electrical equipment", "Stop lamp", "Not working") with comment "Test defect 3"
    And I enter decelerometer results of service brake 60 and parking brake 60
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the vehicle summary section of the test summary has "Result" of "FAIL"
    And I check the registration number {registration1} is shown within the Registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the brake results section of the test summary is "Pass"
    And I check the dangerous failures section of the test summary has "Steering column deformed to the extent that steering is affected"
    And I check the dangerous failures section of the test summary has "Test defect 1"
    And I check the dangerous failures section of the test summary does not have "Exhaust system insecure"
    And I check the dangerous failures section of the test summary does not have "Test defect 2"
    And I check the dangerous failures section of the test summary does not have "Stop lamp(s) not working"
    And I check the dangerous failures section of the test summary does not have "Test defect 3"
    And I check the major failures section of the test summary has "Exhaust system insecure"
    And I check the major failures section of the test summary has "Test defect 2"
    And I check the major failures section of the test summary has "Stop lamp(s) not working"
    And I check the major failures section of the test summary has "Test defect 3"
    And I check the major failures section of the test summary does not have "Steering column deformed to the extent that steering is affected"
    And I check the major failures section of the test summary does not have "Test defect 1"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT30                                                             |
      | {registration1}                                                  |
      | {vin1}                                                           |
      | Steering column deformed to the extent that steering is affected |
      | Exhaust system insecure                                          |
      | Stop lamp(s) not working                                         |
      | Test defect 1                                                    |
      | Test defect 2                                                    |
      | Test defect 3                                                    |
    And I click the "Back to user home" link
    And I wait for "60" seconds

    Then I start a contingency MOT test at site {site}
    And I load "CONTINGENCY_CODE_0_DAY" as {code2}, {issueDate2}, {dateOfTest2}, {day2}, {month2}, {year2}
    And I enter {day2} in the "Day" field
    And I enter {month2} in the "Month" field
    And I enter {year2} in the "Year" field
    And I enter the current time for the contingency test

    And I click the "Communication problem" radio button
    And I enter {code2} in the "Contingency code" field
    And I press the "Confirm contingency test" button

    And I enter {registration1} in the "Registration mark" field
    And I enter {vin1} in the "VIN" field
    And I press the "Search" button
    And I click the "Select vehicle" link

    And I press the "Confirm and start retest" button

    And I enter an odometer reading in miles of {mileage1} plus 5005
    And I mark the defect "Steering column deformed to the extent that steering is affected" as repaired
    And I mark the defect "Exhaust system insecure" as repaired
    And I mark the defect "Stop lamp(s) not working" as repaired
    And I press the "Review test" button

    Then The page title contains "MOT re-test summary"
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the Test Information summary section of the test summary has "Issue date" of {issueDate2}
    And I check the registration number {registration1} is shown within the Registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the brake results section of the test summary is "None Recorded"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the major failures section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT re-test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20                      |
      | {registration1}           |
      | {vin1}                    |
      | {dateOfTest2}             |
