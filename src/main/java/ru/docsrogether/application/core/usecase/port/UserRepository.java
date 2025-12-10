package ru.docsrogether.application.core.usecase.port;

import ru.docsrogether.application.core.entity.User;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    void deleteById(Long id);
    boolean existsByUsername(String username);
}