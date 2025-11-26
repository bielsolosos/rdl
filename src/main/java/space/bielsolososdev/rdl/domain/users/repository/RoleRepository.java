package space.bielsolososdev.rdl.domain.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import space.bielsolososdev.rdl.domain.users.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
