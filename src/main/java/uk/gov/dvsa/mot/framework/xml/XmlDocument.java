package uk.gov.dvsa.mot.framework.xml;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import uk.gov.dvsa.mot.framework.pdf.PdfDocument;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class XmlDocument {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(XmlDocument.class);

    /** Apache xml document. */
    private org.w3c.dom.Document xmlData;

    /** Xml document string. */
    private String rawXmlData;

    /**
     * Instantiates new object.
     * @param rawXml to create the document from.
     * @throws XmlException error loading the document.
     */
    public XmlDocument(String rawXml) throws XmlException {
        rawXmlData = rawXml;

        try {
            InputStream xmlStream = new ByteArrayInputStream(rawXml.getBytes());

            xmlData = parseXmlData(xmlStream);
        } catch (Exception exception) {
            throw new XmlException("Unable to create XML Document", exception);
        }
    }

    private org.w3c.dom.Document parseXmlData(InputStream xmlStream) throws Exception {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            return documentBuilder.parse(xmlStream);
        } catch (Exception exception) {
            throw new XmlException("Unable to parse XML Document", exception);
        }
    }

    /**
     * Check if document contains values by xpath.
     * @param valuesToFind map of xpath and corresponding values to look for.
     * @return whether the document contains the values.
     * @throws Exception error evaluating xpath expression.
     */
    public boolean contains(Map<String, String> valuesToFind) throws Exception {
        xmlData.getDocumentElement().normalize();

        for (String xpathString : valuesToFind.keySet()) {
            String xpathValue = getxPath(xpathString);
            String expectedValue = valuesToFind.get(xpathString);

            if (!expectedValue.equals(xpathValue)) {
                return false;
            }
        }

        logger.debug(String.format("XmlDocument contains all values: %s.",
                StringUtils.join(valuesToFind, ", ")));

        return true;
    }

    /**
     * Check whether the document contains values.
     * @param values to look for.
     * @return whether the document contains the values or not.
     */
    public boolean contains(List<String> values) {
        for (String value : values) {
            logger.info(String.format("Checking if XmlDocument contains %s.", value));
            if (!contains(value)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check whether the document contains a value.
     * @param value to look for.
     * @return whether the document contains the value or not.
     */
    public boolean contains(String value) {
        if (!rawXmlData.contains(value)) {
            logger.info(String.format("XmlDocument does not contain %s.", value));

            return false;
        }
        logger.info(String.format("XmlDocument contains %s.", value));

        return true;
    }

    /**
     * Get xPath value.
     *
     * @param xPathString to get the value from.
     * @return xPath value
     * @throws Exception if getting the value fails.
     */
    public String getxPath(String xPathString) throws Exception {
        XPath xpath = XPathFactory.newInstance().newXPath();

        NodeList nodes = (NodeList) xpath.evaluate(xPathString, xmlData, XPathConstants.NODESET);
        String nodeText = "";

        if (nodes.getLength() > 1) {
            throw new RuntimeException("xPath string too ambiguous, getting too many results. xPath string: "
                    + xPathString);
        } else if (nodes.getLength() == 0) {
            throw new RuntimeException("No matching xPath found, xPath string:"
                    + xPathString);
        } else {
            return nodes.item(0).getTextContent();
        }
    }
}
