package pl.pazurkiewicz.oldtimers_rally.utils;

import org.springframework.web.multipart.MultipartFile;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        } else {
            deleteFromPath(uploadDir);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public static void deleteFromPath(String path) throws IOException {
        File[] files = new java.io.File(path).listFiles();
        if (files != null) {
            for (File file : files)
                if (!file.isDirectory())
                    file.delete();
            Path deletePath = Paths.get(path + "/main");
            Files.deleteIfExists(deletePath);
        }
    }

    public static void savePhotoForCrew(Crew crew, MultipartFile photo, String basePath) throws IOException {
        String extension = "jpg";
        if (photo.getOriginalFilename() != null) {
            String[] fileFrags = photo.getOriginalFilename().split("\\.");
            if (fileFrags.length > 0) {
                extension = fileFrags[fileFrags.length - 1];
            }
        }
        saveFile(String.format("%s/%d/%d", basePath, crew.getEvent().getId(), crew.getId()), "main." + extension, photo);
    }
}
