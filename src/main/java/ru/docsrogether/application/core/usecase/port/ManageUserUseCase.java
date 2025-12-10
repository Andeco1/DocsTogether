package ru.docsrogether.application.core.usecase.port;

import ru.docsrogether.application.core.entity.User;

public interface ManageUserUseCase {
    User register(String username, String password);
    User update(String username, String oldPassword, String newPassword, String newUsername);
    void delete(String username);
    User getByUsername(String username);
    User getById(Long id);
}