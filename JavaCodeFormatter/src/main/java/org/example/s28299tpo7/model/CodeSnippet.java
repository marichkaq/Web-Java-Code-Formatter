package org.example.s28299tpo7.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class CodeSnippet implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String formattedCode;
    private LocalDateTime creationDate;
    private Duration duration;

    public CodeSnippet() {
        this.id = UUID.randomUUID().toString();
        this.creationDate = LocalDateTime.now();
    }

    public CodeSnippet(String formattedCode,long durationInSec) {
        this();
        this.formattedCode = formattedCode;
        this.duration = Duration.ofSeconds(Math.max(10, Math.min(durationInSec, Duration.ofDays(90).toSeconds())));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormattedCode() {
        return formattedCode;
    }

    public void setFormattedCode(String formattedCode) {
        this.formattedCode = formattedCode;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public boolean isExpired() {
        return creationDate.plus(duration).isBefore(LocalDateTime.now());
    }

}
