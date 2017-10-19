@regression
Feature: 06a - duplicate and replacement certificates

  @smoke
  Scenario: AO1 edits non-vehicle details on certificate
    Given I login without 2FA using "AO1_USER" as {AO1}
    And I load immediately "VEHICLE_CLASS_4_WITH_MOT" as {reg}, {vin}, {mileage}
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


  Scenario Outline: <user> user issues duplicate certificate
    Given I login without 2FA using "<dataScript>" as {<user>}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I search for certificates with reg {reg}
    And I click the first "View certificate" link
    And I check there is a "Print certificate" link

  Examples:
  |user        |dataScript       |
  |AO1         |AO1_USER         |
  |DVLA Manager|DVLA_MANAGER_USER|

  Scenario: Tester issues duplicate certificate and can edit test in last 10 days
    Given I login with 2FA using "MOT_TESTER_CLASS_4_WITH_RECENT_TEST" as {tester}, {site}, {reg}, {mileage}
    And I search for certificates with reg {reg}
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

  Scenario: Tester issues duplicate certificate and cannot edit another sites tests
    Given I login with 2FA using "MOT_TESTER_CLASS_4_WITH_OTHER_SITE_TEST" as {tester}, {site}, {reg}
    And I search for certificates with reg {reg}
    And I click the first "View certificate" link
    And I check there is a "Print certificate" link
    And I check there is no "Edit this MOT test result" button

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
