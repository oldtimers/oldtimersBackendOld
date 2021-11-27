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

    private final String resourceLocation;

    public FileUploadUtil(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public String saveFile(String uploadDir, String fileName,
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
            return String.format("/%s/%s", uploadDir, fileName);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public void deleteFromPath(String path) throws IOException {
        File[] files = new java.io.File(path).listFiles();
        if (files != null) {
            for (File file : files)
                if (!file.isDirectory())
                    file.delete();
        }
    }

    private String generatePathForCrew(Crew crew) {
        return String.format("%s/%d/%d", resourceLocation, crew.getEvent().getId(), crew.getId());
    }

    public void deleteForCrew(Crew crew) throws IOException {
        deleteFromPath(generatePathForCrew(crew));
    }

    public String savePhotoForCrew(Crew crew, MultipartFile photo) throws IOException {
        String extension = "jpg";
        if (photo.getOriginalFilename() != null) {
            String[] fileFrags = photo.getOriginalFilename().split("\\.");
            if (fileFrags.length > 0) {
                extension = fileFrags[fileFrags.length - 1];
            }
        }
        return saveFile(generatePathForCrew(crew), "main." + extension, photo);
    }
}
