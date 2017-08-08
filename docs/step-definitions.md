# Step Definitions

A manually maintained summary of the steps defined in the fixture Java classes is given below.

In general the following syntax is used:

* Hard-coded test data is surrounded with ```".."```
* Test data loaded from the database is surrounded with ```{..}```

## Generic

These steps are completely generic to any web application.

### WebStepDefinitions

High level:

* I browse to ```URL```
* I press the ```"..buton text.."``` button
* I click the ```"..link text.."``` link
* I click the ```{key}``` link
* I click the first ```"..link text.."``` link
* I click the ```..label...``` radio button
* I click the ```..label...``` radio button in fieldset ```..legend...```
   * Note: all radio button steps handle labels with ```for``` and radio inputs nested inside the label 
* I click the ```..label...``` checkbox   
   * Note: all checkbox steps handle labels with ```for``` and checkbox inputs nested inside the label 
* I enter ```"..text.."``` in the ```"..label.."``` field
* I enter ```{key}``` in the ```"..label.."``` field
* I select ```"..item.."``` in the ```"..label.."``` field
* The page contains ```"..text.."```
* The page title contains ```"..text.."```
* I check there is a ```"..link text.."``` link
* I check there is no ```"..link text.."```
* I check the ```"..button text.."``` button is disabled
* I check the ```"..button text.."``` button is enabled

Lower level, only use if higher level steps can't be used:

* I enter ```"..text.."``` in the field with id ```"..id.."```
* I enter ```{key}``` in the field with id ```"..id.."```


### DataStepDefinitions

* I load ```"..dataset name.."``` as ```{key1}```
* I load ```"..dataset name.."``` as ```{key1}```, ```{key2}```
* I load ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```
* I load ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```, ```{key4}```

## MOT

These steps are specific to the DVSA MOT application screens.

### AuthenticationStepDefinitions

* I login with 2FA using ```"..dataset name.."``` as ```{..username key..}```, ```{key2}```
* I login with 2FA using ```"..dataset name.."``` as ```{..username key..}```, ```{key2}```, ```{key3}```
* I login with 2FA using ```"..dataset name.."``` as ```{..username key..}```, ```{key2}```, ```{key3}```, ```{key4}```
* I login with 2FA using ```"..dataset name.."``` as ```{..username key..}```, ```{key2}```, ```{key3}```, ```{key4}```, ```{key5}```
   * Note: these steps will use the [configuration settings](../configuration/README.md) and actually try up to ```maxLoginRetries``` different users, if the password isn't ```password``` (e.g. for users that have had passwords manually changed)
   * Note: these steps will also acknowledge any special notices (so the user can undertake tests, if needed)
* I login with 2FA as ```{..username key..}```
   * Note: this step will also acknowledge any special notices (so the user can undertake tests, if needed)
* I login without 2FA using ```"..dataset name.."``` as ```{..username key..}```

### CreateVehicleStepDefinitions

* I enter the date of first use as today minus ```..amount..``` years
* I select Vehicle Class ```..class..```
* I enter reg ```"..reg.."``` and vin ```"..vin..""```
* I select make ```"..make.."``` and model ```"..model.."```
* I select primary colour ```"..colour1.."``` and secondary colour ```"..colour2.."```
* I select fuel type ```"..fuel.."``` and cylinder capacity ```..capacity..```
* I select country of registration ```"..country.."```
* I cancel the mot test after creating the vehicle
* I check the registration ```"..reg.."``` and vin ```"..vin.."``` is correct
* I check the make ```"..make.."``` and model ```"..model.."``` is correct
* I check the fuel type ```"..fuel.."``` and cylinder capacity ```..capacity..``` is correct
* I check the vehicle class ```"..class.."``` is correct
* I check the primary colour ```"..colour1.."``` and secondary colour ```"..colour2.."``` is correct
* I check the date of first use from ```..years..``` years ago is correct
* I check the country of registration ```"..country.."``` is correct

### DuplicateAndReplacementStepDefinitions

