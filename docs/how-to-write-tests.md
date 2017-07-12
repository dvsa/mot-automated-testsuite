# How to write tests

Tests are written using the Gherkin syntax (see [Reference documentation](https://cucumber.io/docs/reference)).

Each test is a `Scenario` containing a number of `Steps` starting with `Given`, `When`, `Then`, `And` and `But`.

Tests are grouped by functional area into a `Feature`, and each ```.feature``` file is stored below the ```src/test/resources/features``` directory.

Each `Step` has a corresponding `Step Definition` in a Java fixture class (also grouped by functional area), with a regular expression that matches the text in the `Step`.

A list of existing step definitions is maintained here: [Step Definitions](step-definitions.md). If you need to create new step definitions in order to add or update test scenarios, add to an existing Java fixture class (if an existing functional area) or create a new class.

Start by writing tests using the generic Web step definitions. Typically the resulting tests will be quite lengthy. Once you have several tests with some common sections, extract out those common sections into an appropriate Java fixture class invoked by a single step definition, with appropriate parameters. 
Retain the original Gherkin steps as comments, so that the Java code can be easily maintained.
Try to repeat this process of extracting and simplifying so that your test scenarios are short but still explicitly cover all the steps being taken and checks being made - so that you can fully understand what a test does without looking through the java code. 


## Writing Java Fixtures

A general guide and some guidance and advice is covered here: [Writing Java Fixtures](writing-java-fixtures.md). 

Please try to stick to the common syntax used by existing step definitions, when creating new steps.

 
## Using Test Data

A general guide and some guidance and advice is covered here: [Using test data](using-test-data.md).
 
Please try to use the existing data that exists in each of the test environments for your test scenarios.
 
 
 