package space.bielsolososdev.rdl.domain.users.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.bielsolososdev.rdl.core.exception.BusinessException;
import space.bielsolososdev.rdl.domain.users.model.User;
import space.bielsolososdev.rdl.domain.users.repository.UserRepository;
import space.bielsolososdev.rdl.domain.users.repository.specification.UserSpecification;

/**
 * Altera informações de qualquer usuário sem validar senha antiga (Admin only)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public Page<User> listUsers(Pageable pageable, String filter, Boolean isActive, LocalDateTime createdAfter,
            LocalDateTime createdBefore) {
        UserSpecification spec = new UserSpecification(filter, isActive, createdAfter, createdBefore);

        return repository.findAll(spec, pageable);
    }

    public User toggleUserActive(Long id) {
        log.debug("Alternando status ativo do usuário com ID: {}", id);

        User user = userService.getEntity(id);
        user.setIsActive(!user.getIsActive());

        User savedUser = repository.save(user);
        log.info("Status do usuário {} alterado para: {}", user.getUsername(),
                user.getIsActive() ? "ATIVO" : "INATIVO");

        return savedUser;
    }

    public void deleteUser(Long id) {
        log.debug("Tentativa de deletar usuário com ID: {}", id);

        User user = userService.getEntity(id);
        repository.delete(user);

        log.warn("Usuário {} (ID: {}) foi DELETADO do sistema", user.getUsername(), id);
    }
    public User adminChangePassword(Long id, String newPassword) {
        log.debug("Admin alterando senha do usuário com ID: {}", id);

        User user = userService.getEntity(id);
        user.setPassword(passwordEncoder.encode(newPassword));

        User savedUser = repository.save(user);
        log.info("Senha do usuário {} alterada por administrador", user.getUsername());

        return savedUser;
    }

    public User adminEditUser(Long id, String username, String email) {
        User entity = userService.getEntity(id);

        if (!isSameValue(entity.getUsername(), username)) {
            repository.findByUsername(username).ifPresent(existingUser -> {
                throw new BusinessException("Nome de usuário já existente");
            });
        }

        if (!isSameValue(entity.getEmail(), email)) {
            repository.findByEmail(email).ifPresent(existingUser -> {
                throw new BusinessException("Email já existente");
            });
        }

        entity.setUsername(username);
        entity.setEmail(email);

        User savedUser = repository.save(entity);
        log.info("Admin atualizou usuário {}: username='{}', email='{}'", id, username, email);

        return savedUser;
    }

    private boolean isSameValue(String newValue, String currentValue) {
        if (newValue.equals(currentValue))
            return true;

        return false;
    }
}
