package uk.gov.dvsa.mot.framework.document.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.gov.dvsa.mot.framework.document.Document.IDocument;
import uk.gov.dvsa.mot.framework.document.Document.Type;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class XmlDocument implements IDocument {
    private final static Type fileType = Type.XML;

    private org.w3c.dom.Document xmlData;

    private String rawXmlData;

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

    public boolean contains(Map<String, String> valuesToFind) throws Exception {
        xmlData.getDocumentElement ().normalize ();
        System.out.println ("Root element of the doc is " +
                xmlData.getDocumentElement().getNodeName());

        for (String xpathString : valuesToFind.keySet()) {
            XPath xpath = XPathFactory.newInstance().newXPath();

            NodeList nodes = (NodeList) xpath.evaluate(xpathString, xmlData, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); ++i) {
                if (!nodes.item(i).getTextContent().equals(valuesToFind.get(xpathString))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean contains(List<String> values) throws Exception {
        for (String value : values) {
            if (!contains(value)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean contains(String value) {
        if (!rawXmlData.contains(value)) {
            return false;
        }

        return true;
    }

    @Override
    public String parse() {
        return rawXmlData;
    }

    @Override
    public void write(String filePath, Optional<Boolean> overwrite) {

    }

    @Override
    public Type getFileType() {
        return fileType;
    }

    @Override
    public String getExtension() {
        return null;
    }
}
