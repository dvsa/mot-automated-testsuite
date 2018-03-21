package uk.gov.dvsa.mot.server.reporting;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class DocumentListReportGenerator {

    private static Map<String, Map<String, String>> documentResults = new HashMap<>();

    /** Time stamp, of when the testsuite was started. */
    private static final String timestamp = DateTime.now().toString("dd-MM-yyyy_HH-mm-ss");

    /**
     * Add a document result.
     * @param fileResult to add
     */
    public static void addDocumentResult(String fileResult) {
        HashMap<String, String> resultMap = new HashMap<>();

        String[] result = fileResult.split(";");
        resultMap.put(result[1], result[2]);

        documentResults.put(result[0], resultMap);
    }

    /**
     * Get document results.
     * @return results
     */
    public static String getDocumentResults() {
        StringBuilder output = new StringBuilder();

        output.append(timestamp + "\n");
        for (String filename : documentResults.keySet()) {
            for (String featureScenario : documentResults.get(filename).keySet()) {
                String feature = null;
                String scenario = null;

                if (featureScenario.contains("_")) {
                    feature = featureScenario.split("_")[0];
                    scenario = featureScenario.split("_")[1];
                }

                output.append(filename + ";"
                        + (feature != null ? feature + ";" : "")
                        + scenario + ";"
                        + documentResults.get(filename).get(featureScenario) + "\n");
            }
        }

        return output.toString();
    }

    /**
     * Get timestamp used by the DocumentListReportGenerator.
     * @return timestamp
     */
    public static String getTimestamp() {
        return timestamp;
    }
}
