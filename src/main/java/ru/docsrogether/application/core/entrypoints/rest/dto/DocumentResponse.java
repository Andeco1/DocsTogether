package ru.docsrogether.application.core.entrypoints.rest.dto;


import lombok.Data;
import ru.docsrogether.application.core.entity.Document;

import java.time.LocalDateTime;

@Data
public class DocumentResponse {
    private String id;
    private String title;
    private String content;
    private LocalDateTime lastModified;

    public static DocumentResponse fromDomain(Document document) {
        DocumentResponse dto = new DocumentResponse();
        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setContent(document.getContent());
        dto.setLastModified(document.getLastModified());
        return dto;
    }
}