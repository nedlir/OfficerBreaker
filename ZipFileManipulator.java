import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileManipulator {

    private String fileName; // file which will be extracted from
    private String extractionPath; // temporary path where the file will be extracted to

    public ZipFileManipulator(String fileName, String extractionPath) {
        this.fileName = fileName;
        this.extractionPath = extractionPath;
    }

    public void extractFile(String targetFile) throws Exception {
        File outputLocation = new File(extractionPath, targetFile);

        // path to the file the file will be extracted from
        Path zipFile = Paths.get(fileName);

        // load zip file as filesystem
        FileSystem fileSystem = FileSystems.newFileSystem(zipFile, null);

        Path source = fileSystem.getPath("ppt/" + targetFile); // location of targetFile inside the zip file

        Files.copy(source, outputLocation.toPath());
    }

    public void insertFile(String targetFile) throws Exception {
      Path myFilePath = Paths.get(targetFile);
  
      Path zipFilePath = Paths.get(this.fileName);
      try( FileSystem fs = FileSystems.newFileSystem(zipFilePath, null) ){
          Path fileInsideZipPath = fs.getPath("/ppt/" + targetFile);
          Files.delete(fileInsideZipPath);
          Files.copy(myFilePath, fileInsideZipPath);
          
      } catch (IOException e) {
          e.printStackTrace();
      }
    }

}