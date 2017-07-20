@regression
Feature: 08 - duplicate and replacement certificates

  @smoke
  Scenario: AO1 edits non-vehicle details on certificate
    Given I login without 2FA using "AO1_USER" as {AO1}
    And I load "VEHICLE_CLASS_4_NOT_UPDATED_TODAY" as {reg}, {vin}, {mileage}
    And I load "SITE" as {siteName}, {siteNumber}
    And I search for certificates with reg {reg}
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

  Scenario: Tester issues duplicate certificate and cannot edit historic
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {tester}, {site}
    And I load "VEHICLE_CLASS_4_HISTORIC_10_DAYS" as {reg}, {vin}, {mileage}
    And I search for certificates with reg {reg}
    And I click the first "View certificate" link
    And I check there is a "Print certificate" link
    And I check there is no "Edit this MOT test result" button


  @smoke
  Scenario: AO1 user edits vehicle details on latest certificate
    Given I login without 2FA using "AO1_USER" as {AO1}
    And I load "VEHICLE_CLASS_4_NOT_UPDATED_TODAY" as {reg}, {vin}, {mileage}
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

  Scenario: DVLA Manager edits VRM / VIN on latest certificate
    Given I login without 2FA using "DVLA_MANAGER_USER" as {DVLAManger}
    And I load "VEHICLE_CLASS_4_NOT_UPDATED_TODAY" as {reg}, {vin}, {mileage}
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

  #Scenario: Tester changes odometer and vehicle colour on latest certificate
    #Given I login as {tester1}
    #And I click the "Replacement / Duplicate certificate" link
    #And I search for certificates with {reg}
    #And I select the first "View certificate" link
    #And I click the "Edit this MOT test result" button
    #And I edit the odometer reading 5000
    #And I edit the vehicle colour "Colour1", "Colour2"
    #And I enter a reason for replacement "differences in tests"
    #When I submit the changes
    #And I check a new vehicle version is created
    #Then a replacement certificate should be created
    #And I check the pdf details are correct

  #Scenario: AO1 edits vehicle details on history certificate
    #Given I login as {AO1}
    #And I click the "Replacement / Duplicate certificate" link
    #And I search for certificates with {reg}
    #And I click the last "View certificate" link
    #And I click the "Edit this MOT test result" button
    #And I select a new make {make}
    #And I select a new model {model}
    #And I edit the vehicle colour "Colour1", "Colour2"
    #And I edit the registration "NewReg"
    #And I edit the VIN "NewReg"
    #And I enter a reason for replacement "Reason"
    #When I submit the changes
    #And I check a new vehicle version is created
    #Then a replacement certificate should be created
    #And I check the pdf details are correct

  #Scenario: DVLA Manager edits VRM / VIN on latest certificate
    #Given I login as {DVLA manager}
    #And I click the "Replacement / Duplicate certificate" link
    #And I search for certificates with {reg}
    #And I click the first "View Certificate" link
    #And I click the "Edit this MOT test result" button
    #And I change the registration to "NewReg"
    #And I change the Vin to {vin}
    #And I enter a reason for replacement
    #When I submit the changes
    #And I check a new vehicle version is created
    #Then a replacement certificate should be created
    #And I check the pdf details are correct

  #Scenario: DVLA Manager edits VRM / VIN on historic certificate
    #Given I login as {DVLA manager}
    #And I click the "Replacement / Duplicate certificate" link
    #And I search for certificates with {reg}
    #And I click the last "View Certificate" link
    #And I click the "Edit this MOT test result" button
    #And I change the registration to "NewReg"
    #And I change the Vin to {vin}
    #And I enter a reason for replacement
    #When I submit the changes
    #And I check a new vehicle version is created
    #Then a replacement certificate should be created
    #And I check the pdf details are correct