package uk.gov.dvsa.mot.reporting;

import com.github.mkolisnyk.cucumber.reporting.CucumberDetailedResults;
import com.github.mkolisnyk.cucumber.reporting.CucumberResultsOverview;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import uk.gov.dvsa.mot.framework.RequestHandler;
import uk.gov.dvsa.mot.utils.config.TestsuiteConfig;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;

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

        generateDocumentList(outputDirectory);
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
     * @throws Exception            Error producing the report
     */
    private static void generateDocumentList(String outputDirectory)
            throws Exception {
        // Get the results from the server
        writeDocumentResults();

        List<String> allowedExtensions = Arrays.asList("pdf", "csv");
        String documentsRoot = "target/documents/";

        // Read the results as an ordered map (TreeMap)
        Map<String, String[]> documentResults = getDocumentResults(documentsRoot);
        String timestamp = documentResults.get("timestamp")[0];
        documentResults.remove("timestamp");

        String documentLocation = documentsRoot + timestamp + "/";

        StringBuilder html = new StringBuilder();
        html.append("<html><head><style type='text/css'>")
                .append("h1 {background-color:#9999CC}")
                .append("h2 {background-color:#BBBBCC}")
                .append("h3 {background-color:#DDDDFF}")
                .append("th {border:1px solid black;background-color:#CCCCDD;}")
                .append("td{text-transform: capitalize; border:1px solid black;}")
                .append("table {border:1px solid black;border-collapse: collapse;}")
                .append("table table {width:98%; float:right; margin-bottom:0.3em; }")
                .append(".passed {background-color:lightgreen;font-weight:bold;color:darkgreen}")
                .append(".skipped {background-color:silver;font-weight:bold;color:darkgray}")
                .append(".failed {background-color:tomato;font-weight:bold;color:darkred}")
                .append(".undefined {background-color:gold;font-weight:bold;color:goldenrod}")
                .append(".known {background-color:goldenrod;font-weight:bold;color:darkred}")
                .append("img {width:100%;")
                .append("OL { counter-reset: item }")
                .append("OL>LI { display: block }")
                .append("OL>LI:before { content: counters(item, '.') ' '; counter-increment: item }</style>")
                .append("<title>Document List</title></head><")

                .append("body><h1>Document List</h1>")
                .append("<h2>Results</h2>");

        // Get list of all files in the directory
        File[] files = new File(documentLocation).listFiles();

        if (documentResults == null || files == null || files.length == 0) {
            html.append("<li>")
                    .append("<h3>No documents saved during test.</h3>")
                    .append("</li>");
        } else {
            html.append("<table width='700px'>")
                    .append("<tbody>");

            String previousFeature = ""; // Store previous feature to see if the next scenario should start in a new row
            boolean usingFeature = false;

            for (String key : documentResults.keySet()) {
                String[] documentResult = documentResults.get(key);

                if (usingFeature == false && documentResult.length == 4) {
                    usingFeature = true;
                }

                int featureIndex = -1;
                int scenarioIndex = -1;
                int filenameIndex = -1;
                int testResultIndex = -1;

                // This is the order the data was added in, in the getDocumentsResults
                if (usingFeature) {
                    filenameIndex = 0;
                    featureIndex = 1;
                    scenarioIndex = 2;
                    testResultIndex = 3;
                } else {
                    filenameIndex = 0;
                    scenarioIndex = 1;
                    testResultIndex = 2;
                }

                File file = new File(documentLocation + documentResult[filenameIndex]);

                String fileFullName = file.getName();
                String name = "";
                String extension = "";

                // get file name without the extension and the extension as separate variables
                int pos = fileFullName.lastIndexOf(".");
                if (pos > 0) {
                    name = fileFullName.substring(0, pos);
                    extension = fileFullName.substring(pos + 1, fileFullName.length());
                }

                if (!allowedExtensions.contains(extension)) {
                    continue;
                }

                String feature = "unknown";
                String scenario = "unknown";
                String result = "unknown";

                // Populate variables. If there is no feature specified, "unknown" will be used instead
                if (usingFeature) {
                    feature = documentResult[featureIndex];
                    scenario = documentResult[scenarioIndex];
                    result = documentResult[testResultIndex];
                } else {
                    scenario = documentResult[scenarioIndex];
                    result = documentResult[testResultIndex];
                }

                // If current feature is different than previous one
                if (!previousFeature.equals(feature)) {
                    html.append("<tr class ='").append(result).append("'>")
                            .append("<td colspan='2' class='").append(result).append("'>Feature: ")
                            .append(feature.replace("-", " "))
                            .append("</td>")
                            .append("</tr>");
                }

                html.append("<tr>")
                        .append("<td colspan='2' class='").append(result).append("'>")
                        .append("<table width='100%'>")
                        .append("<tbody>")
                        .append("<tr class ='").append(result).append("'>")
                        .append("<td class='").append(result).append("'>")
                        .append("<small>Scenario: ")
                        .append(scenario.replace("-", " ")).append("</small>")
                        .append("</td>")
                        .append("<td class='").append(result).append("'>")
                        .append("<small>Result: ").append(result).append("</small>")
                        .append("</td>")
                        .append("</tr>")
                        .append("<tr class='").append(result).append("'>")
                        .append("<td colspan='2' class='").append(result).append("'>");

                // If PDF then add a thumbnail, else just add a link with file name
                if (extension.equals("pdf")) {
                    File thumbnail = new File(documentLocation + "thumbnails/" + name + ".jpg");

                    thumbnail.mkdirs();
                    thumbnail.delete();
                    thumbnail.createNewFile();

                    PDDocument document = PDDocument.load(file);
                    PDFRenderer renderer = new PDFRenderer(document);

                    BufferedImage bufferedImage = renderer.renderImage(0);
                    ImageIO.write(bufferedImage,  "jpg", thumbnail);

                    html.append("<a target='_blank' href='../")
                            .append(documentLocation).append(fileFullName).append("'>")
                            .append("<img src='../").append(documentLocation).append("thumbnails/")
                            .append(thumbnail.getName()).append("'/>")
                            .append("</a>");
                } else {
                    html.append("<a target='_blank' href='../")
                            .append(documentLocation).append(fileFullName).append("'>")
                            .append(fileFullName)
                            .append("</a>");
                }

                html.append("</td>")
                        .append("</tr>")
                        .append("</tbody>")
                        .append("</table>");

                // End row if previous feature is different than current one
                if (!previousFeature.equals(feature)) {
                    html.append("</td>")
                            .append("</tr>");
                    previousFeature = feature;
                }
            }
        }

        html.append("</td>")
                .append("</tr>")
                .append("</body>")
                .append("</html>");

        File file = new File(outputDirectory + "document-list.html");

        file.delete();
        file.createNewFile();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(html.toString());
        bufferedWriter.close();
    }

    private static Map<String, String[]> getDocumentResults(String documentLocation) throws Exception {
        File file = new File(documentLocation + "document_results.txt");

        if (!file.exists()) {
            return null;
        }

        Map<String, String[]> documentResults = new TreeMap<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        // Load the document into a TreeMap
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] result = line.split(";");

            if (result.length  < 3) {
                documentResults.put("timestamp", new String[]{line});
            } else if (result.length == 4) {
                documentResults.put(result[0],
                        new String[]{result[0], result[1], result[2], result[3]});
            } else if (result.length == 3) {
                documentResults.put(result[0], result);
            } else {
                throw new Exception("Failed to read document results. Invalid data format.");
            }
        }

        return documentResults;
    }

    private static void writeDocumentResults() throws Exception {
        String configuration = System.getProperty("configuration");

        TestsuiteConfig env;
        if (configuration != null) {
            env = TestsuiteConfig.loadConfigFromString(configuration);
        } else {
            env = TestsuiteConfig.loadConfig("testsuite");
        }

        RequestHandler requestHandler = new RequestHandler(env);

        String results = requestHandler.getDocumentResults();

        writeFile("target/documents/document_results.txt", results);
    }

    private static void writeFile(String filename, String content) throws Exception {
        BufferedWriter bufferedWriter = null;

        File file = new File(filename);

        file.delete();
        file.createNewFile();

        bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(content);

        bufferedWriter.close();
    }
}
