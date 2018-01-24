package uk.gov.dvsa.mot.reporting;

import com.github.mkolisnyk.cucumber.reporting.CucumberDetailedResults;
import com.github.mkolisnyk.cucumber.reporting.CucumberResultsOverview;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.io.File;
import java.io.FileWriter;

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
        String documentLocation = "documents/" + WebDriverWrapper.getTimestamp();

        generateOverviewReport(outputDirectory, sourceFile, outputName);

        generateFeatureReport(outputDirectory, sourceFile, outputName, screenShotLocation);

        generateDocumentList(outputDirectory, documentLocation);
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
        cucumberDetailedResults.execute(true, new String[] {});
    }

    /**
     * Generate the overview HTML report including pie chart, feature breakdown, and scenario breakdown.
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

    /**
     * Generate HTML to view list of documents.
     * @param documentLocation      The directory where all documents are located
     * @throws Exception            Error producing the report
     */
    private static void generateDocumentList(String outputDirectory, String documentLocation)
            throws Exception {
        File folder = new File(documentLocation);
        File[] files = folder.listFiles();

        StringBuilder html = new StringBuilder();

        html.append("<html><head>style type=\"text/css\">h1 {background-color:#9999CC}\n"
                + "h2 {background-color:#BBBBCC}\n"
                + "h3 {background-color:#DDDDFF}\n"
                + "OL { counter-reset: item }\n"
                + "OL>LI { display: block }\n"
                + "OL>LI:before { content: counters(item, \".\") \" \"; counter-increment: item }\n"
                + "@page {\n"
                + "\t size: auto;\n"
                + "     @top-center {\n"
                + "     \tcontent: \"Detailed Results Report\";\n"
                + "\t    color: silver;\n"
                + "\t    font-size: 14px;\n"
                + "     }\n"
                + "     @top-right {\n"
                + "     \tcontent: date(\"dd MMM, yyyy hh:mm\");\n"
                + "\t    color: silver;\n"
                + "\t    font-size: 8px;\n"
                + "     }\n"
                + "    @bottom-right {\n"
                + "    \tcontent: \"Page \" counter(page) \" of \" counter(pages) ;\n"
                + "\t    color: silver;\n"
                + "\t    font-size: 8px;\n"
                + "    }\n"
                + "}\n"
                + "</style><title>Document List</title></head><body><h1>Document List</h1><ol>");
        for (File file : files) {
            html.append("<li>");
            html.append("<a target='_blank' href='" + file.getPath() + "'>");
            html.append(file.getName() + "</a>");
            html.append("</li>");
        }
        html.append("</ol></body></html>");

        File outputFile = new File(outputDirectory + "document-report.html");
        FileWriter fileWriter = new FileWriter(outputFile);
        fileWriter.write(html.toString());
    }
}
