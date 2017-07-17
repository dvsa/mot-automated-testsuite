@regression
Feature: 8 - duplicate and replacement certificates

  @smoke
  Scenario: AO1 edits non-vehicle details on certificate
    Given I login without 2FA using "AO1_USER" as {AO1}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
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

  Scenario: Tester issues duplicate certificate
    Given I login with 2FA using "MOT_TESTER_CLASS_4" as {tester}, {site}
    And I load "VEHICLE_CLASS_4" as {reg}, {vin}, {mileage}
    And I search for certificates with reg {reg}
    And I click the first "View certificate" link
    And I check there is a "Print certificate" link

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

  #Scenario: Tester cannot change details of historic test
    #Given I login as {tester1}
    #And I click the "Replacement / Duplicate certificate" link
    #And I search for certificates with {reg}
    #And I select the last "View certificate" link
    #Then I should not see the edit certificate button

  #Scenario: AO1 edits vehicle details on latest certificate
    #Given I login as {AO1}
    #And I click the "Replacement / Duplicate certificate" link
    #And I search for certificates with {reg}
    #And I click the first "View certificate" link
    #And I click the "Edit this MOT test result" button
    #And I select a new make {make}
    #And I select a new model {model}
    #And I edit the vehicle colour "Colour1", "Colour2"
    #And I edit the registration "NewReg"
    #And I edit the VIN "NewReg"
    #And I enter a reason for replacement "Reason"
    #When I submit the changes
    #And I check a new vehicle version is not created
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