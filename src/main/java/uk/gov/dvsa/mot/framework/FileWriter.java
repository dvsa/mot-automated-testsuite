package uk.gov.dvsa.mot.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Class to write files.
 */
public class FileWriter {
    private static final Logger logger = LoggerFactory.getLogger(FileWriter.class);

    /**
     * Write file to the specified location.
     *
     * @param directory to which the file should be written to
     * @param name of the file
     * @param content of the file
     * @param extension of the file
     * @param clearDirectoryBeforeWriting whether directory should be deleted first
     * @param removeFileIfExists whether file should be deleted first
     * @param throwExceptionOnFailure whether the exceptions should be thrown in case of failure
     */
    public static void writeFile(String directory, String name, String content, String extension,
                                 boolean clearDirectoryBeforeWriting, boolean removeFileIfExists,
                                 boolean throwExceptionOnFailure) {
        File file = new File(directory + name + "." + extension);
        File fileDir = new File(directory);

        if (fileDir.exists()) {
            if (clearDirectoryBeforeWriting) {
                fileDir.delete();
            }
        } else {
            fileDir.mkdirs();
        }

        if (removeFileIfExists && file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();

        } catch (IOException ex) {
            String message = "Failed to create new file. Details: " + ex.getMessage();

            logger.error(message);

            if (throwExceptionOnFailure) {
                throw new RuntimeException(message);
            }
        }

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file)))) {
            out.write(content);

        } catch (IOException ex) {
            String message = "Error writing HTML file: " + ex.getMessage();

            logger.error(message);

            if (throwExceptionOnFailure) {
                throw new RuntimeException(message);
            }
        }
    }
}