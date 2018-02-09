package uk.gov.dvsa.mot.framework.document.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.document.Document;
import uk.gov.dvsa.mot.framework.document.Document.IDocument;
import uk.gov.dvsa.mot.framework.document.Document.Type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CSV document class.
 */
public class CsvDocument implements IDocument {
    private final static Type fileType = Type.CSV;

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(CsvDocument.class);

    private ArrayList<ArrayList<String>> csvData;

    private String rawCsvData;

    /**
     * Default constructor.
     */
    public CsvDocument(String rawCsv) throws CsvException {
        this.rawCsvData = rawCsv;

        try {
            this.csvData = parseCsvData(CSVParser.parse(rawCsv, CSVFormat.DEFAULT));
        } catch (Exception ex) {
            throw new CsvException("Unable to create CSV Document", ex);
        }
    }

    /**
     * Parses the CSV data into a list of lists.
     * @param csvParser         the CSV parser
     * @return                  the CSV data
     * @throws CsvException     error parsing the CSV data
     */
    private ArrayList<ArrayList<String>> parseCsvData(CSVParser csvParser) throws CsvException {
        try {
            ArrayList<ArrayList<String>> csvDataSet = new ArrayList<ArrayList<String>>();
            for (CSVRecord record : csvParser.getRecords()) {
                ArrayList<String> csvDataRow = new ArrayList<String>();

                for (int i = 0; i < record.size(); i++) {
                    csvDataRow.add(record.get(i));
                }
                csvDataSet.add(csvDataRow);
            }

            return csvDataSet;
        } catch (IOException ex) {
            throw new CsvException("Error parsking CSV data", ex);
        }
    }

    /**
     * Check if a target CSV contains a specified field.
     *
     * @param field to search for.
     * @return whether the CSV contains a specified field or not.
     */
    @Override
    public boolean contains(String field) {
        for (ArrayList<String> row : csvData) {
            for (String value : row) {
                if (field.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Whether this CSV document contains all the string values.
     * @param values            list of string values to check for
     * @return                  whether all strings are contained within the CSV
     */
    @Override
    public boolean contains(List<String> values) {
        for (String text : values) {
            if (!contains(text)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("CSV does not contain: " + text);
                }
                return false;
            }
        }

        // All values must exist
        return true;
    }

    @Override
    public String parse() {
        return rawCsvData;
    }

    @Override
    public void write(String filePath, Optional<Boolean> overwrite) {

    }

    @Override
    public Type getFileType() {
        return fileType;
    }

    @Override
    public String getExtension() throws Exception {
        return Document.getExtension(fileType);
    }
}