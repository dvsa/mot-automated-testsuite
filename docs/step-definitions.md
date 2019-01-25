# Step Definitions

A manually maintained summary of the steps defined in the fixture Java classes is given below.

In general the following syntax is used:

* Hard-coded test data is surrounded with ```".."```
* Test data loaded from the database is surrounded with ```{..}```

## Generic

These steps are completely generic to any web application.

### WebStepDefinitions

Note, in the steps below:
* ```"..text with {key}s.."``` can contain:
  * Hard-coded text, possibly with data keys (```{key}```), which will be replaced by the data value
  * Single-quotes (```'```), but **not** double-quotes (```"```)
  
High level:

* I browse to ```URL```
* I press the ```"..button text.."``` button
* I click the button which contains text ```"..button text.."```
   * Note: this finds the text element then clicks the button that is its ancestor. For use with nested tags in buttons.
* I click the button with class name ```"..class name.."```
   * Note: this should only be used when this is the only way to identify the button
* I click the ```..text with {key}s..``` link
* I click the ```{key}``` link
* I click the first ```"..text with {key}s.."``` link
* I click the first ```{key}``` link
* I click the last ```"..text with {key}s.."``` link
* I click the last ```{key}``` link
* I click the ```..text..``` link for the ```..heading text..``` field row
   * Note: expects a link in a ```td``` element in the same row as a ```th``` element with the heading text
* I click the ```..name..``` icon
   * Note: expects an ```i``` element with a Font Awesome class of the form ```fa-{..name..}```
* I accept the alert popup
* I dismiss the alert popup 
* I click the ```..label..``` radio button
* I click the ```{key}``` radio button
* I click the ```..label..``` radio button in fieldset ```..legend..```
* I click the ```{key}``` radio button in fieldset ```..legend..```
* I click the ```..label..``` radio button in fieldset ```..nested legend..``` in fieldset ```..legend..```
* I click the ```{key}``` radio button in fieldset ```..nested legend..``` in fieldset ```..legend..```
   * Note: all radio button steps handle labels with ```for``` and radio inputs nested inside the label 
* I click the ```..label..``` checkbox   
   * Note: ensures field is set - clicks the checkbox if not already selected, otherwise does nothing
* I clear the ```..label..``` checkbox   
   * Note: all checkbox steps handle labels with ```for``` and checkbox inputs nested inside the label 
   * Note: ensures field is not set - clicks the checkbox if already selected, otherwise does nothing   
* I enter ```"..text.."``` in the ```"..label.."``` field
* I enter ```"..text.."``` in the ```{..label key..}``` field
* I enter ```{key}``` in the ```"..label.."``` field
* I enter ```{key}``` in the ```{..label key..}``` field
* I enter ```"..text.."``` in the ```"..field label.."``` field in fieldset ```..fieldset label..```
* I enter ```{key}``` in the ```"..field label.."``` field in fieldset ```..fieldset label..```
* I select ```"..item.."``` in the ```"..label.."``` field
* The page contains ```"..text with {key}s.."```
* The page contains ```"..text with {key}s.."``` or ```"..text with {key}s.."```
* The page does not contain ```"..text with {key}s.."```
* The page title contains ```"..text.."```
* I check there is a ```"..text with {key}s.."``` link
* I check there is no ```"..text with {key}s.."``` link
* I check the ```"..button text.."``` button is disabled
* I check the ```"..button text.."``` button is enabled
* I check there is no ```"..button text.."``` button
* I check the table with heading ```"..text.."``` has at least ```..num..``` rows
   * Note: counts the number of table rows, excluding the heading row
* I check the ```"..heading text.."``` field row has value ```"..value.."```
* I check the ```"..heading text.."``` field row has value ```{key}```
   * Note: these steps check the contents of a ```td``` element in the same row as a ```th``` element with the heading text 
* I check the ```"..heading text.."``` field column has value ```"..value.."```
* I check the ```"..heading text.."``` field column has value ```{key}```
   * Note: these steps check the contents of the ```td``` element in the first row and same column as a ```th``` element with the heading text    
* I check the alert popup contains ```"..text.."```
* I check the ```"..name.."``` cookie is set
* I delete the ```"..name.."``` cookie
   * Note: allows partial match of the cookie name
* I wait for ```"..number.."``` seconds
* I set the starting url key as ```"..starting url key.."```

Lower level, only use if higher level steps can't be used:

