package rs.ac.bg.fon.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.fon.exceptions.MetadataExtractionException;
import rs.ac.bg.fon.services.AudioService;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(value = "/api/audio")
public class AudioController {

    private final AudioService audioService;

    public AudioController(AudioService audioService){
        this.audioService = audioService;
    }

    @PostMapping()
    public ResponseEntity<String> uploadAudio(@RequestParam("file")MultipartFile file) throws IOException {

        try(InputStream in = file.getInputStream()){
            String uuid = audioService.storeAudioFile(in, file.getOriginalFilename(), file.getSize());
            return ResponseEntity.status(HttpStatus.OK).body("Audio file UUID: " + uuid);
        } catch (IOException | MetadataExtractionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

}
