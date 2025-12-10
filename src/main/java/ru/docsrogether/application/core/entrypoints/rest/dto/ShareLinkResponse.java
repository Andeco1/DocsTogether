package ru.docsrogether.application.core.entrypoints.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.docsrogether.application.core.entity.DocumentRole;

@Data
@AllArgsConstructor
public class ShareLinkResponse {
    private String token;
    private DocumentRole role;
    private String documentId;
    private String documentTitle;
}

