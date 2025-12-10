package ru.docsrogether.application.core.usecase.port;

import ru.docsrogether.application.core.entity.ShareLink;

import java.util.Optional;

public interface ShareLinkRepository {
    ShareLink save(ShareLink shareLink);
    Optional<ShareLink> findByToken(String token);
    void deleteById(Long id);
    void deleteByDocumentId(String documentId);
}

