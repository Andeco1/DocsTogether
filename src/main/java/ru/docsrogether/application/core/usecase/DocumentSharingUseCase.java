package ru.docsrogether.application.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.docsrogether.application.core.entity.Document;
import ru.docsrogether.application.core.entity.DocumentMembership;
import ru.docsrogether.application.core.entity.DocumentRole;
import ru.docsrogether.application.core.entity.ShareLink;
import ru.docsrogether.application.core.usecase.port.DocumentMembershipRepository;
import ru.docsrogether.application.core.usecase.port.DocumentRepository;
import ru.docsrogether.application.core.usecase.port.ShareLinkRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class DocumentSharingUseCase {
    private final DocumentRepository documentRepository;
    private final DocumentMembershipRepository membershipRepository;
    private final ShareLinkRepository shareLinkRepository;

    public ShareLink createShareLink(String documentId, Long ownerId, DocumentRole role) {
        Document doc = documentRepository.findById(documentId).orElseThrow(() -> new RuntimeException("Document not found"));
        if (!doc.getOwnerId().equals(ownerId)) {
            throw new RuntimeException("Access denied");
        }
        ShareLink link = ShareLink.builder()
                .token(UUID.randomUUID().toString())
                .documentId(documentId)
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();
        return shareLinkRepository.save(link);
    }

    public DocumentMembership consumeToken(String token, Long userId) {
        ShareLink shareLink = shareLinkRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Link not found"));
        Document doc = documentRepository.findById(shareLink.getDocumentId()).orElseThrow(() -> new RuntimeException("Document not found"));
        if (doc.getOwnerId().equals(userId)) {
            return membershipRepository.findByDocumentIdAndUserId(doc.getId(), userId).orElseGet(() ->
                    membershipRepository.save(DocumentMembership.builder()
                            .documentId(doc.getId())
                            .userId(userId)
                            .role(DocumentRole.OWNER)
                            .build()));
        }
        DocumentMembership existing = membershipRepository.findByDocumentIdAndUserId(doc.getId(), userId).orElse(null);
        if (existing != null) {
            if (existing.getRole() == DocumentRole.READER && shareLink.getRole() == DocumentRole.WRITER) {
                existing.setRole(DocumentRole.WRITER);
                return membershipRepository.save(existing);
            }
            return existing;
        }
        return membershipRepository.save(DocumentMembership.builder()
                .documentId(doc.getId())
                .userId(userId)
                .role(shareLink.getRole())
                .build());
    }

    public ShareLink getPreview(String token) {
        return shareLinkRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Link not found"));
    }

    public Document getDocumentByToken(String token) {
        ShareLink link = getPreview(token);
        return documentRepository.findById(link.getDocumentId()).orElseThrow(() -> new RuntimeException("Document not found"));
    }
}

