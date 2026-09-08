package rs.ac.bg.fon.extractors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.dtos.AudioMetadata;
import rs.ac.bg.fon.exceptions.MetadataExtractionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class AudioMetadataExtractor {

    private final ObjectMapper om;

    public AudioMetadataExtractor(ObjectMapper om) {
        this.om = om;
    }

    public AudioMetadata extractMetadata(Path path) throws IOException, MetadataExtractionException, InterruptedException {
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

        }
        else{
            process.destroyForcibly();
            throw new MetadataExtractionException("Process took too long to finish");
        }

        JsonNode node = om.readTree(text);

        JsonNode format = node.path("format");

        double duration = format.path("duration").asDouble();
        long bitRate = format.path("bit_rate").asLong();
        long size = format.path("size").asLong();

        Iterable<JsonNode> streams = node.path("streams");
        JsonNode stream = null;
        for(JsonNode n : streams){
            String type = n.path("codec_type").asText();
            if(type.equals("audio")){
                stream = n;
                break;
            }
        }

        if(stream == null){
            throw new MetadataExtractionException("Faulty audio data");
        }

        String codecName = stream.path("codec_name").asText();
        int sampleRate = stream.path("sample_rate").asInt();
        int channels = stream.path("channels").asInt();

        return new AudioMetadata(codecName, sampleRate, channels, duration, bitRate, size);
    }

}
