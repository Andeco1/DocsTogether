package ru.docsrogether.application.core.entrypoints.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.docsrogether.application.core.entity.DocumentRole;

@Data
@AllArgsConstructor
public class DocumentMemberResponse {
    private Long userId;
    private String username;
    private DocumentRole role;
}

