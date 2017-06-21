package uk.gov.dvsa.mot.reporting;

import com.github.mkolisnyk.cucumber.reporting.CucumberDetailedResults;
import com.github.mkolisnyk.cucumber.reporting.CucumberResultsOverview;

/**
 * Reporting class used to generate HTML reports based on the Cucumber json report.
 */
public class CucumberReporting {

    /**
     * Called by the Gradle reporting task to generate the HTML reports.
     * @param args          Command line arguments (unused)
     * @throws Exception    Error producing the report
     */
    public static void main(String[] args) throws Exception {
        String outputDirectory = "target/";
        String sourceFile = "build/reports/selenium/selenium.json";
        String outputName = "cucumber";
        String screenShotLocation = "screenshots/";

        generateOverviewReport(outputDirectory, sourceFile, outputName);

        generateFeatureReport(outputDirectory, sourceFile, outputName, screenShotLocation);
    }

    /**
     * Generate the HTML report on features, scenarios, and steps broken down by feature.
     * @param outputDirectory       The directory to output the report to and the base directory for screenshots
     * @param sourceFile            The json file used to generate the HTML reports
     * @param outputName            The prefix given to the report
     * @param screenShotLocation    The location from the outputDirectory to store screenshots for the report
     * @throws Exception            Error producing the report
     */
    private static void generateFeatureReport(String outputDirectory, String sourceFile, String outputName,
                                              String screenShotLocation) throws Exception {
        CucumberDetailedResults cucumberDetailedResults = new CucumberDetailedResults();
        cucumberDetailedResults.setOutputDirectory(outputDirectory);
        cucumberDetailedResults.setOutputName(outputName);
        cucumberDetailedResults.setSourceFile(sourceFile);
        cucumberDetailedResults.setScreenShotLocation(screenShotLocation);
        cucumberDetailedResults.execute(true, false);
    }

    /**
     * Generate the overview HTML report including pie chart, feature breakdown, and scenario breakdown
     * @param outputDirectory       The directory to output the report to
     * @param sourceFile            The json file used to generate the HTML reports
     * @param outputName            The prefix given to the report
     * @throws Exception            Error producing the report
     */
    private static void generateOverviewReport(String outputDirectory, String sourceFile, String outputName)
            throws Exception {
        CucumberResultsOverview cucumberResultsOverview = new CucumberResultsOverview();
        cucumberResultsOverview.setOutputDirectory(outputDirectory);
        cucumberResultsOverview.setOutputName(outputName);
        cucumberResultsOverview.setSourceFile(sourceFile);
        cucumberResultsOverview.execute();
    }
}
