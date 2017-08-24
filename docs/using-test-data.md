# Using Test Data

## Data specified in the Gherkin Steps
 
Test data can be hard-coded into Gherkin steps using the ```".."``` syntax, for example:
 
```gherkin
And I check the test information section of the test summary is "Fail"
``` 

Test scenarios that only differ by hard-coded data used should be reformatted to use a `Scenario Outline` with the data specified in an `Examples` table.

## Data cached from the database

Test data can also be loaded from the database, populating data keys using the ```{..}``` syntax, for example:
 
```gherkin
Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
``` 

This above step runs the ```VEHICLE_CLASS_4``` dataset query, which must return rows with 3 columns (of any name and type - it is simply treated as a string).

All such dataset queries are run before the first test scenario runs, and the results cached in memory. If no matching rows are found for a dataset then testsuite is aborted as failed. Each time a test scenario `loads` a row from a dataset, that row is deleted from the cache - this ensures each test scenario gets a unique value allowing tests to be more independent. 

Data keys can then be used later in the test, for example:
  
```gherkin
And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
```  

Each dataset query is saved in the ```src/test/queries``` folder, and is a plain text file of the format ```<dataset name>.sql```. The file can contain whitespace, newlines and SQL comments. 

When writing new SQL queries try to consider whether matching data exists in all test environments, and whether adding some extra functionality to your steps would greatly increase data available for testing (e.g. automatically clearing down special notices greatly increases the number of testers that can be used). 

Please also consider how SQL queries perform, and test your queries both on environments with small volumes of anonymised data and full-size production copies. Using ```limit``` statements and tuning your SQL clauses will help this. You may also find some invalid data needs to be specifically excluded (e.g. ```null```-ed out fields, invalid registration numbers).

## Data loaded immediately from the database

You may find in some rare scenarios that certain database queries need to be run immediately (at the point the test is running) instead of before the first test scenario runs. 

An example would be to identify a vehicle that hasn't been tested today, as you don't want to include any vehicles also being tested in the current testsuite run (picked up in several other dataset queries).

To do this, use the following step syntax:

```gherkin
Given I load immediately "VEHICLE_CLASS_4_NOT_UPDATED_TODAY" as {reg}, {vin}, {mileage}
``` 

Please only use this functionality when really needed, as it will be a hindrance to parallelising the test suite in the future. 