* I enter ```"..text.."``` in the field with id ```"..id.."```
* I enter ```{key}``` in the field with id ```"..id.."```
* I select ```"..text.."``` in the field with id ```"..id.."```
* I select ```{key}``` in the field with id ```"..id.."```


### DataStepDefinitions

* I load ```"..dataset name.."``` as ```{key1}```
* I load ```"..dataset name.."``` as ```{key1}```, ```{key2}```
* I load ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```
* I load ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```, ```{key4}```
* I load ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```, ```{key4}```, ```{key5}```
* I load ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```, ```{key4}```, ```{key5}```, ```{key6}```
    * Note: these steps will load the next entry from the cached dataset. Each dataset is read at the start of the testsuite execution
    * Note: if data filtering (parallel execution) is on, each entry will be unique across all tests
    * Note: this is the recommended way to load data
* I load immediately ```"..dataset name.."``` as ```{key1}```
* I load immediately ```"..dataset name.."``` as ```{key1}```, ```{key2}```
* I load immediately ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```
* I load immediately ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```, ```{key4}```
* I load immediately ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```, ```{key4}```, ```{key5}```
* I load immediately ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```, ```{key4}```, ```{key5}```, ```{key6}```
    * Note: these steps will load the first entry in the dataset immediately without any caching
    * Note: this should only be used for data that changes during the test itself
