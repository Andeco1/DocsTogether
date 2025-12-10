package ru.docsrogether.application.core.usecase.port;

import ru.docsrogether.application.core.entity.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {
    Document save(Document document);
    Optional<Document> findById(String id);
    List<Document> findAll();
    List<Document> findByOwnerId(Long ownerId);
    List<Document> searchByOwnerOrMember(Long userId, String query);
    void deleteById(String id);
}