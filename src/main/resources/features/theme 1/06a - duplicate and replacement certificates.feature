@regression
Feature: 06a - duplicate and replacement certificates

  @smoke
  Scenario: AO1 edits non-vehicle details on certificate
    Given I login without 2FA using "AO1_USER" as {AO1}
    And I load uniquely "VEHICLE_CLASS_4_WITH_MOT" as {reg}, {vin}, {mileage}
    And I load "SITE" as {siteNumber}, {siteName}
    When I search for certificates with reg {reg}
    And I click the first "View certificate" link
    And I press the "Edit this MOT test result" button
    And I update the odometer reading by 3000
    And I update the testing location to {siteNumber}, {siteName}
    And I update the expiry date by adding 5 days
    And I submit the certificate changes
    And I check the odometer reading on the confirmation page is correct
    And I check the expiry date of the confirmation page is correct
#    And I check the vts information appears on the confirmation page - commented out due to a known bug ticket 4513
    And I press the "Finish changes and print certificate" button

    Then The page title contains "Test Results Updated Successfully"
    And I check there is a "Print" link
    And I click "Print" and check the PDF contains:
      | VT20            |
      | {reg}           |
      | {vin}           |
#      | {siteName}      |  -- not included on new certs


  Scenario Outline: <user> user issues duplicate certificate
    Given I login without 2FA using "<dataScript>" as {<user>}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I search for certificates with reg {reg}
    And I click the first "View certificate" link
    And I check there is a "Print certificate" link
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate            |

  Examples:
  |user        |dataScript       |
  |AO1         |AO1_USER         |
  |DVLA Manager|DVLA_MANAGER_USER|

  Scenario: Tester completes a test then issues duplicate certificate and can edit test
    Given I load uniquely "VEHICLE_CLASS_4_BEFORE_2010" as {registration1}, {vin1}, {mileage1}
    And I login with 2FA using "MOT_TESTER_CLASS_4" as {username1}, {site}

    When I start an MOT test for {registration1}, {vin1}, {site}
    And The page title contains "Your home"
    And I click the "Enter test results" link

    And I enter an odometer reading in miles of {mileage1} plus 5000
    And I enter decelerometer results of service brake 51 and parking brake 16
    And I press the "Review test" button

    Then The page title contains "MOT test summary"
    And I check the test information section of the test summary is "Pass"
    And I check the vehicle summary section of the test summary has "Registration number" of {registration1}
    And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
    And I check the brake results section of the test summary is "Pass"
    And I check the fails section of the test summary has "None recorded"
    And I press the "Save test result" button
    And The page title contains "MOT test complete"
    And I click "Print documents" and check the PDF contains:
      | VT20            |
      | {registration1} |
      | {vin1}          |
#      | {site}          |  -- not included on new certs
    And I click the "Back to user home" link

    And I search for certificates with reg {registration1}
    And I click the first "View certificate" link
    And I check there is a "Print certificate" link
    And I press the "Edit this MOT test result" button
    And I update the odometer reading by 3000
    And I edit the primary colour "Red" and secondary colour "White"
    And I press the "Review changes" button
    And I check the odometer reading on the confirmation page is correct
    #  And I check the colours are correct "Red" and "White" - commented out due to a known bug ticket 4513
    And I press the "Finish changes and print certificate" button
    Then The page title contains "Test Results Updated Successfully"
    And I check there is a "Print" link
    And I click "Print" and check the PDF contains:
      | VT20                  |
      | {registration1}       |
      | {vin1}                |
#      | {site}                |  -- not included on new certs


  Scenario: Tester issues duplicate certificate and cannot edit another sites tests
    Given I login with 2FA using "MOT_TESTER_CLASS_4_WITH_OTHER_SITE_TEST" as {tester}, {site}, {reg}
    And I search for certificates with reg {reg}
    And I click the first "View certificate" link
    And I check there is a "Print certificate" link
    And I check there is no "Edit this MOT test result" button
    And I click "Print certificate" and check the PDF contains:
      | Duplicate certificate            |

  @smoke
  Scenario: AO1 user edits vehicle details on latest certificate
    Given I login without 2FA using "AO1_USER" as {AO1}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I search for certificates with reg {reg}
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
  #  And I check the colours are correct "Red" and "White" - commented out due to a known bug ticket 4513
    Then I press the "Finish changes and print certificate" button
    And The page title contains "Test Results Updated Successfully"
    And I check there is a "Print" link
    And I click "Print" and check the PDF contains:
      | R3GHAU5               |
      | VINR3V0L              |
      | FORD                  |
      | FOCUS                 |
