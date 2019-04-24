package uk.gov.dvsa.mot.framework.pdf;

public class MotCertFormFields {
    private String timestamp;
    private String testNumber;
    private String checksum;
    private String vrm;
    private String csrfToken;

    public String getTimestamp() {
        return timestamp;
    }

    public MotCertFormFields setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getTestNumber() {
        return testNumber;
    }

    public MotCertFormFields setTestNumber(String testNumber) {
        this.testNumber = testNumber;
        return this;
    }

    public String getChecksum() {
        return checksum;
    }

    public MotCertFormFields setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    public String getVrm() {
        return vrm;
    }

    public MotCertFormFields setVrm(String vrm) {
        this.vrm = vrm;
        return this;
    }

    public String getV5c() {
        return v5c;
    }

    public MotCertFormFields setV5c(String v5c) {
        this.v5c = v5c;
        return this;
    }

    private String v5c;

    public String getCsrfToken() {
        return csrfToken;
    }

    public MotCertFormFields setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }
}
