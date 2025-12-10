package ru.docsrogether.application.core.entrypoints.rest.dto;

import lombok.Data;
import ru.docsrogether.application.core.entity.DocumentRole;

@Data
public class ShareLinkRequest {
    private DocumentRole role;
}

