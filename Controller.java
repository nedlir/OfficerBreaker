package officerbreaker;

import java.awt.Desktop;
import java.io.File;
import javafx.scene.image.Image;
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
    private ImageView titleImageView;

    @FXML
    private ImageView fileImageView;
    private Image wordImage;
    private Image excelImage;
    private Image powerpointImage;

    @FXML
    private Text fileText;

    @FXML
    private Text invalidFileText;

    @FXML
    public void initialize() {
        anchorPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        invalidFileText.setVisible(false); // hides the invalid file text

        try {
            wordImage = new Image(getClass().getResourceAsStream("img/word.png"));

            powerpointImage = new Image(getClass().getResourceAsStream("img/powerpoint.png"));

            excelImage = new Image(getClass().getResourceAsStream("img/excel.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void browsePressed(ActionEvent event) {

        invalidFileText.setVisible(false);

        try {
            fileChooser = new FileChooser();
            fileChooser.setTitle("Please Select a File");
            file = fileChooser.showOpenDialog(null);
            filePath = file.toString();
            fileName = file.getName();
            filePathText.setText(filePath);
            fileType = getFileType();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fileType != null && isFileSupported()) {
            setScreenByFileType();
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

                manipulator.removeXMLFile(); // cleans the xml extracted from origin file

                fileText.setText(fileFinishedMessage());
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

    private void setScreenByFileType() {
        titleImageView.setVisible(false);
        
        switch (fileType) {
            case EXCEL:
                fileImageView.setImage(excelImage);
                fileText.setText(fileRecognizedMessage("Excel"));
                break;
            case WORD:
                fileImageView.setImage(wordImage);
                fileText.setText(fileRecognizedMessage("Word"));
                break;
            case POWERPOINT:
                fileImageView.setImage(powerpointImage);
                fileText.setText(fileRecognizedMessage("PowerPoint"));
                break;
            default:
                break;
        }
    }

    private String getFileType() {
        int indexOfLastDot = fileName.lastIndexOf(".");
        if (indexOfLastDot == -1) // dot not found, invalid file
        {
            return null;
        }
        return fileName.substring(indexOfLastDot + 1);
    }
    
    // Messages on screen
    private String fileRecognizedMessage(String fileType){
        String messageOnScreen = "Officer Breaker recognized " + "\"" + fileName + "\"" +" as " + fileType + " file.\n\n";
        messageOnScreen = messageOnScreen + "Press \"Remove Password\" to unprotect the file.";
        return messageOnScreen;
    }
    
        private String fileFinishedMessage(){
        String messageOnScreen = "Yay! Officer Breaker removed the protection from your file!";
        return messageOnScreen;
    }
    
}
