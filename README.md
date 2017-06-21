# Cucumber Automated Test Proof of Concept

Using Cucumber-JVM and Selenium to test the DVSA MOT application through the web browser UI, with test fixtures
written using Java (JDK 8 Lambda-format step definitions).

These tests are written as Gerkin features, in the *src/test/resources/features* directory.

Test data is loaded from the environment under test, using queries in the *src/test/resources/queries* directory.

## Pre-requisites
Checkout the ```mot/2fa-pin-generator``` project into a directory alongside the top level directory of this project, and build it as follows:
```
./gradlew build
```

Get a copy of ```testsuite.properties``` from another member of the team and save it in the ```configuration``` directory. See [Configuration Settings](configuration/README.md) for further details.

## How to run

To run all feature tests in serial, at the command line:
```
./gradlew cucumber-all
```

To run one specific feature, run the following at the command line. The filename can include any sub-directories below the ```features``` directory (e.g. ```sub-dir/example.feature``):

```
./gradlew cucumber-feature '-Pfeature=<the .feature file>'
```

To run all features with a specific tag, run the following at the command line. To specify multiple tags, use a comma-separated list (e.g. ```@tag1,@tag2``):

```
./gradlew cucumber-tag '-Ptag=<tag>'
```

## Feature Tags

The following tags are used:

* none yet...

## Reports
Reporting is done via the [Extender Cucumber Runner](http://mkolisnyk.github.io/cucumber-reports/extended-cucumber-runner) library. The results are placed in the `target` directory along with screenshots of failing tests.
