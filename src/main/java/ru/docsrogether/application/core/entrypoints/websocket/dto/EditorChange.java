package ru.docsrogether.application.core.entrypoints.websocket.dto;

import lombok.Data;

@Data
public class EditorChange {
    private String newContent;
}
