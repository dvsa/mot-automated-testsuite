# Using Test Data

## Data specified in the Gherkin Steps
 
Test data can be hard-coded into Gherkin steps using the ```".."``` syntax, for example:
 
```gherkin
And I check the test information section of the test summary is "Fail"
``` 

Test scenarios that only differ by hard-coded data used should be reformatted to use a `Scenario Outline` with the data specified in an `Examples` table.

## Data loaded from the database

Test data can also be loaded from the database, populating data keys using the ```{..}``` syntax, for example:
 
```gherkin
Given I load "VEHICLE_CLASS_4" as {registration1}, {vin1}, {mileage1}
``` 

This above step runs the ```VEHICLE_CLASS_4``` dataset query, which must return rows with 3 columns (of any name and type - it is simply treated as a string).

All such dataset queries are run when the step runs, and the results cached in memory. If no matching rows are found for a dataset then the testsuite is aborted as failed. Each time a test scenario `loads` a row from a dataset, that row is deleted from the cache - this ensures each test scenario gets a unique value allowing tests to be more independent. 

Data keys can then be used later in the test, for example:
  
```gherkin
And I check the vehicle summary section of the test summary has "VIN/Chassis number" of {vin1}
```  

Each dataset query is saved below the ```src/server/resources/queries``` folder, and is a plain text file of the format ```<dataset name>.sql```. The file can contain whitespace, newlines and SQL comments. 

When writing new SQL queries try to consider whether matching data exists in all test environments, and whether adding some extra functionality to your steps would greatly increase data available for testing (e.g. automatically clearing down special notices greatly increases the number of testers that can be used). 

Please also consider how SQL queries perform, and test your queries both on environments with small volumes of anonymised data (e.g. CI) and full-size production copies (e.g. PP). Using ```limit``` statements and tuning your SQL clauses will help this. You may also find some invalid data needs to be specifically excluded (e.g. ```null```-ed out fields, invalid registration numbers).

## Data Filtering

The testsuite has data filtering functionality (enabled by the ```dataFiltering``` configuration setting) that is intended to allow running tests in parallel, by filtering all data of a certain type (e.g. users, sites, vehicles...) to be globally unique (i.e. across all tests). 

Dataset ```.sql``` files can be saved in a sub-directory of ```src/server/resources/queries``` folder. When data filtering is enabled, all queries in a given sub-folder will be filtered for uniqueness with all other queries in the same sub-folder. Filtering uses the value of the first column in the query. 

The following filters have been setup so far:

| Name                | First Column |
|---------------------|--------------|
| authorised-examiner | ae_ref       |
| site                | site_number  |
| user                | username     |
| vehicle             | registration |

To add new filters, simply create another sub-folder and then add ```.sql``` files that have common data for the first result column. 

The data filtering is applied at runtime, so running different sets of tests will have different results. See the data usage report (```target/data-usage-report.html```) for details of what data was loaded, filtered out, requested by tests, and if the data was still short.

## Unique Data

Whilst the project is waiting to get sufficient data in all environments for running all tests in parallel to be feasible, the testsuite needs to be able to run either in serial or parallel.

As a temporary work-around, the following step definition has been added, to be used by any tests that require data to be unique (e.g. vehicles or sites of a certain status):

```gherkin
Given I load uniquely ...
``` 

If data filtering is enabled, these steps operate like ```I load ...``` - using caching and data filtering.
If data filtering is not enabled, these steps operate like ```I load immediately ...``` - loading data immediately.

Once sufficient data exists to use parallel/data filtering in all environment, these steps can be replaced with ```I load ...``` and some of the data server functionality cleaned up.

The steps in ```AuthenticationStepDefinitions``` also operate in the same manner, using caching and filtering if the data filtering setting is enabled, otherwise loading data immediately. 
    
    
## Data loaded immediately from the database

You may find in some rare scenarios that certain database queries need to be run immediately (at the point the test is running) instead of using cached data. 

An example would be any data that changes during the test, such as creating a new user account.

To load data immediately, use the following step syntax:

```gherkin
And I load immediately "LATEST_TEST_USER" as {username}
``` 

Please only use this functionality when really needed, as it is a hindrance to running tests in parallel. 
