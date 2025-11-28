package space.bielsolososdev.rdl.domain.users.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.bielsolososdev.rdl.core.exception.BusinessException;
import space.bielsolososdev.rdl.domain.users.model.User;
import space.bielsolososdev.rdl.domain.users.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User getMe() {
        log.debug("Buscando informações do usuário autenticado");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Tentativa de acesso sem autenticação válida");
            throw new BusinessException("Usuário não autenticado");
        }

        String username = authentication.getName();
        log.info("Usuário '{}' acessando seu próprio perfil", username);
        
        return repository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Usuário autenticado '{}' não encontrado no banco de dados", username);
                    return new BusinessException("Usuário não encontrado");
                });
    }

    public User changePassword(Long id, String oldPassword, String newPassword) {
        log.debug("Tentativa de alteração de senha para o usuário com ID: {}", id);
        
        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuário com ID {} não encontrado ao tentar alterar senha", id);
                    return new BusinessException("User não encontrado");
                });

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.warn("Tentativa de alteração de senha com senha atual incorreta para usuário: {}", user.getUsername());
            throw new BusinessException("Senha atual incorreta");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        User savedUser = repository.save(user);
        
        log.info("Senha alterada com sucesso para o usuário: {}", user.getUsername());
        return savedUser;
    }

    /**
     * TODO: Fazer envio e confirmação de email, setar role padrão e outras features mais. 
     * @param username
     * @param email
     * @param password
     * @return
     */
    public User createUser(String username, String email, String password){
        log.debug("Tentativa de criação de novo usuário: username='{}', email='{}'", username, email);
        
        if (repository.findByUsername(username).isPresent()) {
            log.warn("Tentativa de criar usuário com username já existente: {}", username);
            throw new BusinessException("Nome de usuário já está em uso");
        }
        
        if (repository.findByEmail(email).isPresent()) {
            log.warn("Tentativa de criar usuário com email já existente: {}", email);
            throw new BusinessException("E-mail já está em uso");
        }
        
        User entity = new User();  
        entity.setEmail(email);
        entity.setUsername(username); 
        entity.setPassword(passwordEncoder.encode(password));

        User savedUser = repository.save(entity);
        log.info("✅ Novo usuário criado com sucesso: {} (ID: {})", username, savedUser.getId());
        
        return savedUser;
    }

    public User editUser(Long id, String username, String email) {
        repository.findByUsername(username).orElseThrow(() -> new BusinessException("Nome de usuário já existente"));

        repository.findByEmail(email).orElseThrow(() -> new BusinessException("Email do usuário já existente"));

        User entity = getEntity(id);

        verifyUserIntegrity(entity);

        entity.setUsername(username);
        entity.setEmail(email);
        return repository.save(entity);
    }


    private User getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado no sistema."));
    }

    /**
     * Método auxiliar para verificar se o usuário está tentando editar ele mesmo ou outra pessoa.
     * @param entity
     */
    private void verifyUserIntegrity(User entity) {
        if (entity.getId() != getMe().getId())
            throw new BadCredentialsException("Sem permissão.");
    }
}
