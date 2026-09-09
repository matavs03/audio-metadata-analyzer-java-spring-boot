package rs.ac.bg.fon.domain;

import jakarta.persistence.*;
import rs.ac.bg.fon.dtos.AudioMetadata;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class AudioJob {

    protected AudioJob(){}

    public AudioJob(UUID id, String originalFileName, String filePath, AudioJobStatus status) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.status = status;
    }

    @Id
    private UUID id;

    private String originalFileName;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private AudioJobStatus status;

    private int processingTryCounter;

    private String ErrorMessage;

    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;

    private String codecName;

    private int sampleRate;

    private int channels;

    private double duration;

    private long bitRate;

    private long size;

    @Version
    private long version;

    public UUID getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public AudioJobStatus getStatus() {
        return status;
    }

    public int getProcessingTryCounter() {
        return processingTryCounter;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public String getCodecName() {
        return codecName;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getChannels() {
        return channels;
    }

    public double getDuration() {
        return duration;
    }

    public long getBitRate() {
        return bitRate;
    }

    public long getSize() {
        return size;
    }

    public long getVersion() {
        return version;
    }
}
