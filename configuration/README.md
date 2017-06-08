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

## headless
Boolean for running in headless mode

## takeScreenshots
When to take screenshots of the browser window at the end of each test scenario
Supported values are: 

* always
* onErrorOnly

## password
The password that all user accounts are set to in the test environment being used
