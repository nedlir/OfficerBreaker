package officerbreaker;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManipulator {

    public final static String EXCEL_FILE = "xlsx";
    public final static String POWER_POINT_FILE = "pptx";
    public final static String WORD_FILE = "docx";

    public final static String EXCEL_XML = "workbook.xml";
    public final static String POWER_POINT_XML = "presentation.xml";
    public final static String WORD_XML = "settings.xml";

    public final static String EXCEL_PATH = "xl/";
    public final static String POWER_POINT_PATH = "ppt/";
    public final static String WORD_PATH = "word/";

    public final static String EXCEL_SECURITY_NODE = "workbookProtection";
    public final static String POWER_POINT_SECURITY_NODE = "p:modifyVerifier";
    public final static String WORD_SECURITY_NODE = "w:documentProtection";

    private String filePath; // path of file which will be manipulated
    private String extractionPath; // temporary path where the file will be extracted to
    private String fileType; // file type (extension name)
    private String XMLTargetFile; // XML file to be extracted from file
    private String targetPath; // path to XML inside the file
    private String securityElement; // hashed password element

    public FileManipulator(String filePath, String extractionPath) throws Exception {
        this.filePath = filePath;
        this.extractionPath = extractionPath;
        this.fileType = determineFileType();
        locateTargetFileAndPath();

    }

    public void extractFile() throws Exception {
        File outputLocation = new File(this.extractionPath, this.XMLTargetFile);

        // path to the file the file will be extracted from
        Path zipFile = Paths.get(this.filePath);

        // load zip file as filesystem
        FileSystem fileSystem = FileSystems.newFileSystem(zipFile, null);

        Path source = fileSystem.getPath(this.targetPath + this.XMLTargetFile); // location of XMLTargetFile inside the zip file

        Files.copy(source, outputLocation.toPath());
    }

    public void insertFile() throws Exception {
        Path myFilePath = Paths.get(this.XMLTargetFile);

        Path zipFilePath = Paths.get(this.filePath);
        try (FileSystem fs = FileSystems.newFileSystem(zipFilePath, null)) {
            Path fileInsideZipPath = fs.getPath(this.targetPath + this.XMLTargetFile);
            Files.delete(fileInsideZipPath);
            Files.copy(myFilePath, fileInsideZipPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String determineFileType() throws Exception {
        int indexOfLastDot = this.filePath.lastIndexOf(".");
        if (indexOfLastDot == -1) // dot not found, invalid file
        {
            throw new Exception("invalid input, file extension not found.");
        } else {
            return filePath.substring(indexOfLastDot + 1);
        }
    }

    public void locateTargetFileAndPath() throws Exception {
        if (fileType.equals(POWER_POINT_FILE)) {
            this.XMLTargetFile = POWER_POINT_XML;
            this.targetPath = POWER_POINT_PATH;
            this.securityElement = POWER_POINT_SECURITY_NODE;
        } else if (fileType.equals(WORD_FILE)) {
            this.XMLTargetFile = WORD_XML;
            this.targetPath = WORD_PATH;
            this.securityElement = WORD_SECURITY_NODE;
        } else if (fileType.equals(EXCEL_FILE)) {
            this.XMLTargetFile = EXCEL_XML;
            this.targetPath = EXCEL_PATH;
            this.securityElement = EXCEL_SECURITY_NODE;
        } else // file not recognized
        {
            throw new Exception("invalid input, file type not supported.");
        }
    }

        public void removeXMLFile() throws Exception {
        File file = new File(this.XMLTargetFile);
        Files.deleteIfExists(file.toPath());
    }
        
    public String getXMLTargetFile() {
        return this.XMLTargetFile;
    }

    public String getSecurityElement() {
        return this.securityElement;
    }
}
