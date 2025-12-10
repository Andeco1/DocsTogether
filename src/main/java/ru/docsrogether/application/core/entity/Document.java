package ru.docsrogether.application.core.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Document {
    private String id;
    private String title;
    private String content;
    private LocalDateTime lastModified;

    public void updateContent(String newContent) {
        if (newContent == null) return;
        this.content = newContent;
        this.lastModified = LocalDateTime.now();
    }
}
