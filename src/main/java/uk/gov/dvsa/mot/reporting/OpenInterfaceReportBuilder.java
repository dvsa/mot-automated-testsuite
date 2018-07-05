package uk.gov.dvsa.mot.reporting;

import uk.gov.dvsa.mot.framework.FileWriter;
import uk.gov.dvsa.mot.framework.html.HtmlWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to build reports for open interface tests.
 */
public class OpenInterfaceReportBuilder {

    private static final String PASS_CLASS = "pass";
    private static final String FAIL_CLASS = "fail";
    private static final String SPACER_CLASS = "spacer";

    private static final Map<String, String> PASS_ATTRIBUTES = new HashMap<String, String>() {
        {
            put("class", PASS_CLASS);
        }
    };
    private static final Map<String, String> FAIL_ATTRIBUTES = new HashMap<String, String>() {
        {
            put("class", FAIL_CLASS);
        }
    };

    /**
     * Generate a report from the test results.
     *
     * @param registration of the vehicle used for test
     * @param response from the open interface
     * @param expectedValues for the xpath
     * @param responseValues for the xpath located in response
     * @param results of the comparisons
     */
    public static void generateResponseReport(String queryName, String registration, String response,
                                              Map<String, String> expectedValues, Map<String, String> responseValues,
                                              Map<String, Boolean> results) {
        StringBuilder resultsHtml = new StringBuilder();


        for (String key : expectedValues.keySet()) {
            resultsHtml.append( HtmlWriter.tag("tr",
                    (results.get(key) ? PASS_ATTRIBUTES : FAIL_ATTRIBUTES),
                    HtmlWriter.tag("td", null, key)
                            + HtmlWriter.tag("td", null, expectedValues.get(key))
                            + HtmlWriter.tag("td", null, responseValues.get(key))));
        }

        Map<String, String> colspanTwoAttribute = new HashMap<>();
        colspanTwoAttribute.put("colspan", "2");

        String vrmHeading = HtmlWriter.tag("tr", null,
                HtmlWriter.tag("th", null, "Vehicle VRM:")
                        + HtmlWriter.tag("td", colspanTwoAttribute, registration));

        String tableHeadingHtml = HtmlWriter.tag("tr", null,
                HtmlWriter.tag("th", null, "xPath")
                        + HtmlWriter.tag("th", null, "Expected Value")
                        + HtmlWriter.tag("th", null, "Response Value"));

        String responseHtml = HtmlWriter.tag("tr", null,
                HtmlWriter.tag("th", null, "Response:")
                        + HtmlWriter.tag("td", colspanTwoAttribute,
                        HtmlWriter.tag("xmp", null, response)));

        Map<String, String> spacerTdAttributes = new HashMap<>();
        spacerTdAttributes.put("colspan", "3");
        Map<String, String> spacerTrAttributes = new HashMap<>();
        spacerTrAttributes.put("class", SPACER_CLASS);

        String spacerHtml = HtmlWriter.tag("tr", spacerTrAttributes,
                HtmlWriter.tag("td", spacerTdAttributes, ""));

        String responseReport = HtmlWriter.tag("h1", null, queryName + ":")
                + HtmlWriter.tag("table", null, vrmHeading
                + spacerHtml + tableHeadingHtml + resultsHtml
                + spacerHtml + responseHtml);

        FileWriter.writeFile(OpenInterfaceReportConfig.RESPONSE_REPORT_TARGET_DIR,
                OpenInterfaceReportConfig.BASE_RESPONSE_FILE_NAME.replace("DESC", queryName),
                responseReport, OpenInterfaceReportConfig.EXTENSION,
                false, true,true);
    }

    /**
     * Generate a final report, containg all scenario reports in it.
     */
    public static void generateFinalReport() {
        File sourceDir = new File(OpenInterfaceReportConfig.RESPONSE_REPORT_TARGET_DIR);
        File[] sourceFileList;

        if (sourceDir.exists()) {
            sourceFileList = sourceDir.listFiles();
        } else {
            return;
        }

        if (sourceFileList == null && sourceFileList.length == 0) {
            return;
        }

        String output = "";

        for (File file : sourceFileList) {
            try {
                output += HtmlWriter.tag("div", null,
                        new String(Files.readAllBytes(file.toPath())));
            } catch (IOException ex) {
                String message = String.format("Failed to read file %s. Stacktrace: %s", file.getName(),
                        ex.getMessage());

                throw new RuntimeException(message);
            }

        }

        String styleContent = "h1:first-letter{text-transform:uppercase;}"
                + "xmp{white-space:pre-wrap;word-wrap:break-word;}"
                + "div{margin-bottom:5rem;}"
                + "." + FAIL_CLASS + "{background-color:#ff7676;}"
                + "." + PASS_CLASS + "{background-color:#94fab1;}"
                + "table{max-width:400px;border-collapse:collapse;}"
                + "table td,table th{padding:0.2rem 0.6rem;border:1px solid black;}"
                + "table th{background-color: #a3c8ff;}"
                + "textarea{width:100%;height:100%;box-sizing:border-box;resize:none;}"
                + "." + SPACER_CLASS + "{background-color:rgb(216,216,216);height:1.4rem;}"
                + "xmp{white-space: break-word;}";

        output = HtmlWriter.tag("head", null,
                HtmlWriter.tag("title", null, "Open Interface Test Report")
                        + HtmlWriter.tag("style", null, styleContent))
                + HtmlWriter.tag("body", null, output.toString());

        output = HtmlWriter.tag("html", null, output);

        FileWriter.writeFile(OpenInterfaceReportConfig.FINAL_REPORT_TARGET_DIR,
                OpenInterfaceReportConfig.FINAL_REPORT_FILE_NAME,
                output, OpenInterfaceReportConfig.EXTENSION, false, true, true);
    }
}
