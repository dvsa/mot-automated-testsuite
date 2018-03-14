package uk.gov.dvsa.mot.reporting;

import uk.gov.dvsa.mot.framework.html.HtmlWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class OpenInterfaceReportBuilder {

    private static final String PASS = "pass";
    private static final String FAIL = "fail";
    private static final String SPACER_CLASS = "spacer";

    /**
     * Generate a report from the test results.
     *
     * @param fileName of the html file to output
     * @param registration of the vehicle used for test
     * @param response from the open interface
     * @param expectedValues for the xpath
     * @param responseValues for the xpath located in response
     * @param results of the comparisons
     */
    public static void generateResponseReport(String fileName, String registration, String response,
                                              Map<String, String> expectedValues, Map<String, String> responseValues,
                                              Map<String, Boolean> results) {
        StringBuilder resultsHtml = new StringBuilder();

        // Add results
        for (String key : expectedValues.keySet()) {

            String resultxPath = HtmlWriter.tag("td", null, key);

            String expectedValue = expectedValues.get(key);
            String resultExpected = HtmlWriter.tag("td", null, expectedValue);

            String responseValue = responseValues.get(key);
            String resultResponse = HtmlWriter.tag("td", null, responseValue);

            String resultContent = resultxPath + resultExpected + resultResponse;

            Map<String, String> attributes = new HashMap<>();
            attributes.put("class", (results.get(key) ? PASS : FAIL));
            String resultRow = HtmlWriter.tag("tr",
                    attributes,
                    resultContent);

            resultsHtml.append(resultRow);
        }

        // Colspan = 2 attribute
        Map<String, String> colspanTwoAttribute = new HashMap<>();
        colspanTwoAttribute.put("colspan", "2");

        // Vrm Heading
        String vrmHeadingContent = HtmlWriter.tag("th", null, "Vehicle VRM:")
                + HtmlWriter.tag("td", colspanTwoAttribute, registration);
        String vrmHeading = HtmlWriter.tag("tr", null, vrmHeadingContent);

        // Table Heading
        String tableHeadingContent = HtmlWriter.tag("th", null, "xPath")
                + HtmlWriter.tag("th", null, "Expected Value")
                + HtmlWriter.tag("th", null, "Response Value");
        String tableHeadingHtml = HtmlWriter.tag("tr", null, tableHeadingContent);

        // Response
        String responseXmp = HtmlWriter.tag("xmp", null, response);
        String responseContent = HtmlWriter.tag("th", null, "Response:")
                + HtmlWriter.tag("td", colspanTwoAttribute, responseXmp);
        String responseHtml = HtmlWriter.tag("tr", null, responseContent);

        // Spacer
        Map<String, String> spacerTdAttributes = new HashMap<>();
        spacerTdAttributes.put("colspan", "3");
        Map<String, String> spacerTrAttributes = new HashMap<>();
        spacerTrAttributes.put("class", SPACER_CLASS);

        String spacerContent = HtmlWriter.tag("td", spacerTdAttributes, "");
        String spacerHtml = HtmlWriter.tag("tr", spacerTrAttributes, spacerContent);

        String tableContent = vrmHeading + spacerHtml
                + tableHeadingHtml + spacerHtml
                + resultsHtml + spacerHtml + responseHtml;
        String tableHtml = HtmlWriter.tag("table", null, tableContent);

        String parentDirectory = "target/openinterface/";
        String outputDirectory = parentDirectory + "results/";
        String outputName = fileName.replace(" ", "_") + ".html";

        File parentDir = new File(parentDirectory);
        File outputDir = new File(outputDirectory);
        File outputFile = new File(outputDirectory + outputName);

        if (parentDir.exists()) {
            parentDir.delete();
        }

        outputDir.mkdirs();

        try {
            outputFile.createNewFile();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write open interface test results.");
        }

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputFile), "UTF-8"))) {
            out.write(tableHtml);

        } catch (IOException ex) {
            String message = "Error writing HTML file: " + ex.getMessage();
            throw new RuntimeException(message);
        }
    }

    /**
     * Generate a final report, containg all scenario reports in it.
     */
    public static void generateFinalReport() {
        String sourceDirectory = "target/openinterface/results/";

        File sourceDir = new File(sourceDirectory);
        File[] sourceFileList = sourceDir.listFiles();

        String output = "";

        for (File file : sourceFileList) {
            Map<String, String> attributes = new HashMap<>();
            attributes.put("width", "auto");
            attributes.put("height", "auto");
            attributes.put("frameborder", "0");
            attributes.put("src", "openinterface/results/" + file.getName());

            String heading = file.getName().substring(0, file.getName().lastIndexOf('.')).replace("_", " ") + ":";
            String headingHtml = HtmlWriter.tag("h1", null, heading);

            String fileContents = null;
            try {
                fileContents = new String(Files.readAllBytes(file.toPath()));
            } catch (IOException ex) {
                String message = String.format("Failed to read file %s. Stacktrace: %s", file.getName(),
                        ex.getStackTrace());

                throw new RuntimeException(message);
            }

            output += HtmlWriter.tag("div", null, headingHtml + fileContents);
        }

        // Style
        String styleContent = "xmp{white-space:pre-wrap;word-wrap:break-word;}"
                + "div{margin-bottom:5rem;}"
                + "." + FAIL + "{background-color:#ff7676;}"
                + "." + PASS + "{background-color:#94fab1;}"
                + "table{max-width:400px;border-collapse:collapse;}"
                + "table td,table th{padding:0.2rem 0.6rem;border:1px solid black;}"
                + "table th{background-color: #a3c8ff;}"
                + "textarea{width:100%;height:100%;box-sizing:border-box;resize:none;}"
                + "." + SPACER_CLASS + "{background-color:rgb(216,216,216);height:1.4rem;}"
                + "xmp{white-space: break-word;}";
        String style = HtmlWriter.tag("style", null, styleContent);
        String title = HtmlWriter.tag("title", null, "Open Interface Test Report");

        output = HtmlWriter.tag("head", null, title + style)
                + HtmlWriter.tag("body", null, output);

        output = HtmlWriter.tag("html", null, output);

        String targetDirectory = "target/";
        String outputName = "open-interface-report.html";

        File outputFile = new File(targetDirectory + outputName);


        if (outputFile.exists()) {
            outputFile.delete();
        }

        try {
            outputFile.createNewFile();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write open interface test results.");
        }

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputFile), "UTF-8"))) {
            out.write(output);

        } catch (IOException ex) {
            String message = "Error writing HTML file: " + ex.getMessage();
            throw new RuntimeException(message);
        }
    }
}