* I load uniquely ```"..dataset name.."``` as ```{key1}```
* I load uniquely ```"..dataset name.."``` as ```{key1}```, ```{key2}```
* I load uniquely ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```
* I load uniquely ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```, ```{key4}```
* I load uniquely ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```, ```{key4}```, ```{key5}```
* I load uniquely ```"..dataset name.."``` as ```{key1}```, ```{key2}```, ```{key3}```, ```{key4}```, ```{key5}```, ```{key6}```
    * Note: if the testsuite is in parallel mode, these steps operate like ```I load ...``` (using caching and data filtering)
    * Note: if not in parallel mode, these steps operate like ```I load immediately ...``` (loading immediately) 
    * Note: use this as a temporary work-around for data that can be affected/invalidated by other tests
    * Note: once sufficient data exists to use parallel/data filtering in all environment, replace with ```I load ...```    
* I set today as ```{..day key..}```, ```{..month key..}```, ```{..year key..}```  
    * Note: this sets the current date (day/month/year as integers)
* I set today formatted using ```"..date time format string.."``` as ```{key}```
    * Note: supports ```java.time.format.DateTimeFormatter``` format strings 
    (see [DateTimeFormatter JavaDoc](http://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html)) 
    
    
## MOT

These steps are specific to the DVSA MOT application screens.

### AedmStepDefinitions

* I check the organisation role assignment confirmation message for ```{..username key..}```, ```{..name key..}```
* I check the site test log has the recent test ```{..registration key..}```, ```{..username key..}```
* I click the ```{..site name key..}``` site link with status ```"..status.."``` on the service reports$
* I check the TQI report has the title ```{..site name key..}```

### AuthenticationStepDefinitions

* I login with 2FA using ```"..dataset name.."``` as ```{..username key..}```, ```{key2}```
* I login with 2FA using ```"..dataset name.."``` as ```{..username key..}```, ```{key2}```, ```{key3}```
* I login with 2FA using ```"..dataset name.."``` as ```{..username key..}```, ```{key2}```, ```{key3}```, ```{key4}```
* I login with 2FA using ```"..dataset name.."``` as ```{..username key..}```, ```{key2}```, ```{key3}```, ```{key4}```, ```{key5}```
* I login with 2FA using ```"..dataset name.."``` as ```{..username key..}```, ```{key2}```, ```{key3}```, ```{key4}```, ```{key5}```, ```{key6}```
   * Note: these steps will load the first entry in the dataset immediately without any caching
   * Note: these steps will use the [configuration settings](../configuration/README.md) and actually try up to ```maxLoginRetries``` different users, if the password or PIN is rejected 
   * Note: these steps will also acknowledge any special notices (so the user can undertake tests, if needed)
* I login with 2FA as ```{..username key..}```
   * Note: this step will also acknowledge any special notices (so the user can undertake tests, if needed)
* I login without 2FA using ```"..dataset name.."``` as ```{..username key..}```
   * Note: this step will load the first entry in the dataset immediately without any caching
* I login without 2FA as ```{..username key..}```
* I login with 2FA and drift ```..drift period..``` using ```"..dataset name.."``` as ```{..username key..}```, ```{..last drift key..}```, ```{key3}```, ```{key4}```
* I generate 2FA PIN with drift ```..drift period..``` as ```{key}```
   * Note: ```drift period``` is of the format ```+/-<n>``` (e.g. +2, +0, -10)
* I generate 2FA PIN with previous drift ```{..last drift key..}``` as ```{key}```  
* I login and click ```"..journey type.."``` card using ```"..dataset name.."``` as ```{..username key..}```, ```{key2}```, ```{key3}```, ```{key4}```


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

### FinanceStepDefinitions

* I click the link ```"..name.."``` with id ```"..id.."```
* I enter the last 8 characters of ```..paymentRef..``` in the field with id ```"..id.."```
* And I click ```"..text.."``` and check the CSV contains:
* I wait ``"..number.."``` seconds then ``"..refresh.."``` the page until ```"..text.."``` button displays

### HomePageStepDefinitions

* I click the organisation link ```{..organisationKey..}```
* I get the slot count from the homepage for site ```{..siteKey..}```
* I check a slot was successfully used for site ```{..siteKey..}```
* I check a slot was not used for site ```{..siteKey..}```
* I get the slot count for organisation ```{..organisationKey..}```
* I check a slot was not used for organisation ```{..organisationKey..}```

### PurchaseSlotsStepDefinitions

* I order ```..amount..``` slots
* I enter card details from csv ```"..csvName.."```
* I enter the card holders name as ```"..name.."```
* I make the payment for card from csv ```"..csvName.."```
* I make an orphan payment for card from csv ```"..csvName.."```
* I check that ```..amount..``` slots were bought successfully

### SiteAdminStepDefinitions

* I click on the ```{..site name key..}```, ```{..site number key..}``` site
* I check the role summary has a new role of ```"..text.."```
* I check there is a role assignment confirmation message for ```{..username key..}```, ```{..name key..}```
* I check there is pending ```"..text.."``` role listed for ```{..name key..}```
* I check the VTS default for ```..brake type..``` is ```{..test type key..}```
* I choose different brake defaults for ```{..starting Group A Brake Default..}```, ```{..starting Group B Service Brake Default..}```, ```{..starting Group B Parking Brake Default..}```, ```{..new Group A Brake Default..}```, ```{..new Group B Service Brake Default..}```, ```{..new Group B Parking Brake Default..}```

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
* I enter the current time for the contingency test
* I search for defect ```"..defect.."``` and open the ```"..manualLinkText.."``` manual link, I expect the ```".manualPageTitle."``` manual page
* I record the MOT test number

### VehicleExaminerStepDefinitions

* I search for an mot by ```"..search type.."```` with ```{searchKey}```
* I search for an mot by ```"..search type.."```` with ```{searchKey}``` from ```..amount..``` months ago
* I start a ```"..testType.."```
* I search for vehicle information by ```"..searchType.."``` with ```{searchKey}```
* I perform a test comparison with outcome ````"..outcome.."``` and justification ```"..justification.."```
* I check the case outcome ```"..expectedOutcome.."```` is saved
* I click the view certificate link for test number ```{testNumberKey}```
* I search for AE information with ```{aeReferenceKey}```
* I check the AE name is ```{aeNameKey}```
* I search for Site information by site number with ```{siteNumberKey}```
* I check the site name is ```{siteNameKey}```
* I search for user with username ```{usernameKey}```
* I check the slot usage for the past ```..amount..``` days is ```{slotsKey}```
* I abort the active mot test at site for reg ```{regKey}```, vin ```{vinKy}```
* I check the reg ```{regKey}```, vin ```{vinKey}``` on vehicle information
* I click the first name in the list
* I check the user profile contains username ```{usernameKey}```
* I change the testers group ```"..group.."``` status to ```"..status.."```

### MothStepDefinitions

* I enter ```"..text.."``` in the registration field
* I enter ```{key}``` in the registration field
* I click the last text ```..text..```
* I go to the next tab
* I close extra tabs
* I click the accordion section with the id ```..text..```
* I click the ```..text..``` help link

### ElasticSearchStepDefinitons

* I query elastic search with ```"..csv file name.."``` as ```"..data key.."```
* I compare the search results for ```"..result set key.."``` and ```"..result set key.."``` with data ```"..csv data file.."```
