package uk.gov.dvsa.mot.framework.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CsvDocumentTest {

    private static CsvDocument csvDocument = null;

    @After
    public void clean() {
        csvDocument = null;
    }

    /**
     * Tests <code>CsvDocument.load()</code> and <code>CsvDocument.parse()</code> with an invalid CSV file.
     */
    @Test(expected = RuntimeException.class)
    public void loadInvalidCsv() {
        try {
            String fileName = new File("").getAbsolutePath()
                    + "/src/test/resources/exampleCsv/invalidCSV.csv";

            csvDocument = loadDocument(fileName);
        } catch (CsvException failedToLoadCsv) {
            throw new RuntimeException(failedToLoadCsv.getMessage());
        }
    }

    /**
     * Tests <code>CsvDocument.load()</code> and <code>CsvDocument.parse()</code> with an invalid CSV file.
     */
    @Test
    public void loadValidCsv() {
        try {
            String fileName = new File("").getAbsolutePath()
                    + "/src/test/resources/exampleCsv/validCSV.csv";

            csvDocument = loadDocument(fileName);

            assertTrue(csvDocument.getValue(0, 0).equals("column1"));
        } catch (CsvException failedToLoadCsv) {
            throw new RuntimeException(failedToLoadCsv.getMessage());
        }
    }

    /**
     * Test <code>contains()</code> function with valid and invalid data.
     */
    @Test
    public void contains() {
        try {
            String fileName = new File("").getAbsolutePath()
                    + "/src/test/resources/exampleCsv/validCSV.csv";

            csvDocument = loadDocument(fileName);

            assertTrue(csvDocument.contains("column2"));
            assertTrue(csvDocument.contains("4asdfads"));
            assertTrue(csvDocument.contains("W"));

            assertFalse(csvDocument.contains("d098sdf"));
            assertFalse(csvDocument.contains("X"));
            assertFalse(csvDocument.contains("123"));
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

            return CsvDocument.load(rawDocument.toString());
        } catch (IOException io) {
            throw new RuntimeException(io.getMessage());
        }
    }
}
