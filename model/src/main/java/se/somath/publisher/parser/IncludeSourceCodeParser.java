package se.somath.publisher.parser;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import se.somath.publisher.excpetion.PublishException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;


public class IncludeSourceCodeParser {
    private String root;
    private String fileName;
    private String fileDisplayName;
    private boolean shouldDisplayFileName = false;

    public void parse(String include) {
        Document document;
        try {
            document = parseIncludeString(include);
        } catch (ParserConfigurationException e) {
            throw new PublishException(e);
        } catch (SAXException e) {
            throw new PublishException(e);
        } catch (IOException e) {
            throw new PublishException(e);
        }

        NamedNodeMap attributes = findIncludeTag(document);
        root = findRootName(attributes);
        fileName = findFileName(attributes);
        fileDisplayName = findFileDisplayName(attributes);
        shouldDisplayFileName = findShouldDisplayFileName(attributes);
    }

    public String getRoot() {
        return root;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileDisplayName() {
        return fileDisplayName;
    }

    public boolean shouldDisplayFileName() {
        return shouldDisplayFileName;
    }

    private Document parseIncludeString(String include) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringReader reader = new StringReader(include);
        InputSource source = new InputSource(reader);
        return builder.parse(source);
    }

    private NamedNodeMap findIncludeTag(Document document) {
        String tagName = "include-source-code";
        NodeList elements = document.getElementsByTagName(tagName);
        int theOnlyExpectedElement = 0;
        Node item = elements.item(theOnlyExpectedElement);

        return item.getAttributes();
    }

    private String findRootName(NamedNodeMap attributes) {
        String attributeName = "root";
        return getNodeValue(attributes, attributeName);
    }

    private String findFileName(NamedNodeMap attributes) {
        String attributeName = "file";
        return getNodeValue(attributes, attributeName);
    }

    private String findFileDisplayName(NamedNodeMap attributes) {
        String attributeName = "fileDisplayName";
        return getNodeValue(attributes, attributeName);
    }

    private boolean findShouldDisplayFileName(NamedNodeMap attributes) {
        String attributeName = "displayFileName";
        String nodeValue = getNodeValue(attributes, attributeName);

        return !nodeValue.equalsIgnoreCase("false");
    }

    private String getNodeValue(NamedNodeMap attributes, String attributeName) {
        Node item = attributes.getNamedItem(attributeName);
        if (item != null) {
            return item.getNodeValue();
        } else {
            return "";
        }
    }
}