* I search for certificates with reg ```{..regKey..}```
* I search for certificates with vin ```{..vinKey..}```
* I update the odometer reading by ```..reading..```
* I update the testing location to ```{..locationKey..}```, ```{..nameKey..}```
* I update the expiry date by adding ```..days..``` days
* I submit the certificate changes
* I check the odometer reading on the confirmation page is correct
* I check the expiry date of the confirmation page is correct
* I check the vts information appears on the confirmation page

### HomePageStepDefinitions

* I click the organisation link ```{..organisationKey..}```
* I get the slot count from the homepage for site ```{..siteKey..}```
* I check a slot was successfully used for site ```{..siteKey..}```

### PurchaseSlotsStepDefinitions

* I order ```..amount..``` slots
* I enter the card details ```"..cardNumber.."```, ```"..expiryDate.."```, ```"..securityCode.."```
* I enter the card holders name as ```"..name.."```
* I make the payment for card ```"..cardNumber.."```
* I check that ```..amount..``` slots were bought successfully

### TesterDoesStepDefinitions

* I enter an odometer reading in miles of ```{..key..}``` plus ```..amount..```
* I enter an odometer reading in kilometres of ```..amount..```
* I enter odometer not present
* The MOT status is ```"..Pass or Fail.."```
* I start an MOT test for ```{..reg key..}```, ```{..vin key..}```
* I start an MOT retest for ```{..reg key..}```, ```{..vin key..}```
* I start an MOT test for DVLA vehicle ```{..reg key..}```, ```{..vin key..}```
* I start an MOT test for ```{..reg key..}```, ```{..vin key..}``` with colour changed to ```"..colour.."```
* I start an MOT test for ```{..reg key..}```, ```{..vin key..}``` with engine changed to ```"..fuel type.."```  with capacity ```..cc's..```
* I browse for a ```"Failure or PRS or Advisory"``` defect of (```"..category.."```, ```"..defect.."```) with comment ```"..comment.."```
* I browse for a ```"Failure or PRS or Advisory"``` defect of (```"..category.."```, ```"..sub-category.."```, ```"..defect.."```) with comment ```"..comment.."```
* I search for a ```"Failure or PRS or Advisory"``` defect of ```"..category.."``` with comment ```"..comment.."```
* I add a manual advisory of ```"..defect.."```
* I edit the ```"Failure or PRS or Advisory"``` defect of ```"..defect.."``` with comment ```"..comment.."``` and not dangerous
* I edit the ```"Failure or PRS or Advisory"``` defect of ```"..defect.."``` with comment ```"..comment.."``` and is dangerous
* I remove the ```"Failure or PRS or Advisory"``` defect of ```"..defect.."```
* I enter decelerometer results of efficiency ```..amount..```
* I enter decelerometer results of service brake ```..amount..``` and parking brake ```..amount..```
* I edit decelerometer results of service brake ```..amount..``` and parking brake ```..amount..```
* I enter decelerometer service brake result of ```..amount..``` and gradient parking brake result of ```"Pass or Fail"```
* I mark the defect ```"..defect.."``` as repaired
* I search for a vehicle with ```"..reg.."```, ```"..vin.."```
* I check the "Add brake test" link is hidden
* I check the vehicle summary section of the test summary has ```"..field label.."``` of ```{key}```
* I check the vehicle summary section of the test summary has ```"..field label.."``` of ```"..text.."```
* I check the test information section of the test summary is ```"Pass or Fail"```
* I check the brake results section of the test summary is ```"Pass or Fail"```
* I check the fails section of the test summary has ```"..text.."```
* I check the prs section of the test summary has ```"..text.."```
* I check the advisory section of the test summary has ```"..text.."```
* I check the fails section of the test summary does not have ```"..text.."```
* I check the prs section of the test summary does not have ```"..text.."```
* I check the advisory section of the test summary does not have ```"..text.."```


### SiteAdminStepDefinitions

* I click on the ```{..site name key..}```, ```{..site number key..}``` site
* I check the role summary has a new role of ```"..text.."```
* I check there is a role assignment confirmation message for ```{..username key..}```, ```{..name key..}```
* I check there is pending ```"..text.."``` role listed for ```{..name key..}```
