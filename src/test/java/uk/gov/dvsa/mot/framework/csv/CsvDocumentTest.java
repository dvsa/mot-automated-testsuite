package uk.gov.dvsa.mot.framework.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvDocumentTest {

    /**
     * Test <code>contains()</code> function with valid data.
     */
    @Test
    public void contains() {
        try {
            String fileName = new File("").getAbsolutePath()
                    + "/src/test/resources/exampleCsv/validCSV.csv";

            CsvDocument csvDocument = loadDocument(fileName);

            assertTrue(csvDocument.contains("column2"));

            List<String> containsList = new ArrayList<String>();
            containsList.add("column3");
            containsList.add("4");
            containsList.add("a");
            containsList.add("10%");
            containsList.add("a,b,c");

            assertTrue(csvDocument.contains(containsList));

        } catch (CsvException failedToLoadCsv) {
            throw new RuntimeException(failedToLoadCsv.getMessage());
        }
    }

    /**
     * Test <code>contains()</code> function with invalid data.
     */
    @Test
    public void notContains() {
        try {
            String fileName = new File("").getAbsolutePath()
                    + "/src/test/resources/exampleCsv/validCSV.csv";

            CsvDocument csvDocument = loadDocument(fileName);

            assertFalse(csvDocument.contains("column5")); // Should not exist

            List<String> containsList = new ArrayList<String>();
            containsList.add("column3");
            containsList.add("9"); // Should not exist
            containsList.add("a");

            assertFalse(csvDocument.contains(containsList));

        } catch (CsvException failedToLoadCsv) {
            throw new RuntimeException(failedToLoadCsv.getMessage());
        }
    }

    /**
     * Test <code>contains()</code> function with valid and invalid data.
     */
    @Test(expected = RuntimeException.class)
    public void invalidFile() {
        try {
            String fileName = new File("").getAbsolutePath()
                    + "/src/test/resources/exampleCsv/invalidCSV.csv";

            CsvDocument csvDocument = loadDocument(fileName);
        } catch (CsvException failedToLoadCsv) {
            throw new RuntimeException(failedToLoadCsv.getMessage());
        }
    }

    /**
     * Loads and returns a CSV document.
     *
     * @param filePath path to the target file.
     * @return loaded document.
     */
    private static CsvDocument loadDocument(String filePath) throws CsvException {
        try {
            BufferedReader file = new BufferedReader(new FileReader(filePath));

            StringBuilder rawDocument = new StringBuilder();

            for (;;) {
                String line = file.readLine();
                if (line == null) {
                    break;
                }

                rawDocument.append(line);
                rawDocument.append("\n");
            }

            return new CsvDocument(CSVParser.parse(rawDocument.toString(), CSVFormat.DEFAULT));
        } catch (IOException io) {
            throw new RuntimeException(io.getMessage());
        }
    }
}
