package space.bielsolososdev.rdl.domain.users.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import space.bielsolososdev.rdl.domain.users.dto.UserRegistrationDto;
import space.bielsolososdev.rdl.domain.users.model.Role;
import space.bielsolososdev.rdl.domain.users.model.User;
import space.bielsolososdev.rdl.domain.users.repository.RoleRepository;
import space.bielsolososdev.rdl.domain.users.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public User register(UserRegistrationDto dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("As senhas não conferem");
        }
        
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username já está em uso");
        }
        
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já está em uso");
        }
        
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setIsActive(true);
        
        // Adicionar role padrão ROLE_USER
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Role ROLE_USER não encontrada"));
        user.getRoles().add(userRole);
        
        return userRepository.save(user);
    }
}
