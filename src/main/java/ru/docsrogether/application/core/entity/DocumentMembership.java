package ru.docsrogether.application.core.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentMembership {
    private Long id;
    private String documentId;
    private Long userId;
    private DocumentRole role;
}

