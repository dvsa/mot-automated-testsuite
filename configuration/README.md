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
Boolean for running in headless mode. If enabled, emulates a 1920x1080 browser window

## takeScreenshots
When to take screenshots of the browser window at the end of each test scenario.
Supported values are: 

* always
* onErrorOnly
* never

## outputHtml
When to save the raw HTML of the page reached at the end of each test scenario.
Supported values are: 

* always
* onErrorOnly
* never

## password
The password that all user accounts are set to in the test environment being used

## clickWait
The amount of time, in milliseconds, to wait for browser clicks to happen before starting to poll for the page refresh
and any dynamic javascript to complete. This is a mandatory delay, to accommodate any browser/environment/network
latency. If any tests fail and the screen shot shows the browser is still on the previous page, try increasing this
delay. Other factors like working remotely/over VPN/using slower environments (e.g. PP) can also affect this.
*values from 500ms to 1500ms are fairly reliable, larger values impact testsuite completion duration*

## pageWait
Maximum wait, in seconds, for each page refresh/submit to complete and any dynamic javascript to run, before using
Selenium to locate elements in the web page (because the testsuite polls refreshed pages for completion this time
delay will typically be much less). If a page fails to refresh/complete javascript by this time limit, the current
test will be failed as an error

## jdbc.url
The MariaDB/MySQL URL of the environment under test
 
## jdbc.username
The database username to use. Only read access is needed
  
## jdbc.password
The database password to use  

## maxLoginRetries
The number of times to try failed logins before failing the test scenario.
To accommodate changed user passwords, the testsuite tries again with another user.

## dataserverUrl
Base URL to use when connecting to the local dataserver