package pl.pazurkiewicz.oldtimers_rally.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.pazurkiewicz.oldtimers_rally.MyConfigurationProperties;
import pl.pazurkiewicz.oldtimers_rally.model.Crew;
import pl.pazurkiewicz.oldtimers_rally.model.Event;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadService {

    private final String resourceLocation;

    public FileUploadService(MyConfigurationProperties configurationProperties) {
        this.resourceLocation = configurationProperties.getResourceLocation();
    }

    public String saveFile(String uploadDir, String fileName,
                           MultipartFile multipartFile) throws IOException {
        String realDir = resourceLocation + "/" + uploadDir;
        Path uploadPath = Paths.get(realDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        } else {
            deleteFromPath(realDir);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return String.format("/photos/%s/%s", uploadDir, fileName);
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
        return String.format("%d/%d", crew.getEvent().getId(), crew.getId());
    }

    private String generateBasicPathForEvent(Event event) {
        return String.format("%d", event.getId());
    }

    public void deleteForCrew(Crew crew) throws IOException {
        deleteFromPath(generatePathForCrew(crew));
    }

    public String savePhotoForCrew(Crew crew, MultipartFile photo) throws IOException {
        return saveFile(generatePathForCrew(crew), "main." + getExtension(photo), photo);
    }

    public String cleanPath(String path) {
        return path.replaceFirst("/photos", "");
    }

    public String saveMainEventPhoto(Event event, MultipartFile photo) throws IOException {
        if (event.getMainPhoto() != null) {
            removeOldPhoto(resourceLocation + cleanPath(event.getMainPhoto()));
        }
        return saveFile(generateBasicPathForEvent(event), "mainPhoto." + getExtension(photo), photo);
    }

    private String getExtension(MultipartFile photo) {
        String extension = "jpg";
        if (photo.getOriginalFilename() != null) {
            String[] fileFrags = photo.getOriginalFilename().split("\\.");
            if (fileFrags.length > 0) {
                extension = fileFrags[fileFrags.length - 1];
            }
        }
        return extension;
    }

    private void removeOldPhoto(String fullPath) {
        File file = new File(fullPath);
        if (!file.isDirectory()) {
            file.delete();
        }
    }
}
