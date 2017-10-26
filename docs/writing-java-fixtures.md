# Writing Java Fixtures

## Checkstyle

The gradle build script is set to enforce a coding standard, based upon the Google coding standard (but with some pragmatic exclusions - like the MOT Java Services use) when compiling any code.

## Java APIs

Each fixture class must implement ```cucumber.api.java8.En``` and includes a public constructor with nested lambda functions as follows:
 
```java
public MyStepDefinitions() {
    
    And("^I check the vehicle summary section of the test summary has \"([^\"]+)\" of \"([^\"]+)\"$", 
        (String field, String value) -> {
            ..the code..
        });    
}
``` 

There are methods corresponding to each of the Gherkin keywords (```Given```, ```When```, ```Then```, ```And```, ```But```) but in practice it does not matter which keyword is actually used with the step.
 
Each regular expression should start with ```^``` and end with ```$``` so the whole step is matched. Each parameter to extract is a regular expression surrounded with brackets. Primitive parameters have to be matched using wrapper classes. 

Because the regular expressions characters need to be escaped in Java Strings, they can easily get pretty complex. Here are some common regular expressions to use:

* ```(\\d+)``` - matches a number of any length
* ```\"([^\"]+)\"``` - matches text within a ".." block
* ```\{([^\}]+)\}``` - matches text within a {..} block

## Logging

The test suite uses `slf4j` for logging. Add a logger to your fixture class as follows:

```java
/** The logger to use. */
private static final Logger logger = LoggerFactory.getLogger(MyStepDefinitions.class);
```

The logging configuration is in ```src/main/resources/logback.xml```, and the logs are written to ```build/logs``` directory
(as several ```pid@hostname.log``` files).

If needed, the logging configuration for the data server is in ```src/server/resources/logback.xml```, and the logs are also written to ```build/logs```
directory (as ```server.log```).

## Data Server and Testsuite processes

If running the testsuite in serial, there will be two processes: the data server (```src/server```), and the cucumber testsuite (```src/main```).

If running the testsuite in parallel, there will be several processes: one data server (```src/server```), and one process (```src/main```) for each cucumber feature.
The maximum number of processes run in parallel is set in ```threads``` setting of the ```@CourgetteOptions``` annotation in ```CourgetteJUnitRunner```.

The Courgette-JVM library handles aggregating the various separate ```selenium.json``` results into a single file, and will re-run failed scenarios
(if the ```rerunFailedScenarios``` setting of the ```@CourgetteOptions``` is set to ```true```).

## Object lifecycles and Dependency Injection

The testsuite uses Spring (v4) to manage object lifecycles and inject dependencies. When the testsuite runs, the following actions happen:

1. The ```main``` method of ```uk.gov.dvsa.mot.runner.StartDataServer``` is run to start the data server
   1. A sanity check is made to see if the data server is already running (e.g. left over from a previous failed run), if so it is shut down
   1. The ```uk.gov.dvsa.mot.server.ServerApplication``` is started
       1. The Spring configuration is loaded from ```uk.gov.dvsa.mot.server.di.SpringConfiguration```
           1. Common global objects, such as ```uk.gov.dvsa.mot.data.DatabaseDataProvider``` (handles DB datasets) are created
1. *Then for each cucumber feature:*   
    1. The Spring configuration is loaded from ```src/main/resources/cucumber.xml```, which references the Java Configuration in ```uk.gov.dvsa.mot.di.SpringConfiguration```
       1. Common global objects, such as ```uk.gov.dvsa.mot.framework.WebDriverWrapper``` (runs the Chrome web browser) are created
    1. *Then for each cucumber scenario:*
       1. New instances of all java fixtures (step definition classes) and lifecycle hooks (the ```uk.gov.dvsa.mot.framework.LifecycleHooks``` class) are instantiated
       1. Any methods in lifecycle hook classes annotated with ```@cucumber.api.java.Before``` are run
          1. In ```uk.gov.dvsa.mot.framework.LifecycleHooks```, the ```startup``` method is run
       1. The test steps are run
       1. Any methods in lifecycle hook classes annotated with ```@cucumber.api.java.After``` are run
          1. In ```uk.gov.dvsa.mot.framework.LifecycleHooks```, the ```teardown``` method takes a screenshot, writes the HTML, resets data keys, and clears any browser cookies 
    1. Then upon shutdown any methods in Spring beans annotated with ```@javax.annotation.PreDestroy``` are run
       1. In ```uk.gov.dvsa.mot.framework.WebDriverWrapper```, the ```preDestroy``` method shuts down the Chrome web browser
1. Then once all testsuite processes have completed, the ```main``` method of ```uk.gov.dvsa.mot.runner.StopDataServer``` is run to stop the data server
   1. Any methods in Spring beans annotated with ```@javax.annotation.PreDestroy``` are run
       1. This triggers the data usage report to be written to the ```target``` folder
1. Then ```uk.gov.dvsa.mot.reporting.CucumberReporting``` is invoked to process the ```build/reports/selenium/selenium.json``` file and produce reports in ```target``` folder
    
Each java fixture class can be stateful, if required, and any maintained state will be kept for a single test scenario.
   
To access common global objects from within a java fixture class, use dependency injection - for example use constructor injection with ```@javax.inject.Inject``` as follows:
   
```java
@Inject
public MyStepDefinitions(WebDriverWrapper driverWrapper) {
  ..use driverWrapper as needed..
}  
```   

## WebDriverWrapper

This class wraps the web browser state and Selenium API use in a generic (i.e. no MOT application-specifics) set of methods that can be used to manipulate and test web pages. 

Some of these methods have the complexity of coping with a number of situations (for example buttons implemented using either ```input``` or ```button``` tags, or form fields with labels associated in various different ways).
Because of this, a simple JUnit test framework has been setup so you can update/extend these methods safely, without breaking existing functionality. These tests are run automatically as part of any Gradle build, and must pass for the testsuite to be used.

The ```WebDriverWrapperTest``` is the JUnit test class, and it tests using HtmlUnit, set to emulate Chrome. Web page examples can be added to the ```exampleHtml``` folder.

   
## General Advice

Identify web page elements as generically as possible, so that tests are not brittle and won't break if minor changes are made to the page. For example, identify links and buttons using the link/button text, and identify fields using the label text. Only use ids, names, or XPath expressions as a last resort.

Keep step definition lambdas short and simple, if more than a couple of lines of code then extract to a private method.

When refactoring common Gherkin steps into Java methods with a new step definition, keep the original steps as comments followed by the single line of code (or short block of code) that implements that original step.

Use JUnit assertions to test logic, and keep all JUnit use to the java fixture classes.

Keep all Selenium API usage inside the ```WebDriverWrapper``` class (see above), keep all MOT-specific logic in fixture classes.

Keep any behaviour that needs to happen before or after all scenarios inside the ```LifecycleHooks``` class. 

Extract and refactor any common code inside your fixture classes, but a few techniques can be used to help retain maintainability:

* Use ```enum``` with descriptive names to capture concepts such as the user journey being followed, or options being taken
* Use ```Optional<T>``` to handle parameters only needed in certain circumstances (rather than ```null``` or special values like ```0``` or ```-1```)