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

public class IncludeFileTreeParser {
    private String root;

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
    }

    public String getRoot() {
        return root;
    }

    private Document parseIncludeString(String include) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringReader reader = new StringReader(include);
        InputSource source = new InputSource(reader);
        return builder.parse(source);
    }

    private NamedNodeMap findIncludeTag(Document document) {
        String tagName = "include-file-tree";
        NodeList elements = document.getElementsByTagName(tagName);
        int theOnlyExpectedElement = 0;
        Node item = elements.item(theOnlyExpectedElement);

        return item.getAttributes();
    }

    private String findRootName(NamedNodeMap attributes) {
        String attributeName = "root";
        return getNodeValue(attributes, attributeName);
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