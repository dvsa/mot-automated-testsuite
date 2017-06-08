package uk.gov.dvsa.mot.junit;

import com.github.mkolisnyk.cucumber.runner.ExtendedCucumberOptions;
import com.github.mkolisnyk.cucumber.runner.ExtendedParallelCucumber;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Junit/Courgette-JVM runner used to run the Gerkin features.
 *
 * <code>runLevel</code> of <code>FEATURE</code> results in one independent copy of the Spring Application Context
 * being created for each cucumber **feature** (so shared between scenarios within each feature).
 */
@RunWith(ExtendedParallelCucumber.class)
@ExtendedCucumberOptions(
        threadsCount = 3,
        outputFolder = "target",
        jsonReport = "target/cucumber.json",
        overviewReport = true,
        detailedReport = true
)
@CucumberOptions(plugin = { "json:target/cucumber.json"},
        features = { "features" },
        glue = { "uk.gov.dvsa.mot" })
public class CucumberRunner {
}
