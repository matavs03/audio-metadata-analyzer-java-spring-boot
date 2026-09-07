package rs.ac.bg.fon.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.exceptions.MetadataExtractionException;
import rs.ac.bg.fon.extractors.AudioMetadataExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AudioService {

    @Value("${storage.upload-dir}")
    private String uploadDirectory;

    private final AudioMetadataExtractor extractor;

    public AudioService(AudioMetadataExtractor extractor) {
        this.extractor = extractor;
    }

    public String storeAudioFile(InputStream file, String fileName, long fileSize) throws IOException, MetadataExtractionException, InterruptedException {

            UUID uuid = UUID.randomUUID();

            Path uploadPath = Paths.get(uploadDirectory);

            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(uuid.toString());

            Files.copy(file, filePath);

            String text = "UUID: " + uuid + " " + extractor.extractMetadata(filePath);
            return text;
    }

}
