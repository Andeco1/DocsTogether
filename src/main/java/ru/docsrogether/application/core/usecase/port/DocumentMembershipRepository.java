package ru.docsrogether.application.core.usecase.port;

import ru.docsrogether.application.core.entity.DocumentMembership;
import ru.docsrogether.application.core.entity.DocumentRole;

import java.util.List;
import java.util.Optional;

public interface DocumentMembershipRepository {
    DocumentMembership save(DocumentMembership membership);
    List<DocumentMembership> findByDocumentId(String documentId);
    List<DocumentMembership> findByUserId(Long userId);
    Optional<DocumentMembership> findByDocumentIdAndUserId(String documentId, Long userId);
    void deleteById(Long id);
    void deleteByDocumentIdAndUserId(String documentId, Long userId);
}

