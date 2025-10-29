import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStorage {
    private static final String CLOUD_DIRECTORY = "local_cloud_storage"; // Folder name

    public FileStorage() {
        File dir = new File(CLOUD_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    // Upload file
    public boolean uploadFile(File file) {
        try {
            Path destinationPath = Paths.get(CLOUD_DIRECTORY, file.getName());
            Files.copy(file.toPath(), destinationPath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Download file
    public boolean downloadFile(String fileName, File destinationDir) {
        try {
            Path sourcePath = Paths.get(CLOUD_DIRECTORY, fileName);
            Path destinationPath = destinationDir.toPath().resolve(fileName);
            Files.copy(sourcePath, destinationPath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // List stored files
    public File[] listFiles() {
        File cloudDir = new File(CLOUD_DIRECTORY);
        return cloudDir.listFiles();
    }
}


