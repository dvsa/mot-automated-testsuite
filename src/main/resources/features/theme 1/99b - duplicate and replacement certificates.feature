# The themes 99a and 99b need to be ran last as they change vehicle data that
# other tests may use and would have cached prior to other tests running
# subsequently this can cause errors such as vehicle not found if these tests change in running order
@regression
Feature: 99b - duplicate and replacement certificates

  Scenario: DVLA Manager edits VRM / VIN on latest certificate
    Given I login without 2FA using "DVLA_MANAGER_USER" as {DVLAManger}
    And I load uniquely "VEHICLE_CLASS_4_NOT_UPDATED_TODAY" as {reg}, {vin}, {mileage}
    And I search for certificates with reg {reg}
    And I click the first "View certificate" link
    And I press the "Edit this MOT test result" button
    And I edit the vehicle vin with "DVLA304050921024"
    And I edit the vehicle registration with "DVLA904"
    When I submit the certificate changes
    And I check the registration on the confirmation page is "DVLA904"
    And I check the vin on the confirmation page is "DVLA304050921024"
    Then I press the "Finish changes and print certificate" button
    And The page title contains "Test Results Updated Successfully"
    And I click "Print" and check the PDF contains:
      | VT                              |
      | DVLA904                         |
      | DVLA304050921024                |


  Scenario: DVLA Manager edits VRM / VIN on historic certificate
    Given I login without 2FA using "DVLA_MANAGER_USER" as {DVLAManager}
    And I load uniquely "VEHICLE_CLASS_4_HISTORIC_10_DAYS" as {reg}, {vin}, {mileage}, {testNumber}
    And I search for certificates with reg {reg}
    And I click the first "View certificate" link
    And I press the "Edit this MOT test result" button
    And I edit the vehicle vin with "DVLA304050921020"
    And I edit the vehicle registration with "DVLA903"
    When I submit the certificate changes
    And I check the registration on the confirmation page is "DVLA903"
    And I check the vin on the confirmation page is "DVLA304050921020"
    Then I press the "Finish changes and print certificate" button
    And The page title contains "Test Results Updated Successfully"
    And I check there is a "Print" link
    And I click "Print" and check the PDF contains:
      | VT                              |
      | DVLA903                         |
      | DVLA304050921020                |

  Scenario: AO1 edits vehicle details on history certificate
    Given I login without 2FA using "AO1_USER" as {AO1}
    And I load uniquely "VEHICLE_CLASS_4_HISTORIC_10_DAYS" as {reg}, {vin}, {mileage}, {testNumber}
    And I search for certificates with reg {reg}
    And I click the "Show older tests" link
    And I click the first "View certificate" link
    And I press the "Edit this MOT test result" button
    And I edit the make "FORD" and model "FOCUS"
    And I edit the primary colour "Red" and secondary colour "White"
    And I edit the vehicle vin with "VINR3V0L"
    And I edit the vehicle registration with "R3GHAU5"
    And I edit the country of registration with "GB, NI (UK) - Northern Ireland"
    When I submit the certificate changes
    And I check the registration on the confirmation page is "R3GHAU5"
    And I check the vin on the confirmation page is "VINR3V0L"
    And I check the make on the confirmation page is "FORD"
    And I check the model on the confirmation page is "FOCUS"
    And I check the colours are correct "Red" and "White"
    Then I press the "Finish changes and print certificate" button
    And The page title contains "Test Results Updated Successfully"
    And I check there is a "Print" link
    And I click "Print" and check the PDF contains:
      | R3GHAU5                         |
      | VINR3V0L                        |
      | FORD                            |
      | FOCUS                           |

  Scenario: Tester edits odometer and vehicle colour on certificate
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {tester}, {site}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I start an MOT test for {reg}, {vin}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage} plus 5000
    And I enter decelerometer results of service brake 70 and parking brake 30
    And I press the "Review test" button

    And The page title contains "MOT test summary"
    And I check the vehicle summary section of the test summary has "Result" of "PASS"
    And I check the registration number {reg} is shown within the Registration number span text
    And I check the VIN {vin} is shown within the VIN span text
    And I check the brake results section of the test summary is "Pass"
    And I check the dangerous failures section of the test summary has "None recorded"
    And I check the major failures section of the test summary has "None recorded"
    And I check the minors section of the test summary has "None recorded"
    And I check the prs section of the test summary has "None recorded"
    And I check the advisory section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20                  |
      | {reg}                 |
      | {vin}                 |
    And I click the "Back to user home" link

    When I search for certificates with reg {reg}
    And I click the first "View certificate" link
    And I press the "Edit this MOT test result" button
    And I update the odometer reading by 3000
    And I edit the primary colour "Red" and secondary colour "White"
    And I press the "Review changes" button
    And I check the odometer reading on the confirmation page is correct
    And I check the colours are correct "Red" and "White"
    Then I press the "Finish changes and print certificate" button
    And The page title contains "Test Results Updated Successfully"
    And I check there is a "Print" link
    And I click "Print" and check the PDF contains:
      | VT                    |
      | {reg}                 |
      | {vin}                 |