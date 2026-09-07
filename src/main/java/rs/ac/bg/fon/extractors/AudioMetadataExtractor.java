package rs.ac.bg.fon.extractors;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.exceptions.MetadataExtractionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class AudioMetadataExtractor {

    public String extractMetadata(Path path) throws IOException, MetadataExtractionException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder("ffprobe", "-v", "quiet", "-print_format", "json", "-show_format", "-show_streams", path.toAbsolutePath().toString());

        Process process = builder.redirectErrorStream(true).start();

        String text = "";
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){

            text = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        boolean exitedInTime = process.waitFor(5, TimeUnit.SECONDS);

        if(exitedInTime){
            int exitCode = process.exitValue();
            if(exitCode != 0){
                throw new MetadataExtractionException("Process exited with faulty code");
            }
            return text;
        }
        else{
            process.destroyForcibly();
            throw new MetadataExtractionException("Process took too long to finish");
        }
    }

}
