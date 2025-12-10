package ru.docsrogether.application.core.dataproviders.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.docsrogether.application.core.entity.ShareLink;
import ru.docsrogether.application.core.usecase.port.ShareLinkRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ShareLinkRepositoryImpl implements ShareLinkRepository {
    private final ShareLinkJpaRepository jpaRepository;

    @Override
    public ShareLink save(ShareLink shareLink) {
        ShareLinkJpaEntity entity = toJpa(shareLink);
        ShareLinkJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<ShareLink> findByToken(String token) {
        return jpaRepository.findByToken(token).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteByDocumentId(String documentId) {
        jpaRepository.deleteByDocumentId(documentId);
    }

    private ShareLink toDomain(ShareLinkJpaEntity entity) {
        return ShareLink.builder()
                .id(entity.getId())
                .token(entity.getToken())
                .documentId(entity.getDocumentId())
                .role(entity.getRole())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private ShareLinkJpaEntity toJpa(ShareLink shareLink) {
        ShareLinkJpaEntity entity = new ShareLinkJpaEntity();
        entity.setId(shareLink.getId());
        entity.setToken(shareLink.getToken());
        entity.setDocumentId(shareLink.getDocumentId());
        entity.setRole(shareLink.getRole());
        entity.setCreatedAt(shareLink.getCreatedAt());
        return entity;
    }
}

