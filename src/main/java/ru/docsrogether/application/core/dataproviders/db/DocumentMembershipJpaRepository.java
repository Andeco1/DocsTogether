package ru.docsrogether.application.core.dataproviders.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.docsrogether.application.core.entity.DocumentRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentMembershipJpaRepository extends JpaRepository<DocumentMembershipJpaEntity, Long> {
    List<DocumentMembershipJpaEntity> findByDocumentId(String documentId);
    List<DocumentMembershipJpaEntity> findByUserId(Long userId);
    Optional<DocumentMembershipJpaEntity> findByDocumentIdAndUserId(String documentId, Long userId);
    void deleteByDocumentIdAndUserId(String documentId, Long userId);
    List<DocumentMembershipJpaEntity> findByDocumentIdAndRole(String documentId, DocumentRole role);
}

