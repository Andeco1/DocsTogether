package ru.docsrogether.application.core.entrypoints.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.docsrogether.application.core.entity.DocumentMembership;
import ru.docsrogether.application.core.entity.ShareLink;
import ru.docsrogether.application.core.entrypoints.rest.dto.ShareLinkRequest;
import ru.docsrogether.application.core.entrypoints.rest.dto.ShareLinkResponse;
import ru.docsrogether.application.core.usecase.DocumentSharingUseCase;
import ru.docsrogether.application.core.usecase.ManageDocumentUseCase;
import ru.docsrogether.application.core.usecase.port.ManageUserUseCase;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ShareController {
    private final DocumentSharingUseCase documentSharingUseCase;
    private final ManageDocumentUseCase manageDocumentUseCase;
    private final ManageUserUseCase manageUserUseCase;

    @PostMapping("/api/documents/{id}/share")
    public ResponseEntity<ShareLinkResponse> createShare(@PathVariable("id") String documentId, @RequestBody ShareLinkRequest request, Principal principal) {
        Long ownerId = manageUserUseCase.getByUsername(principal.getName()).getId();
        ShareLink link = documentSharingUseCase.createShareLink(documentId, ownerId, request.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ShareLinkResponse(link.getToken(), link.getRole(), link.getDocumentId(), manageDocumentUseCase.getAccessibleDocument(documentId, ownerId).getTitle()));
    }

    @PostMapping("/api/share/{token}")
    public ResponseEntity<Void> consume(@PathVariable String token, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = manageUserUseCase.getByUsername(principal.getName()).getId();
        DocumentMembership membership = documentSharingUseCase.consumeToken(token, userId);
        manageDocumentUseCase.getAccessibleDocument(membership.getDocumentId(), userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/share/{token}")
    public ResponseEntity<ShareLinkResponse> preview(@PathVariable String token) {
        ShareLink link = documentSharingUseCase.getPreview(token);
        var doc = documentSharingUseCase.getDocumentByToken(token);
        return ResponseEntity.ok(new ShareLinkResponse(link.getToken(), link.getRole(), link.getDocumentId(), doc.getTitle()));
    }
}

