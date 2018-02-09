package uk.gov.dvsa.mot.framework.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.document.csv.CsvDocument;
import uk.gov.dvsa.mot.framework.document.pdf.PdfDocument;
import uk.gov.dvsa.mot.framework.document.xml.XmlDocument;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Document {

    private final static Logger logger = LoggerFactory.getLogger(Document.class);

    public enum Type {
        PDF,
        CSV,
        XML,
        PLAIN
    }

    private static final HashMap<Type, String> typeExtension = new HashMap<Type, String>() {{
        put(Type.PDF, "pdf");
        put(Type.CSV, "csv");
        put(Type.XML, "xml");
    }};

    private Document() { }

    public static IDocument getDocument(String rawDocument, Type documentType) throws Exception {
        switch (documentType) {
            case PDF:
                return new PdfDocument(rawDocument);
            case CSV:
                return new CsvDocument(rawDocument);
            case XML:
                return new XmlDocument(rawDocument);
            default:
                throw new DocumentException("Trying to load unsupported document type.", new Exception());
        }
    }

    public static String getExtension(Type documentType) throws Exception {
        switch (documentType) {
            case PDF:
                return typeExtension.get(Type.PDF);
            case CSV:
                return typeExtension.get(Type.CSV);
            case XML:
                return typeExtension.get(Type.XML);
            default:
                throw new DocumentException("Trying to load unsupported document type.", new Exception());
        }
    }

    public interface IDocument {
        boolean contains(List<String> values) throws Exception;
        boolean contains(String value) throws Exception;

        String parse() throws Exception;

        void write(String filePath, Optional<Boolean> overwrite) throws Exception;

        Type getFileType();
        String getExtension() throws Exception;
    }
}
