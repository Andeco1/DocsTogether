package ru.docsrogether.application.core.dataproviders.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShareLinkJpaRepository extends JpaRepository<ShareLinkJpaEntity, Long> {
    Optional<ShareLinkJpaEntity> findByToken(String token);
    void deleteByDocumentId(String documentId);
}

