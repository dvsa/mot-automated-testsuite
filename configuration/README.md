# Configuration Settings

This directory is where your copy of ```testsuite.properties``` must be saved. 

**DO NOT CHECK ```testsuite.properties``` into GitHub** as it contains passwords, encryption seeds etc used on the internal test environment.

The required settings are listed below:
 
## startingUrl
Defines which environment will be used.
Used as the home URL in tests, don't include a trailing slash 

## seed
The seed used by OTP Pin Generator in all test environments

## browser
The browser for Selenium to use.
Supported values are: 

* chrome

## driver
The Selenium browser driver binary to use. See ```drivers\readme.txt``` for details of what has been downloaded and checked in.
Supported values are:

* drivers/mac-chromedriver
* driver/linux-chromedriver

## headless
Boolean for running in headless mode

## takeScreenshots
When to take screenshots of the browser window at the end of each test scenario.
Supported values are: 

* always
* onErrorOnly
* never

## password
The password that all user accounts are set to in the test environment being used

## pageWait
The maximum wait, in seconds, for each page refresh/submit to complete and any dynamic
javascript to run, before using Selenium to locate elements in the web page

## jdbc.url
The MariaDB/MySQL URL of the environment under test
 
## jdbc.username
The database username to use. Only read access is needed
  
## jdbc.password
The database password to use  

## maxLoginRetries
The number of times to try failed logins before failing the test scenario.
To accommodate changed user passwords, the testsuite tries again with another user.
