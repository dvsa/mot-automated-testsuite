# Writing Java Fixtures

## Checkstyle

The gradle build script is set to enforce a coding standard, based upon the Google coding standard (but with some pragmatic exclusions - like the MOT Java Services use) when compiling any code.

## Java APIs

Each fixture class must implement ```cucumber.api.java8.En``` and includes a public constructor with nested lambda functions as follows:
 
```
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

```
/** The logger to use. */
private static final Logger logger = LoggerFactory.getLogger(MyStepDefinitions.class);
```

The logging configuration is in ```src/test/resources/logback.xml```, and the logs are written to ```build/logs/testsuite.log```.

## Object lifecycles and Dependency Injection

The testsuite uses Spring (v4) to manage object lifecycles and inject dependencies. When the testsuite runs, the following actions happen:

1. The Spring configuration is loaded from ```src/test/resources/cucumber.xml```, which references the Java Configuration in ```uk.gov.dvsa.mot.di.SpringConfiguration```
   1. Common global objects, such as ```uk.gov.dvsa.mot.data.DatabaseDataProvider``` (handles DB datasets) and ```uk.gov.dvsa.mot.framework.WebDriverWrapper``` (runs the Chrome web browser) are created
1. Then for each scenario:
   1. New instances of all java fixtures (step definition classes) and lifecycle hooks (the ```uk.gov.dvsa.mot.framework.LifecycleHooks``` class) are instantiated
   1. Any methods in lifecycle hook classes annotated with ```@cucumber.api.java.Before``` are run
      1. In ```uk.gov.dvsa.mot.framework.LifecycleHooks```, the ```startup``` method ensures the datasets are loaded (if not already)
   1. The test steps are run
   1. Any methods in lifecycle hook classes annotated with ```@cucumber.api.java.After``` are run
      1. In ```uk.gov.dvsa.mot.framework.LifecycleHooks```, the ```teardown``` method takes a screenshot, resets data keys, and clears any browser cookies 
1. Then upon shutdown any methods in Spring beans annotated with ```@javax.annotation.PreDestroy``` are run
   1. In ```uk.gov.dvsa.mot.framework.WebDriverWrapper```, the ```preDestroy``` method shuts down the Chrome web browser
    
Each java fixture class can be stateful, if required, and any maintained state will be kept for a single test scenario.
   
To access common global objects from within a java fixture class, use dependency injection - for example use constructor injection with ```@javax.inject.Inject``` as follows:
   
```
@Inject
public MyStepDefinitions(WebDriverWrapper driverWrapper) {
  ..use driverWrapper as needed..
}  
```   
   
## General Advice

Identify web page elements as generically as possible, so that tests are not brittle and won't break if minor changes are made to the page. For example, identify links and buttons using the link/button text, and identify fields using the label text. Only use ids, names, or XPath expressions ias a last resort.

Keep step definition lambdas short and simple, if more than a couple of lines of code then extract to a private method.

Use JUnit assertions to test logic, and keep all JUnit use to the java fixture classes.

Keep all Selenium API usage inside the ```DriverWrapper``` class.

Refactor and parameterise any common code just as you would with production code, but a few techniques can be used to help retain maintainability:

* Use enumerations with descriptive names to capture concepts such as the user journey being followed, or options being taken
* Use Optional<T> to handle parameters only needed in certain circumstances, rather than ```null``` or special values like ```0``` or ```-1```