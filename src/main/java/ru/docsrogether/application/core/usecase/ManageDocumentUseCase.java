package ru.docsrogether.application.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.docsrogether.application.core.entity.Document;
import ru.docsrogether.application.core.entity.DocumentMembership;
import ru.docsrogether.application.core.entity.DocumentRole;
import ru.docsrogether.application.core.usecase.port.DocumentMembershipRepository;
import ru.docsrogether.application.core.usecase.port.DocumentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ManageDocumentUseCase {

    private final DocumentRepository repository;
    private final DocumentMembershipRepository membershipRepository;

    public Document createDocument(String title, Long ownerId) {
        Document doc = Document.builder()
                .id(UUID.randomUUID().toString())
                .title(title)
                .content("")
                .lastModified(LocalDateTime.now())
                .ownerId(ownerId)
                .build();
        Document saved = repository.save(doc);
        membershipRepository.save(DocumentMembership.builder()
                .documentId(saved.getId())
                .userId(ownerId)
                .role(DocumentRole.OWNER)
                .build());
        return saved;
    }

    public Document updateDocumentContent(String id, String content) {
        Document doc = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        doc.updateContent(content);
        return repository.save(doc);
    }

    public List<Document> getUserDocuments(Long userId, String query) {
        return repository.findByOwnerAndQuery(userId, query);
    }

    public Document getAccessibleDocument(String id, Long userId) {
        Document doc = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        if (doc.getOwnerId().equals(userId)) {
            return doc;
        }
        boolean allowed = membershipRepository.findByDocumentIdAndUserId(id, userId).isPresent();
        if (!allowed) {
            throw new RuntimeException("Access denied");
        }
        return doc;
    }

    public boolean canEdit(String documentId, Long userId) {
        Document doc = repository.findById(documentId).orElse(null);
        if (doc == null) {
            return false;
        }
        if (doc.getOwnerId().equals(userId)) {
            return true;
        }
        return membershipRepository.findByDocumentIdAndUserId(documentId, userId)
                .map(m -> m.getRole() != DocumentRole.READER)
                .orElse(false);
    }

    public List<DocumentMembership> getDocumentMembers(String documentId) {
        return membershipRepository.findByDocumentId(documentId);
    }

    public void removeMembership(String documentId, Long userId) {
        membershipRepository.deleteByDocumentIdAndUserId(documentId, userId);
    }

    public List<DocumentMembership> getUserMemberships(Long userId) {
        return membershipRepository.findByUserId(userId);
    }

    public void deleteDocument(String documentId, Long requesterId) {
        Document doc = repository.findById(documentId).orElseThrow(() -> new RuntimeException("Document not found"));
        if (!doc.getOwnerId().equals(requesterId)) {
            throw new RuntimeException("Access denied");
        }
        List<DocumentMembership> members = membershipRepository.findByDocumentId(documentId);
        for (DocumentMembership membership : members) {
            membershipRepository.deleteById(membership.getId());
        }
        repository.deleteById(documentId);
    }

    public DocumentMembership addMembership(String documentId, Long userId, DocumentRole role) {
        DocumentMembership existing = membershipRepository.findByDocumentIdAndUserId(documentId, userId).orElse(null);
        if (existing != null) {
            existing.setRole(role);
            return membershipRepository.save(existing);
        }
        return membershipRepository.save(DocumentMembership.builder()
                .documentId(documentId)
                .userId(userId)
                .role(role)
                .build());
    }

    public List<Document> getAllDocuments() {
        return repository.findAll();
    }

    public List<Document> getOwnedDocuments(Long ownerId) {
        return repository.findByOwnerId(ownerId);
    }
}