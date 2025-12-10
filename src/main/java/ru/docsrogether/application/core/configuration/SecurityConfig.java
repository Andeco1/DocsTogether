package ru.docsrogether.application.core.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.docsrogether.application.core.usecase.port.PasswordEncoderPort;
import ru.docsrogether.application.core.usecase.port.UserRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Для упрощения работы с fetch-запросами
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register", "/login", "/auth/register", "/css/**", "/js/**").permitAll() // Разрешаем вход и статику
                        .anyRequest().authenticated() // Все остальное только для вошедших
                )
                .formLogin(form -> form
                        .loginPage("/login") // Указываем URL нашей кастомной страницы
                        .loginProcessingUrl("/perform_login") // Куда отправлять форму (POST)
                        .defaultSuccessUrl("/profile", true) // Куда перенаправить после успеха
                        .failureUrl("/login?error=true") // Куда если ошибка
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Адаптер для Spring Security
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .map(u -> org.springframework.security.core.userdetails.User.builder()
                        .username(u.getUsername())
                        .password(u.getPassword())
                        .roles("USER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // Адаптер для UseCase
    @Bean
    public PasswordEncoderPort passwordEncoderPort(PasswordEncoder encoder) {
        return new PasswordEncoderPort() {
            @Override
            public String encode(String raw) {
                return encoder.encode(raw);
            }
            @Override
            public boolean matches(String raw, String encoded) {
                return encoder.matches(raw, encoded);
            }
        };
    }
}