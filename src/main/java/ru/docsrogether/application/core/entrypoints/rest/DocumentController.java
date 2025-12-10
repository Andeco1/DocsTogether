package ru.docsrogether.application.core.entrypoints.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.docsrogether.application.core.entity.Document;
import ru.docsrogether.application.core.entrypoints.rest.dto.CreateDocumentRequest;
import ru.docsrogether.application.core.entrypoints.rest.dto.DocumentResponse;
import ru.docsrogether.application.core.usecase.ManageDocumentUseCase;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final ManageDocumentUseCase manageDocumentUseCase;

    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(@RequestBody CreateDocumentRequest request) {
        Document newDoc = manageDocumentUseCase.createDocument(request.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(DocumentResponse.fromDomain(newDoc));
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getAllDocuments() {
        List<Document> docs = manageDocumentUseCase.getAllDocuments();
        List<DocumentResponse> response = docs.stream()
                .map(DocumentResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocumentById(@PathVariable String id) {
        try {
            Document doc = manageDocumentUseCase.getDocument(id);
            return ResponseEntity.ok(DocumentResponse.fromDomain(doc));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}