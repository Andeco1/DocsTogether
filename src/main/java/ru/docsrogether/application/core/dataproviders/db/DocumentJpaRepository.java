package ru.docsrogether.application.core.dataproviders.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentJpaRepository extends JpaRepository<DocumentJpaEntity, String> {
    List<DocumentJpaEntity> findByOwnerId(Long ownerId);

    @Query("select d from DocumentJpaEntity d where (d.ownerId = :userId or d.id in (select m.documentId from DocumentMembershipJpaEntity m where m.userId = :userId)) and (:query is null or lower(d.title) like lower(concat('%', :query, '%')))")
    List<DocumentJpaEntity> searchAccessible(@Param("userId") Long userId, @Param("query") String query);
}