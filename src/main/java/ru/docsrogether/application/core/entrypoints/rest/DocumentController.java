package ru.docsrogether.application.core.entrypoints.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.docsrogether.application.core.entity.Document;
import ru.docsrogether.application.core.entity.DocumentMembership;
import ru.docsrogether.application.core.entity.DocumentRole;
import ru.docsrogether.application.core.entrypoints.rest.dto.CreateDocumentRequest;
import ru.docsrogether.application.core.entrypoints.rest.dto.DocumentMemberResponse;
import ru.docsrogether.application.core.entrypoints.rest.dto.DocumentResponse;
import ru.docsrogether.application.core.usecase.ManageDocumentUseCase;
import ru.docsrogether.application.core.usecase.port.ManageUserUseCase;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final ManageDocumentUseCase manageDocumentUseCase;
    private final ManageUserUseCase manageUserUseCase;

    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(@RequestBody CreateDocumentRequest request, Principal principal) {
        Long userId = manageUserUseCase.getByUsername(principal.getName()).getId();
        Document newDoc = manageDocumentUseCase.createDocument(request.getTitle(), userId);
        DocumentResponse response = DocumentResponse.fromDomain(newDoc);
        response.setRole(DocumentRole.OWNER);
        response.setEditable(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getAllDocuments(@RequestParam(value = "q", required = false) String query, Principal principal) {
        Long userId = manageUserUseCase.getByUsername(principal.getName()).getId();
        List<Document> docs = manageDocumentUseCase.getUserDocuments(userId, query);
        List<DocumentMembership> memberships = manageDocumentUseCase.getUserMemberships(userId);
        List<DocumentResponse> response = docs.stream()
                .map(doc -> {
                    DocumentResponse dto = DocumentResponse.fromDomain(doc);
                    dto.setRole(determineRole(doc, memberships, userId));
                    dto.setEditable(manageDocumentUseCase.canEdit(doc.getId(), userId));
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocumentById(@PathVariable String id, Principal principal) {
        try {
            Long userId = manageUserUseCase.getByUsername(principal.getName()).getId();
            Document doc = manageDocumentUseCase.getAccessibleDocument(id, userId);
            DocumentResponse response = DocumentResponse.fromDomain(doc);
            response.setEditable(manageDocumentUseCase.canEdit(id, userId));
            response.setRole(determineRole(doc, manageDocumentUseCase.getUserMemberships(userId), userId));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id, Principal principal) {
        Long userId = manageUserUseCase.getByUsername(principal.getName()).getId();
        manageDocumentUseCase.deleteDocument(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<DocumentMemberResponse>> members(@PathVariable String id, Principal principal) {
        Long userId = manageUserUseCase.getByUsername(principal.getName()).getId();
        manageDocumentUseCase.getAccessibleDocument(id, userId);
        List<DocumentMemberResponse> members = manageDocumentUseCase.getDocumentMembers(id).stream()
                .map(m -> new DocumentMemberResponse(
                        m.getUserId(),
                        manageUserUseCase.getById(m.getUserId()).getUsername(),
                        m.getRole()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(members);
    }

    private static ru.docsrogether.application.core.entity.DocumentRole determineRole(Document doc, List<DocumentMembership> memberships, Long userId) {
        if (doc.getOwnerId().equals(userId)) {
            return ru.docsrogether.application.core.entity.DocumentRole.OWNER;
        }
        return memberships.stream()
                .filter(m -> m.getDocumentId().equals(doc.getId()) && m.getUserId().equals(userId))
                .map(DocumentMembership::getRole)
                .findFirst()
                .orElse(ru.docsrogether.application.core.entity.DocumentRole.READER);
    }
}