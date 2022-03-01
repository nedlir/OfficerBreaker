package officerunlocker;

import java.awt.Desktop;
import java.io.File;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class Controller {

    private final String EXCEL = "xlsx";
    private final String POWERPOINT = "pptx";
    private final String WORD = "docx";

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Text filePathText;
    private File file;
    private FileChooser fileChooser;
    private String filePath;
    private String fileName;
    private String fileType;

    @FXML
    private ImageView image;
    private InputStream stream;
    private Image wordImage;
    private Image excelImage;
    private Image powerpointImage;
    private Image openingImage;

    // opening screen instructions 
    @FXML
    private Text textInstructions;

    // opening screen title
    @FXML
    private Text textTitle;

    @FXML
    private Text invalidFileText;

    @FXML
    public void initialize() {
        anchorPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        invalidFileText.setVisible(false);

        textInstructions.setText(instructions());

        try {
            openingImage = new Image(getClass().getResourceAsStream("img/opening.png"));


            wordImage = new Image(getClass().getResourceAsStream("img/word.png"));

            powerpointImage = new Image(getClass().getResourceAsStream("img/powerpoint.png"));

            excelImage = new Image(getClass().getResourceAsStream("img/excel.png"));

            image.setImage(openingImage);
            image.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void browsePressed(ActionEvent event) {

        // remove text from screen
        textTitle.setVisible(false);
        textInstructions.setVisible(false);
        invalidFileText.setVisible(false);

        fileChooser = new FileChooser();
        fileChooser.setTitle("Please Select a File");
        file = fileChooser.showOpenDialog(null);
        filePath = file.toString();
        fileName = file.getName();
        filePathText.setText(filePath);
        fileType = getFileType();

        if (fileType != null) {
            setImageByFileType();

        }
    }

    @FXML
    void removePasswordPressed(ActionEvent event) {
        if (file == null) {
            return;
        }
        if (!isFileSupported()) {
            invalidFileText.setVisible(true);
            return;
        }
        try {
            FileManipulator manipulator = new FileManipulator(filePath, "./"); // create new object to manipulate the file

            manipulator.extractFile(); // extract XML from file

            XMLParser parser = new XMLParser(manipulator); // create new object to parse the XML

            if (parser.isExistSecurityElement()) // if the file has a password
            {
                parser.removeElementOfSecurity(); // remove hash node from XML file

                parser.writeToXMLFile(); // write to XML the change

                manipulator.insertFile(); // write back to file

                manipulator.removeXMLFile();

                System.out.println("Finished");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void openCioccolatodorimaURL(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URL("https://twitter.com/cioccolato_kun").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isFileSupported() {
        if (fileType.equals(EXCEL) || fileType.equals(WORD) || fileType.equals(POWERPOINT)) {
            return true;
        } else {
            return false;
        }
    }

    private void setImageByFileType() {
        switch (fileType) {
            case EXCEL:
                image.setImage(excelImage);
                break;
            case WORD:
                image.setImage(wordImage);
                break;
            case POWERPOINT:
                image.setImage(powerpointImage);
                break;
            default:
                break;
        }
    }

    private String getFileType() {
        int indexOfLastDot = this.fileName.lastIndexOf(".");
        if (indexOfLastDot == -1) // dot not found, invalid file
        {
            return null;
        }
        return this.fileName.substring(indexOfLastDot + 1);
    }

    private String instructions() {
        return "Welcome to Office Breaker!\n" + "This tool is deisgned to help remove read-only protection from .pptx, .xlsx. .docx filetypes.";
    }

}
