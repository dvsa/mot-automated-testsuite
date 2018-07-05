package uk.gov.dvsa.mot.reporting;

public class OpenInterfaceReportConfig {
    public static final String RESPONSE_REPORT_TARGET_DIR = "target/openinterface/";
    public static final String FINAL_REPORT_TARGET_DIR = "target/";

    // Replace DESC with a unique string that identifies each report, for example query name.
    public static final String BASE_RESPONSE_FILE_NAME = "open-interface-response-report_DESC";
    public static final String FINAL_REPORT_FILE_NAME = "open-interface-report";

    public static final String EXTENSION = "html";
}
