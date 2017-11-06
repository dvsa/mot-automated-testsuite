package uk.gov.dvsa.mot.framework.csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvDocument {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(CsvDocument.class);

    /** This is the 2d array of strings containing values parsed from a requested CSV file. */
    private ArrayList<ArrayList<String>> csvData = null;

    /**
     * Default constructor.
     */
    public CsvDocument() { }

    /**
     * Load a document from a raw CSV string.
     *
     * @param document raw CSV string.
     * @return a new, populated, CsvDocument.
     */
    public static CsvDocument load(String document) throws FailedToLoadCsvException {
        CsvDocument output = new CsvDocument();

        output.csvData = parse(document);

        return output;
    }

    /**
     * Parse values from a raw CSV string.
     *
     * @param document raw CSV string.
     * @return output as 2D string array.
     */
    public static ArrayList<ArrayList<String>> parse(String document) throws FailedToLoadCsvException {
<<<<<<< Updated upstream
        boolean usesDoubleQuotes = false;
=======
        try {
            boolean usesDoubleQuotes = false;
>>>>>>> Stashed changes

        //This pattern is to check if values are wrapped in double quotes
        Pattern pattern = Pattern.compile("(\"([\\s]+)?,([\\s]+)?\")");
        Matcher matcher = pattern.matcher(document);
        int matchingCount = matcher.groupCount();

        String[] rows = document.split(System.getProperty("line.separator"));
        ArrayList<ArrayList<String>> mappedCsv = new ArrayList<>();

        if (matchingCount > 0) {
            usesDoubleQuotes = true;
        }

        for (int i = 0; i < rows.length; ++i) {
            String[] values = rows[i].split( ",");

            mappedCsv.add(new ArrayList<>());

            for (int j = 0; j < values.length; ++j) {
                String value = trimSpaces(values[j], usesDoubleQuotes);
                mappedCsv.get(i).add(usesDoubleQuotes ? stripDoubleQuotesFromValue(value) : value);
            }
<<<<<<< Updated upstream
        }
        return mappedCsv;
=======
            return mappedCsv;
        } catch (RuntimeException runtime) {
            throw new FailedToLoadCsvException(runtime.getMessage());
        }
>>>>>>> Stashed changes
    }

    /**
     * Get a row at a specified index.
     *
     * @param rowIndex index of the row to get.
     * @return entire row with matching index.
     */
    public ArrayList<String> getRow(int rowIndex) {
        return csvData.get(rowIndex);
    }

    /**
     * Get a column at a specified index.
     *
     * @param columnIndex index of the column to get.
     * @return entire column with matching index.
     */
    public ArrayList<String> getColumn(int columnIndex) {
        ArrayList<String> columnData = new ArrayList<>();

        for (int i = 0; i < csvData.size(); ++i) {
            columnData.add(csvData.get(i).get(columnIndex));
        }
        return columnData;
    }

    /**
     * Get value at a specified index.
     *
     * @param rowIndex index of the row to get.
     * @param columnIndex index of the column to get.
     * @return value stored in the CSV.
     */
    public String getValue(int rowIndex, int columnIndex) {
        return csvData.get(rowIndex).get(columnIndex);
    }

    /**
     * Check if a target CSV contains a specified field.
     *
     * @param field to search for.
     * @return whether the CSV contains a specified field or not.
     */
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
     * Get count of all rows in the CSV.
     *
     * @return row count.
     */
    public int getRowCount() {
        if (csvData == null) {
            return -1;
        }

        return csvData.size();
    }

    /**
     * Get count of all columns in the CSV.
     *
     * @return column count. Returns -1 if data contained by object is null.
     */
    public int getColumnCount() {
        if (csvData == null && csvData.get(0) == null) {
            return -1;
        }

        return csvData.get(0).size();
    }

    /**
     * Removes the spaces from value.
     *
     * @param value value to trim spaces from.
     * @return value with removed whitespaces.
     */
    private static String trimSpaces(String value, boolean usesDoubleQuotes) {
        int rightOffset = getWhiteSpaceOffsetRight(value, usesDoubleQuotes);
        int leftOffset = getWhiteSpaceOffsetLeft(value, usesDoubleQuotes);

        return value.substring(leftOffset, value.length() - rightOffset);
    }

    /**
     * Get the offset, from the end of the value, without whitespaces & double quotes.
     *
     * @param value to get the offset from.
     * @param usesDoubleQuotes whether the value is encapsulated in double quotes.
     * @return offset of white space starting from the end of the value.
     */
    private static int getWhiteSpaceOffsetRight(String value, boolean usesDoubleQuotes) {
        int offset = 0;
        for (int i = value.length() - 1; i >= 0; --i) {
            if (value.charAt(i) == ' ' || value.charAt(i) == '\t' || value.charAt(i) == '\n') {
                ++offset;
            } else {
                return offset;
            }
        }

        return offset;
    }

    /**
     * Get the offset, from the beginning of the value, without whitespaces & double quotes.
     *
     * @param value to get the offset from.
     * @param usesDoubleQuotes whether the value is encapsulated in double quotes.
     * @return offset of white space starting from the beginning of the value.
     */
    private static int getWhiteSpaceOffsetLeft(String value, boolean usesDoubleQuotes) {
        int offset = 0;
        for (int i = 0; i < value.length(); ++i) {
            if (value.charAt(i) == ' ' || value.charAt(i) == '\t' || value.charAt(i) == '\n') {
                ++offset;
            } else {
                return offset;
            }
        }

        return offset;
    }

    /**
     * Strip double quotes from a values passed to the function.
     *
     * @param value parsed from the CSV.
     * @return value without leading trailing double quote.
     */
    private static String stripDoubleQuotesFromValue(String value) {
        return value.substring(1, value.length() -  1);
    }
}

