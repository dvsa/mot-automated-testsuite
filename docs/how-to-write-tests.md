# How to write tests

Tests are written using the Gherkin syntax (see [Reference documentation](https://cucumber.io/docs/reference)).

Each test is a `Scenario` containing a number of `Steps` starting with `Given`, `When`, `Then`, `And` and `But`.

Tests are grouped by functional area into a `Feature`, and each ```.feature``` file is stored below the ```src/test/resources/features``` directory.

Each `Step` has a corresponding `Step Definition` in a Java fixture class (also grouped by functional area), with a regular expression that matches the text in the `Step`.

A list of existing step definitions is maintained here: [Step Definitions](step-definitions.md). To create new step definitions add to an existing Java fixture class (if an existing functional area) or create a new class.

## Writing Java Fixtures

A general guide and some guidance and advice is covered here: [Writing Java Fixtures](writing-java-fixtures.md). 

Please try to stick to the common syntax used by existing step definitions, when creating new steps.

 
## Using Test Data
 
 