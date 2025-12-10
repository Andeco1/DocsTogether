package ru.docsrogether.application.core.dataproviders.db;

import org.springframework.stereotype.Component;
import ru.docsrogether.application.core.entity.Document;

@Component
public class DocumentMapper {

    public DocumentJpaEntity toJpaEntity(Document domainEntity) {
        DocumentJpaEntity jpaEntity = new DocumentJpaEntity();
        jpaEntity.setId(domainEntity.getId());
        jpaEntity.setTitle(domainEntity.getTitle());
        jpaEntity.setContent(domainEntity.getContent());
        jpaEntity.setLastModified(domainEntity.getLastModified());
        jpaEntity.setOwnerId(domainEntity.getOwnerId());
        return jpaEntity;
    }

    public Document toDomainEntity(DocumentJpaEntity jpaEntity) {
        return Document.builder()
                .id(jpaEntity.getId())
                .title(jpaEntity.getTitle())
                .content(jpaEntity.getContent())
                .lastModified(jpaEntity.getLastModified())
                .ownerId(jpaEntity.getOwnerId())
                .build();
    }
}