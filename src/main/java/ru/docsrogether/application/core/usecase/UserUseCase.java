package ru.docsrogether.application.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.docsrogether.application.core.entity.User;
import ru.docsrogether.application.core.usecase.port.ManageUserUseCase;
import ru.docsrogether.application.core.usecase.port.PasswordEncoderPort;
import ru.docsrogether.application.core.usecase.port.UserRepository;

@RequiredArgsConstructor
public class UserUseCase implements ManageUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    @Override
    public User register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("User already exists");
        }
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        return userRepository.save(user);
    }

    @Override
    public User update(String currentUsername, String oldPassword, String newPassword, String newUsername) {
        User user = getByUsername(currentUsername);

        // Проверяем старый пароль (если требуется подтверждение)
        if (oldPassword != null && !passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }

        if (newUsername != null && !newUsername.isBlank()) {
            if (!currentUsername.equals(newUsername) && userRepository.existsByUsername(newUsername)) {
                throw new RuntimeException("Username already taken");
            }
            user.setUsername(newUsername);
        }

        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        return userRepository.save(user);
    }

    @Override
    public void delete(String username) {
        User user = getByUsername(username);
        userRepository.deleteById(user.getId());
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
