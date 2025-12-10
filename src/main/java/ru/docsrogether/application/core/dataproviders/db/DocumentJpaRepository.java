package ru.docsrogether.application.core.dataproviders.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentJpaRepository extends JpaRepository<DocumentJpaEntity, String> {

}