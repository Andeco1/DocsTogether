package ru.docsrogether.application.core.dataproviders.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.docsrogether.application.core.entity.DocumentMembership;
import ru.docsrogether.application.core.entity.DocumentRole;
import ru.docsrogether.application.core.usecase.port.DocumentMembershipRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DocumentMembershipRepositoryImpl implements DocumentMembershipRepository {
    private final DocumentMembershipJpaRepository jpaRepository;

    @Override
    public DocumentMembership save(DocumentMembership membership) {
        DocumentMembershipJpaEntity entity = toJpa(membership);
        DocumentMembershipJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<DocumentMembership> findByDocumentId(String documentId) {
        return jpaRepository.findByDocumentId(documentId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<DocumentMembership> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<DocumentMembership> findByDocumentIdAndUserId(String documentId, Long userId) {
        return jpaRepository.findByDocumentIdAndUserId(documentId, userId).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteByDocumentIdAndUserId(String documentId, Long userId) {
        jpaRepository.deleteByDocumentIdAndUserId(documentId, userId);
    }

    private DocumentMembership toDomain(DocumentMembershipJpaEntity entity) {
        return DocumentMembership.builder()
                .id(entity.getId())
                .documentId(entity.getDocumentId())
                .userId(entity.getUserId())
                .role(entity.getRole())
                .build();
    }

    private DocumentMembershipJpaEntity toJpa(DocumentMembership membership) {
        DocumentMembershipJpaEntity entity = new DocumentMembershipJpaEntity();
        entity.setId(membership.getId());
        entity.setDocumentId(membership.getDocumentId());
        entity.setUserId(membership.getUserId());
        entity.setRole(membership.getRole());
        return entity;
    }
}

