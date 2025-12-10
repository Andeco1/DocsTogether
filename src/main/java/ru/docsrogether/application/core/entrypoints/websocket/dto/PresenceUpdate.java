package ru.docsrogether.application.core.entrypoints.websocket.dto;

import lombok.Data;
import ru.docsrogether.application.core.entity.DocumentRole;

@Data
public class PresenceUpdate {
    private Long userId;
    private String username;
    private DocumentRole role;
    private String type; // join | leave
}

