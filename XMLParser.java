import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class XMLParser {

    private final String TARGET_ELEMENT = "p:modifyVerifier"; // hashed password element
    private String filePath;

    private Document document;

    public XMLParser(String filePath) throws Exception {
        this.filePath = filePath;
        this.document = XMLFileToDocument();
    }

    public Document XMLFileToDocument() throws Exception {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        return documentBuilder.parse(new FileInputStream(new File(filePath)));
    }

    public void removeElementOfSecurity() {
        // get the element TARGET_ELEMENT (the hashed password element):
        Element element = (Element) document.getElementsByTagName(TARGET_ELEMENT).item(0);

        // remove the node:
        if (element != null) // if element == null it means there is no hashed pass
        {
            element.getParentNode().removeChild(element);
        }

    }

    public void writeToXMLFile(String newFilePath) throws Exception {
        // create transformer from document to xml file:
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource dom = new DOMSource(this.document);
        // new file:
        StreamResult streamResult = new StreamResult(new File(newFilePath));

        // transform the XML source to a file:
        transformer.transform(dom, streamResult);
    }
}
