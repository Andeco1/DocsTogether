package ru.docsrogether.application.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.docsrogether.application.core.entity.Document;
import ru.docsrogether.application.core.usecase.port.DocumentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ManageDocumentUseCase {

    private final DocumentRepository repository;

    public Document createDocument(String title) {
        Document doc = Document.builder()
                .id(UUID.randomUUID().toString())
                .title(title)
                .content("")
                .lastModified(LocalDateTime.now())
                .build();
        return repository.save(doc);
    }

    public Document updateDocumentContent(String id, String content) {
        Document doc = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        doc.updateContent(content);
        return repository.save(doc);
    }

    public List<Document> getAllDocuments() {
        return repository.findAll();
    }

    public Document getDocument(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }
}