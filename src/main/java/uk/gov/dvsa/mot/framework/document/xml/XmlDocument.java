package uk.gov.dvsa.mot.framework.document.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.gov.dvsa.mot.framework.document.Document.IDocument;
import uk.gov.dvsa.mot.framework.document.Document.Type;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
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

    @Override
    public boolean contains(List<String> values) throws Exception {
        // normalize text representation
        xmlData.getDocumentElement ().normalize ();
        System.out.println ("Root element of the doc is " +
                xmlData.getDocumentElement().getNodeName());


        NodeList listOfPersons = xmlData.getElementsByTagName("person");
        int totalPersons = listOfPersons.getLength();
        System.out.println("Total no of people : " + totalPersons);

        for(int s=0; s<listOfPersons.getLength() ; s++){


            Node firstPersonNode = listOfPersons.item(s);
            if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE){


                Element firstPersonElement = (Element)firstPersonNode;

                //-------
                NodeList firstNameList = firstPersonElement.getElementsByTagName("first");
                Element firstNameElement = (Element)firstNameList.item(0);

                NodeList textFNList = firstNameElement.getChildNodes();
                System.out.println("First Name : " +
                        ((Node)textFNList.item(0)).getNodeValue().trim());

                //-------
                NodeList lastNameList = firstPersonElement.getElementsByTagName("last");
                Element lastNameElement = (Element)lastNameList.item(0);

                NodeList textLNList = lastNameElement.getChildNodes();
                System.out.println("Last Name : " +
                        ((Node)textLNList.item(0)).getNodeValue().trim());

                //----
                NodeList ageList = firstPersonElement.getElementsByTagName("age");
                Element ageElement = (Element)ageList.item(0);

                NodeList textAgeList = ageElement.getChildNodes();
                System.out.println("Age : " +
                        ((Node)textAgeList.item(0)).getNodeValue().trim());

                //------


            }//end of if clause


        }//end of for loop with s var

    }

    @Override
    public boolean contains(String value) {
        return false;
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

    private boolean checkIfAnyNodeContains(NodeList nodeList, List<String> values) {
        for (int i = 0; i < nodeList.getLength(); ++i) {
            if (nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
                for (String value : values) {
                    if (nodeList.item(i).getTextContent().equals(value)) {
                        values.remove(value);
                    }
                }
            } else {
                return checkIfAnyNodeContains(nodeList.item(i).getChildNodes(), values);
            }
        }

        return false;
    }
}
