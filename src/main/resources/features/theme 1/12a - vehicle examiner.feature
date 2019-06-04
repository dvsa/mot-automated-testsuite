@regression @VEinspection
Feature: 12a - Vehicle Examiner

  Scenario Outline: VE Inspection Cancelled
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I click the "Vehicle information" link
    And I search for vehicle information by "Registration (VRM)" with {registration1}
    And I click the "View MOT history" link
    And I click the first "View" link
    When I start a "<ConfirmTestType>" test
    And I press the "Start inspection" button
    And I enter an odometer reading in miles of {mileage1} plus 1000
    And I enter decelerometer results of service brake 60 and parking brake 60
    Then I click the "Cancel test" link
    And I click the "<CancelReason>" radio button
    And I press the "Cancel test" button
    And I click "Print certificate" and check the PDF contains:
      | VT32            |
      | {registration1} |
      | {vin1}          |
    And I click the "Return to home" link

    Examples:
      | ConfirmTestType       | CancelReason                |
      | Targeted Reinspection | Vehicle registered in error |
      | MOT Compliance Survey | Test equipment issue        |
      | Targeted Reinspection | VTS incident                |


  Scenario Outline: VE re-inspection Pass to Verified Pass
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I click the "Vehicle information" link
    And I search for vehicle information by "Registration (VRM)" with {registration1}
    And I click the "View MOT history" link
    And I click the first "View" link
    When I start a "<ConfirmTestType>" test
    And I press the "Start inspection" button
    And I enter an odometer reading in miles of {mileage1} plus 1000
    And I enter decelerometer results of service brake 60 and parking brake 60
    Then I press the "Review test" button
    And I check the registration plate {registration1} is shown within the registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the brake results section of the test summary is "Pass"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the major failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Finish reinspection" button
    And The page title contains "MOT reinspection complete"
    And I click "Print documents" and check the PDF contains:
      | VT32            |
      | {registration1} |
      | {vin1}          |
    And I click the "Compare test results" link
    And I perform a test comparison with outcome "No further action" and justification "Test was satisfactory"
    And I check the case outcome "No further action" is saved

  Examples:
    | ConfirmTestType |
    | Targeted Reinspection |
    | MOT Compliance Survey |


  Scenario Outline: VE re-inspection Pass to Major Verified Fail
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    When I start an MOT test for {registration1}, {vin1}, {site}
    And I click the "Enter test results" link
    And I enter an odometer reading in miles of {mileage1} plus 1000
    And I enter decelerometer results of service brake 60 and parking brake 60
    And I press the "Review test" button
    And I check the registration plate {registration1} is shown within the registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the brake results section of the test summary is "Pass"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click the "Back to user home" link

    Then I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I click the "Vehicle information" link
    And I search for vehicle information by "Registration (VRM)" with {registration1}
    And I click the "View MOT history" link
    And I click the first "View" link
    And I start a "<ConfirmTestType>" test
    And I press the "Start inspection" button
    And I enter an odometer reading in miles of {mileage1} plus 1125
    And I enter decelerometer results of service brake 30 and parking brake 16
    And I press the "Review test" button
    And I check the registration plate {registration1} is shown within the registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "FAIL"
    And I check the brake results section of the test summary is "Fail"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the major failures section of the test summary has "Service brake efficiency below requirements"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Finish reinspection" button
    And The page title contains "MOT reinspection complete"
    And I click "Print documents" and check the PDF contains:
      | VT32            |
      | {registration1} |
      | {vin1}          |
    And I click the "Compare test results" link
    And I perform a test comparison with outcome "Advisory warning letter" and justification "Test was unsatisfactory"
    And I check the case outcome "Advisory warning letter" is saved

    Examples:
      | ConfirmTestType |
      | Targeted Reinspection |
      | MOT Compliance Survey |


  Scenario Outline: VE re-inspection Fail to Disciplinary Verified Pass
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    When I start an MOT test for {registration1}, {vin1}, {site}
    And I click the "Enter test results" link
    And I enter an odometer reading in miles of {mileage1} plus 1000
    And I enter decelerometer results of service brake 31 and parking brake 10
    And I press the "Review test" button
    And I check the registration plate {registration1} is shown within the registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "FAIL"
    And I check the brake results section of the test summary is "Fail"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click the "Back to user home" link

    Then I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I click the "Vehicle information" link
    And I search for vehicle information by "Registration (VRM)" with {registration1}
    And I click the "View MOT history" link
    And I click the first "View" link
    And I start a "<ConfirmTestType>" test
    And I press the "Start inspection" button
    And I enter an odometer reading in miles of {mileage1} plus 1125
    And I enter decelerometer results of service brake 88 and parking brake 88
    And I press the "Review test" button
    And I check the registration plate {registration1} is shown within the registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the brake results section of the test summary is "Pass"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the major failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Finish reinspection" button
    And The page title contains "MOT reinspection complete"
    And I click "Print documents" and check the PDF contains:
      | VT32            |
      | {registration1} |
      | {vin1}          |
    And I click the "Compare test results" link
    And I perform a test comparison with outcome "Disciplinary action report" and justification "Test was discriminatory"
    And I check the case outcome "Disciplinary action report" is saved

    Examples:
      | ConfirmTestType |
      | Targeted Reinspection |
      | MOT Compliance Survey |


  Scenario Outline: VE re-inspection Pass to Disciplinary Verified Fail
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    When I start an MOT test for {registration1}, {vin1}, {site}
    And I click the "Enter test results" link
    And I enter an odometer reading in miles of {mileage1} plus 1000
    And I enter decelerometer results of service brake 75 and parking brake 42
    And I press the "Review test" button
    And I check the registration plate {registration1} is shown within the registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the brake results section of the test summary is "Pass"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click the "Back to user home" link

    Then I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I click the "Vehicle information" link
    And I search for vehicle information by "Registration (VRM)" with {registration1}
    And I click the "View MOT history" link
    And I click the first "View" link
    And I start a "<ConfirmTestType>" test
    And I press the "Start inspection" button
    And I enter an odometer reading in miles of {mileage1} plus 1005
    And I enter decelerometer results of service brake 12 and parking brake 6
    And I browse for a "PRS" defect of ("Brakes", "Brake actuators", "Hydraulic brake callipers", "Corroded, excessively weakened") with comment "Test prs 1"
    And I search for a "Advisory" defect of "Hydraulic brake calliper slightly damaged but function not impaired" with comment "Test advisory 1"
    And I browse for a "Minor" defect of ("Brakes", "Air and vacuum systems", "Valves", "Excessive discharge of oil") with comment "Test Minor 1"
    And I browse for a "Major" defect of ("Identification of the vehicle", "Registration plates", "Incorrect") with comment "Test Major 1"
    And I browse for a "Dangerous" defect of ("Road wheels", "Condition", "Fractured") with comment "Test Dangerous 1"
    And I press the "Review test" button
    And I check the registration plate {registration1} is shown within the registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "FAIL"
    And I check the brake results section of the test summary is "Fail"
    And I check the dangerous failures section of the test summary has "Road wheel fractured"
    And I check the dangerous failures section of the test summary has "Service brake efficiency less than"
    And I check the dangerous failures section of the test summary has "Parking brake efficiency less than"
    And I check the major failures section of the test summary has "Registration plate incorrect"
    And I check the minors section of the test summary has "Brake valve has an excessive discharge of oil"
    And I check the prs section of the test summary has "Hydraulic brake calliper excessively weakened by corrosion"
    And I check the advisory section of the test summary has "Hydraulic brake calliper slightly damaged but function not impaired"
    And I press the "Finish reinspection" button
    And The page title contains "MOT reinspection complete"
    And I click "Print documents" and check the PDF contains:
      | VT32            |
      | {registration1} |
      | {vin1}          |
    And I click the "Compare test results" link
    And I perform a test comparison with outcome "Disciplinary action report" and justification "Test was inadequate"
    And I check the case outcome "Disciplinary action report" is saved

    Examples:
      | ConfirmTestType |
      | Targeted Reinspection |
      | MOT Compliance Survey |


  Scenario: VE re-inspection Pass to Inverted Appeal Verified Fail
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    When I start an MOT test for {registration1}, {vin1}, {site}
    And I click the "Enter test results" link
    And I enter an odometer reading in miles of {mileage1} plus 75
    And I enter decelerometer results of service brake 90 and parking brake 90
    And I press the "Review test" button
    And I check the registration plate {registration1} is shown within the registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the brake results section of the test summary is "Pass"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click the "Back to user home" link

    Then I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I click the "Vehicle information" link
    And I search for vehicle information by "Registration (VRM)" with {registration1}
    And I click the "View MOT history" link
    And I click the first "View" link
    And I start a "Inverted Appeal" test
    And I enter "AB1236T" in the field with id "complaintRef"
    And I press the "Start inspection" button
    And I enter an odometer reading in miles of {mileage1} plus 80
    And I enter decelerometer results of service brake 12 and parking brake 6
    And I browse for a "PRS" defect of ("Brakes", "Brake actuators", "Hydraulic brake callipers", "Corroded, excessively weakened") with comment "Test prs 1"
    And I search for a "Advisory" defect of "Hydraulic brake calliper slightly damaged but function not impaired" with comment "Test advisory 1"
    And I browse for a "Minor" defect of ("Brakes", "Air and vacuum systems", "Valves", "Excessive discharge of oil") with comment "Test Minor 1"
    And I browse for a "Major" defect of ("Identification of the vehicle", "Registration plates", "Incorrect") with comment "Test Major 1"
    And I browse for a "Dangerous" defect of ("Road wheels", "Condition", "Fractured") with comment "Test Dangerous 1"
    And I press the "Review test" button
    And I check the registration plate {registration1} is shown within the registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "FAIL"
    And I check the brake results section of the test summary is "Fail"
    And I check the dangerous failures section of the test summary has "Road wheel fractured"
    And I check the dangerous failures section of the test summary has "Service brake efficiency less than"
    And I check the dangerous failures section of the test summary has "Parking brake efficiency less than"
    And I check the major failures section of the test summary has "Registration plate incorrect"
    And I check the minors section of the test summary has "Brake valve has an excessive discharge of oil"
    And I check the prs section of the test summary has "Hydraulic brake calliper excessively weakened by corrosion"
    And I check the advisory section of the test summary has "Hydraulic brake calliper slightly damaged but function not impaired"
    And I press the "Finish reinspection" button
    And The page title contains "MOT reinspection complete"
    And I click "Print documents" and check the PDF contains:
      | VT30            |
      | {registration1} |
      | {vin1}          |
    And I click the "Compare test results" link
    And I perform a test comparison with outcome "Disciplinary action report" and justification "Test was inadequate"
    And I check the case outcome "Disciplinary action report" is saved


  Scenario: VE re-inspection Fail to Statutory Appeal Verified Pass
    Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}
    When I start an MOT test for {registration1}, {vin1}, {site}
    And I click the "Enter test results" link
    And I enter an odometer reading in miles of {mileage1} plus 75
    And I enter decelerometer results of service brake 28 and parking brake 12
    And I press the "Review test" button
    And I check the registration plate {registration1} is shown within the registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "FAIL"
    And I check the brake results section of the test summary is "Fail"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click the "Back to user home" link

    Then I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I click the "Vehicle information" link
    And I search for vehicle information by "Registration (VRM)" with {registration1}
    And I click the "View MOT history" link
    And I click the first "View" link
    And I start a "Statutory Appeal" test
    And I enter "AB1236T" in the field with id "complaintRef"
    And I press the "Start inspection" button
    And I enter an odometer reading in miles of {mileage1} plus 80
    And I enter decelerometer results of service brake 90 and parking brake 90
    And I press the "Review test" button
    And I check the registration plate {registration1} is shown within the registration number span text
    And I check the VIN {vin1} is shown within the VIN span text
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the brake results section of the test summary is "Pass"
    And I press the "Finish reinspection" button
    And The page title contains "MOT reinspection complete"
    And I click "Print documents" and check the PDF contains:
      | VT20            |
      | {registration1} |
      | {vin1}          |
    And I click the "Compare test results" link
    And I perform a test comparison with outcome "Advisory warning letter" and justification "Test was inadequate"
    And I check the case outcome "Advisory warning letter" is saved


    Scenario: Authorised Examiner Search, slot check and PDF download
      Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
      And I load "AUTHORISED_EXAMINER" as {aeNumber}, {aeName}, {slotUsage}
      When I search for AE information with {aeNumber}
      Then I check the AE name is {aeName}
      And I click the "Slot usage" link
      And The page contains "Test slot usage"
      And I check the slot usage for the past 30 days is {slotUsage}
      And I click "PDF" and check the PDF contains:
    | Slot usage history |
    | {aeName}           |


  Scenario: Site information search and abort active test
      Given I login with 2FA using "MOT_TESTER_CLASS_4" as {tester}, {site}
      And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
      And I get the site number {siteNumber} by name {site}
      And I start an MOT test for {reg}, {vin}, {site}
      And I click the "Sign out" link
      And I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
      When I search for Site information by site number with {siteNumber}
      And I abort the active mot test at site for reg {reg}, vin {vin}
      And I click "Print certificate" and check the PDF contains:
        | VT30            |
        | {reg}           |
        | {vin}           |


  Scenario: Vehicle examiner searches for user by username
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "AEDM_USER" as {searchUser}, {organisation}
    And I search for user with username {searchUser}
    And I click the first name in the list
    And I check the user profile contains username {searchUser}
    And I change the testers group "A" status to "Qualified"


  Scenario: Vehicle information MOT test summary search
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {odo}
    And I click the "Vehicle information" link
    When I search for vehicle information by "Registration (VRM)" with {reg}
    Then I check the reg {reg}, vin {vin} on vehicle information
    And I click the "View MOT history" link
    And The page contains "Vehicle MOT test history"
    And The page does not contain "Test date/time utc"
    And I click the first "View" link
    And The page contains "MOT test summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |


  Scenario: Vehicle information MOT re-test summary search
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_REG_MOT_LATEST_RETEST" as {reg}, {vin}, {odo}
    And I click the "Vehicle information" link
    When I search for vehicle information by "Registration (VRM)" with {reg}
    Then I check the reg {reg}, vin {vin} on vehicle information
    And I click the "View MOT history" link
    And The page contains "Vehicle MOT test history"
    And The page does not contain "Test date/time utc"
    And I click the first "View" link
    And The page contains "MOT re-test summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate          |


  Scenario: Vehicle information MOT reinspection summary search
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_REG_MOT_LATEST_REINSPECTION_TEST" as {reg}, {vin}, {odo}
    And I click the "Vehicle information" link
    When I search for vehicle information by "Registration (VRM)" with {reg}
    Then I check the reg {reg}, vin {vin} on vehicle information
    And I click the "View MOT history" link
    And The page contains "Vehicle MOT test history"
    And The page does not contain "Test date/time utc"
    And I click the first "View" link
    And The page contains "MOT reinspection summary"
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      |  Quality Control check          |


  Scenario: Vehicle information test history search table validation
    Given I login without 2FA using "VEHICLE_EXAMINER_USER" as {vehicleExaminer}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {odo}
    And I click the "Vehicle information" link
    When I search for vehicle information by "Registration (VRM)" with {reg}
    Then I check the reg {reg}, vin {vin} on vehicle information
    And I click the "View MOT history" link
    And The page contains "Vehicle MOT test history"
    And The page does not contain "Test date/time utc"
    And The page contains "Filter"
    And The page contains "Test date/time"
    And The page contains "Result"
    And The page contains "Summary"
    And The page contains "Test"
    And The page contains "VIN/Chassis"
    And The page contains "Registration"
    And The page contains "Make"
    And The page contains "Model"
    And The page contains "Type"
    And The page contains "Site"
    And The page contains "User ID"
    And The page contains "Previous"
    And The page contains "Next"