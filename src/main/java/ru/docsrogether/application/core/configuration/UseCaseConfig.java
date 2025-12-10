package ru.docsrogether.application.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.docsrogether.application.core.usecase.UserUseCase;
import ru.docsrogether.application.core.usecase.port.DocumentRepository;
import ru.docsrogether.application.core.usecase.ManageDocumentUseCase;
import ru.docsrogether.application.core.usecase.port.ManageUserUseCase;
import ru.docsrogether.application.core.usecase.port.PasswordEncoderPort;
import ru.docsrogether.application.core.usecase.port.UserRepository;

@Configuration
public class UseCaseConfig {
    @Bean
    public ManageDocumentUseCase manageDocumentUseCase(DocumentRepository documentRepository) {
        return new ManageDocumentUseCase(documentRepository);
    }

    @Bean
    public ManageUserUseCase manageUserUseCase(UserRepository userRepository, PasswordEncoderPort passwordEncoder) {
        return new UserUseCase(userRepository, passwordEncoder);
    }
}