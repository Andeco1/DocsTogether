package ru.docsrogether.application.core.entrypoints.rest;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.docsrogether.application.core.entity.User;
import ru.docsrogether.application.core.usecase.port.ManageUserUseCase;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final ManageUserUseCase manageUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody AuthRequest request) {
        User user = manageUserUseCase.register(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(toResponse(user));
    }

    @GetMapping()
    public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
        User user = manageUserUseCase.getByUsername(principal.getName());
        return ResponseEntity.ok(toResponse(user));
    }

    @PutMapping()
    public ResponseEntity<UserResponse> updateCurrentUser(
            Principal principal,
            @RequestBody UpdateUserRequest request) {
        User user = manageUserUseCase.update(
                principal.getName(),
                request.getOldPassword(),
                request.getNewPassword(),
                request.getNewUsername()
        );
        return ResponseEntity.ok(toResponse(user));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteCurrentUser(Principal principal) {
        manageUserUseCase.delete(principal.getName());
        return ResponseEntity.noContent().build();
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername());
    }

    @Data
    public static class AuthRequest {
        private String username;
        private String password;
    }

    @Data
    public static class UpdateUserRequest {
        private String oldPassword;
        private String newPassword;
        private String newUsername;
    }

    @Data
    @RequiredArgsConstructor
    public static class UserResponse {
        private final Long id;
        private final String username;
    }
}