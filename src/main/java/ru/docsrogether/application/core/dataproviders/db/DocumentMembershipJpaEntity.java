package ru.docsrogether.application.core.dataproviders.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.docsrogether.application.core.entity.DocumentRole;

@Entity
@Table(name = "document_memberships")
@Getter
@Setter
@NoArgsConstructor
public class DocumentMembershipJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String documentId;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentRole role;
}

