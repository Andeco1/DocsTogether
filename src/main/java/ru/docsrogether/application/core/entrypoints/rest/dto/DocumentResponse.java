package ru.docsrogether.application.core.entrypoints.rest.dto;


import lombok.Data;
import ru.docsrogether.application.core.entity.Document;
import ru.docsrogether.application.core.entity.DocumentRole;

import java.time.LocalDateTime;

@Data
public class DocumentResponse {
    private String id;
    private String title;
    private String content;
    private LocalDateTime lastModified;
    private Long ownerId;
    private DocumentRole role;
    private boolean editable;

    public static DocumentResponse fromDomain(Document document) {
        DocumentResponse dto = new DocumentResponse();
        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setContent(document.getContent());
        dto.setLastModified(document.getLastModified());
        dto.setOwnerId(document.getOwnerId());
        return dto;
    }
}