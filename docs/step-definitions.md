# Step Definitions

A manually maintained summary of the steps defined in the fixture Java classes is given below.

In general the following syntax is used:

* Hard-coded test data is surrounded with ".."
* Test data loaded from the database is surrounded with {..}
 
## Generic

These steps are completely generic to any web application.

### WebStepDefinitions

High level:

* I browse to ```URL```
* I press the ```"..buton text.."``` button
* I click the ```"..link text.."``` link
* I enter ```"..text.."``` in the ```"..label.."``` field
* I enter ```{key}``` in the ```"..label.."``` field
* I select ```"..item.."``` in the ```"..label.."``` field
* The page title contains ```"..text.."```
* I check there is a ```"..link text.."``` link
* I check there is no ```"..link text.."```
* I check the ```"..link text.."``` link is disabled
* I check the ```"..link text.."``` link is enabled
* I check the ```"..button text.."``` button is disabled
* I check the ```"..button text.."``` button is enabled
* I click the first ```"..link text.."``` link

Lower level, only use if higher level steps can't be used:

* I enter ```"..text.."``` in the field with id ```"..id.."```


### DataStepDefinitions

* I load ```"..dataset name.."``` as ```{key1}```
* I load ```"..dataset name.."``` as ```{key1}```, ```{key2}```
* I load ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```
* I load ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```, ```{key4}```

## MOT

These steps are specific to the DVSA MOT application screens.

### AuthenticationStepDefinitions

* I login with 2FA using ```"..dataset name.."``` as ```{..username key..}```, ```{..site key..}```
* I login without 2FA using ```"..dataset name.."``` as ```{..username key..}```

### CreateVehicleStepDefinitions

### DuplicateAndReplacementStepDefinitions

### TesterDoesStepDefinitions

* I enter an odometer reading in miles of ```{..key..}``` plus ```..amount..```
* I enter an odometer reading in kilometres of ```..amount..```
* I enter odometer not present
* I click the "Aborted by VE" radio button
* I click the "Inspection may be dangerous or cause damage" radio button
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
* I edit the ```"Failure or PRS or Advisory"``` defect of ```"..defect.."``` with comment ``"..comment.."``` and not dangerous
* I edit the ```"Failure or PRS or Advisory"``` defect of ```"..defect.."``` with comment ``"..comment.."``` and is dangerous
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