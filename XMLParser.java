
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

    private FileManipulator manipulator;
    private String filePath;
    private final String targetElement; // hashed password element
    private Element securityElement;

    private Document document;

    public XMLParser(FileManipulator manipulator) throws Exception {
        this.manipulator = manipulator;
        this.filePath = manipulator.getXMLTargetFile();
        this.targetElement = manipulator.getSecurityElement();
        this.document = XMLFileToDocument();
        // get the element targetElement (the hashed password element):
        this.securityElement = (Element) document.getElementsByTagName(targetElement).item(0);
    }

    public Document XMLFileToDocument() throws Exception {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        return documentBuilder.parse(new FileInputStream(new File(filePath)));
    }

    public void removeElementOfSecurity() {
        // remove the node:
        if (this.securityElement != null) { // if element == null it means there is no hashed pass
            this.securityElement.getParentNode().removeChild(this.securityElement);
        }

        /*
        if excel file, remove fileSharing and element of security from each sheet
         */
    }

    public void writeToXMLFile() throws Exception {
        // create transformer from document to xml file:
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource dom = new DOMSource(this.document);
        StreamResult streamResult = new StreamResult(new File(this.filePath));

        // transform the XML source to file:
        transformer.transform(dom, streamResult);
    }

    public boolean isExistSecurityElement() {
        // if the element was returned as null during instantiation, it means it doesn't exist
        return (this.securityElement != null);
    }
}
