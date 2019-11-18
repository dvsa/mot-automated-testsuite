# MOT Automated Test Suite

Using Cucumber-JVM and Selenium to test the DVSA MOT application through the web browser UI, with test fixtures
written using Java (JDK 8 Lambda-format step definitions).

These tests are written as Gherkin features, in the *src/main/resources/features* directory.

Test data is loaded from the environment under test, using queries in the *src/server/resources/queries* directory.

## Pre-requisites
Checkout the ```mot/2fa-pin-generator``` project into a directory alongside the top level directory of this project, and build it as follows:
```
./gradlew build
```

Get a copy of ```testsuite.properties``` from another member of the team and save it in the ```configuration``` directory. See [Configuration Settings](configuration/README.md) for further details.

Get a copy of the current [Chromedriver](https://sites.google.com/a/chromium.org/chromedriver/) that matches the installed version of chrome and copy this to the ```drivers``` directory. This also needs to be referenced in the ```testsuite.properties``` file.

## How to run
To run all scenarios (in serial) within one specific feature, run the following at the command line. The filename can include any sub-directories below the ```features``` directory (e.g. ```sub-dir/example.feature```):

```
./gradlew cucumber-feature '-Pfeature=<the .feature file>'
```

To run all features (in serial) with a specific tag, run the following at the command line. To specify multiple tags, use a comma-separated list (e.g. ```@tag1,@tag2```):

```
./gradlew cucumber-tag '-Ptag=<tag>'
```

To run all features (in parallel) with a specific tag, run the following at the command line. To specify multiple tags, use a comma-separated list (e.g. ```@tag1,@tag2```):

```
./gradlew parallel '-Ptag=<tag>'
```

To run a specific scenario within one feature, run the following at the command line, The scenario name can be a regex (e.g. ```-Pscenario=.*aborts.*```):

```
./gradlew cucumber-scenario '-Pfeature=<the .feature file>' '-Pscenario=<the scenario regex>'
```

To rerun failed tests from the last run (identified by target/rerun.txt):

```
./gradlew cucumber-rerun
```

## Feature Tags
The following tags are used:

MOT:
* **@regression** = Theme 1 regression tests
* **@smoke** = Smoke tests
* **@cpms** = CPMS regression tests
* **@elasticSearch** = Elastic Search comparison test suite
* **@extended** = Extended regression tests
* **@regressiondata** = Generate data for manual checking
* **@VEinspection** = Vehicle Examiner Inspection test suite
* **@OpenInterfaceTests** = Covers MOT tests for OpenIF
* **@accessibility** = Runs the AXE accessibility checks output to `target/accessibility`. Note you must run `npm install` first to install the necessary axe libraries.

MOT History:
* **@mothint** = MOT History regression tests on the INT Environment
* **@mothpp** = MOT History regression tests on the PP Environment
* **@mothrecall** = MOT History Recall only regression tests
* **@mothhgv** = MOT History HGV/PSV only regression tests (trailers excluded)
* **@mothtrailers** = MOT History HGV/PSV only regression tests for trailers
* **@mothprint** = MOT certificate printing only regression tests
* **@mothaccessibility** = Runs the AXE accessibility checks output to `target/accessibility`. Note you must run `npm install` first to install the necessary axe libraries.

MOT Reminders:
* **@motrint** = MOT Reminders regression tests on the INT Environment
* **@motrdemo** = MOT Reminders regression tests on the DEMO Environment
* **@motrpp** = MOT Reminders regression tests on the PP Environment

## Reports
Reporting is done via the [Extender Cucumber Runner](http://mkolisnyk.github.io/cucumber-reports/extended-cucumber-runner) library. The results are placed in the `target` directory along with screenshots of failing tests, and the HTML of the last page reached (if configured).

A custom report detailing the usage of database test data is also output in the `target` directory, with any errors highlighted in red.


## Data Server
This test suite requires sharing/co-ordination of data between tests, however Cucumber-JVM does not support multi-threading.

The test suite uses the [Courgette JVM](https://github.com/prashant-ramcharan/courgette-jvm) runner, which runs features in separate processes. Before the test suite starts, a data sever (which is a small SpringBoot microservice) is started. During each test, a simple REST API is invoked from each feature process to obtain test data. After the test suite completes, the data server is automatically shut down.


## Source Code Folders
* main
   * java = The test fixtures, and supporting framework
   * resources = The Gherkin features
* server
   * java = The data server
   * resources = The SQL queries
* server-test
   * java = Unit tests for the data server
* test
   * java = Unit tests for the test suite supporting framework
   
