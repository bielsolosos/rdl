package space.bielsolososdev.rdl.domain.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import space.bielsolososdev.rdl.domain.users.model.User;


public interface UserRepository extends JpaRepository<User, Long>,  JpaSpecificationExecutor<User>{

    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
}
