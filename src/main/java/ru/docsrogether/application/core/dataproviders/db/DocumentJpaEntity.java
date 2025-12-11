package ru.docsrogether.application.core.dataproviders.db;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
public class DocumentJpaEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    private String content;

    private LocalDateTime lastModified;

    @Column(nullable = false)
    private Long ownerId;
}
