package rs.ac.bg.fon.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AudioMetadata(String codecName,
                            int sampleRate,
                            int channels,
                            double duration,
                            long bitRate,
                            long size){}
