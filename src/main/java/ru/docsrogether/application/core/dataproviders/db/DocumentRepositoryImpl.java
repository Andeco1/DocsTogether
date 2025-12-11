package ru.docsrogether.application.core.dataproviders.db;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.docsrogether.application.core.entity.Document;
import ru.docsrogether.application.core.usecase.port.DocumentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DocumentRepositoryImpl implements DocumentRepository {

    private final DocumentJpaRepository jpaRepository;
    private final DocumentMapper mapper;

    @Override
    public Document save(Document document) {
        DocumentJpaEntity jpaEntity = mapper.toJpaEntity(document);
        DocumentJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Document> findById(String id) {
          return jpaRepository.findById(id).map(mapper::toDomainEntity);
    }

    @Override
    public List<Document> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Document> findByOwnerId(Long ownerId) {
        return jpaRepository.findByOwnerId(ownerId).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Document> findByOwnerAndQuery(Long userId,@Nullable String query) {
        return jpaRepository.searchAccessible(userId, query != null ? query.toLowerCase() : "").stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}