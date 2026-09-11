package rs.ac.bg.fon.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.domain.AudioJob;
import rs.ac.bg.fon.domain.AudioJobStatus;
import rs.ac.bg.fon.dtos.AudioMetadata;
import rs.ac.bg.fon.exceptions.MetadataExtractionException;
import rs.ac.bg.fon.extractors.AudioMetadataExtractor;
import rs.ac.bg.fon.repositories.AudioJobRepository;

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

    private final AudioJobRepository audioJobRepository;

    public AudioService(AudioMetadataExtractor extractor, AudioJobRepository audioJobRepository) {
        this.extractor = extractor;
        this.audioJobRepository = audioJobRepository;
    }

    public AudioJob storeAudioFile(InputStream file, String fileName, long fileSize) throws IOException{

            UUID uuid = UUID.randomUUID();

            Path uploadPath = Paths.get(uploadDirectory);

            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(uuid.toString());

            AudioJob audioJob = new AudioJob(uuid, fileName, filePath.toString(), AudioJobStatus.PENDING);
            audioJobRepository.save(audioJob);

            Files.copy(file, filePath);

            try{
                AudioMetadata metadata = extractor.extractMetadata(filePath);
                audioJob.markCompleted(metadata);

            }
            catch (IOException | MetadataExtractionException | InterruptedException e){
                audioJob.markFailed(e.getMessage());
            }

            audioJobRepository.save(audioJob);
            return audioJob;




    }

}
