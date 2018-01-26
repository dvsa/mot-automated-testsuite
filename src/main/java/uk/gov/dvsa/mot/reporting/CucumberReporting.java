package uk.gov.dvsa.mot.reporting;

import com.github.mkolisnyk.cucumber.reporting.CucumberDetailedResults;
import com.github.mkolisnyk.cucumber.reporting.CucumberResultsOverview;
import groovy.lang.Tuple2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
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
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(new File(outputDirectory + "timestamp.txt")));
        String timestamp = bufferedReader.readLine();
        bufferedReader.close();
        String documentLocation = "target/documents/" + timestamp + "/";

        File folder = new File(documentLocation);

        StringBuilder html = new StringBuilder();

        html.append("<html><head><style type=\"text/css\">"
                + "h1 {background-color:#9999CC}\n"
                + "h2 {background-color:#BBBBCC}\n"
                + "h3 {background-color:#DDDDFF}\n"
                + "th {border:1px solid black;background-color:#CCCCDD;}\n"
                + "td{border:1px solid black;}\n"
                + "table {border:1px solid black;border-collapse: collapse;}\n"
                + ".passed {background-color:lightgreen;font-weight:bold;color:darkgreen}\n"
                + ".skipped {background-color:silver;font-weight:bold;color:darkgray}\n"
                + ".failed {background-color:tomato;font-weight:bold;color:darkred}\n"
                + ".undefined {background-color:gold;font-weight:bold;color:goldenrod}\n"
                + ".known {background-color:goldenrod;font-weight:bold;color:darkred}\n"
                + "OL { counter-reset: item }\n"
                + "OL>LI { display: block }\n"
                + "OL>LI:before { content: counters(item, \".\") \" \"; counter-increment: item }\n"
                + "</style><title>Document List</title></head><body><h1>Document List</h1>"
                + "<h2>Results</h2>");

        File[] files = folder.listFiles();

        Map<String, Tuple2<String, String>> documentResults = getPdfResults(documentLocation);

        for (String key : documentResults.keySet()) {
            System.out.println(key + "=" + documentResults.get(key).getFirst()
                    + ";" + documentResults.get(key).getSecond());
        }

        if (files == null || files.length == 0) {
            html.append("<li>");
            html.append("<h3>No documents saved during test.</h3>");
            html.append("</li>");
        } else {
            html.append("<table width='700px'><tbody>");
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }

                String name = file.getName();
                int pos = name.lastIndexOf(".");
                if (pos > 0) {
                    name = name.substring(0, pos);
                }

                String extension = file.getName();
                pos = extension.lastIndexOf(".");
                if (pos > 0) {
                    extension = extension.substring(pos + 1, extension.length());
                    System.out.println("." + extension + ".");
                }

                if (!extension.equals("pdf") && !extension.equals("csv")) {
                    System.out.println(extension.equals("pdf"));
                    continue;
                }

                File image = new File(documentLocation + "thumbnails/" + name + ".jpg");
                image.mkdirs();
                image.delete();
                image.createNewFile();

                PDDocument document = PDDocument.load(file);
                PDFRenderer renderer = new PDFRenderer(document);
                BufferedImage bufferedImage = renderer.renderImage(0);
                ImageIO.write(bufferedImage,  "jpg", image);

                System.out.println(file.getName());
                String scenario = (documentResults == null)
                        ? "unknown" : documentResults.get(file.getName()).getFirst();
                String result = (documentResults == null)
                        ? "unknown" : documentResults.get(file.getName()).getSecond();

                html.append("<tr class ='" + result + "'>"
                        + "<td class='" + result + "'>Scenario: "
                        + scenario + "</td>"
                        + "<td class='" + result + "'>Result: "
                        + result + "</td>"
                        + "</tr>");
                html.append("<tr class='" + result + "'>");
                html.append( "<td colspan='2' class='" + result + "><a target='_blank' href='../"
                        + documentLocation + file.getName() + "'>");
                html.append("<img width='100%' src='../"
                        + documentLocation + "thumbnails/" + image.getName() + "'/></a></td>");
                html.append("</tr>");
            }
            html.append("</tbody></table>");
        }

        html.append("</body></html>");


        File file = new File(outputDirectory + "document-report.html");

        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(html.toString());
        bufferedWriter.close();
    }

    private static Map<String, Tuple2<String, String>> getPdfResults(String documentLocation) throws Exception {
        File file = new File(documentLocation + "pdf_file_results.txt");

        if (!file.exists()) {
            return null;
        }

        Map<String, Tuple2<String, String>> documentMap = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
            String filename = line.split("=")[0];
            String tuple = line.split("=")[1];
            String scenario = tuple.split(";")[0];
            String result = tuple.split(";")[1];
            documentMap.put(filename, new Tuple2<>(scenario, result));
        }

        return documentMap;
    }
}
