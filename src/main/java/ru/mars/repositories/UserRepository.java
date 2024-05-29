package ru.mars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mars.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}