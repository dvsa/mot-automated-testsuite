@regression
Feature: 8 - duplicate and replacement certificates

  #Scenario Outline: Tester / DVLA Manager / AO1 user issues duplicate certificate
    #Given I login as <user>
    #And I click the "Replacement / Duplicate certificate" link
    #And I search for certificates with {reg}
    #And I click the first "view certificate" link
    #Then I should see the "Print certificate" button
    #And I check the pdf details are correct

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

  #Scenario: AO1 edits non-vehicle details on certificate
    #Given I login as {AO1}
    #And I click the "Replacement / Duplicate certificate" link
    #And I search for certificates with {reg}
    #And I click the first "View certificate" link
    #And I click the "Edit this MOT test result" button
    #And I edit the odometer reading
    #And I edit the testing location {newLocation}
    #And I edit the Expiry date {newDate}
    #And I edit the Country of Registration {countryOfRegistration}
    #And I enter a reason for replacement "Reason"
    #When I submit the changes
    #And I check a new vehicle version is created
    #Then a replacement certificate should be created
    #And I check the pdf details are correct

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