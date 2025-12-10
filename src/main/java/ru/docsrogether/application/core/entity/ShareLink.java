package ru.docsrogether.application.core.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ShareLink {
    private Long id;
    private String token;
    private String documentId;
    private DocumentRole role;
    private LocalDateTime createdAt;
}

