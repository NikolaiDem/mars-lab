package ru.mars.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mars.entities.User;
import ru.mars.exceptions.DuplicateEntityException;
import ru.mars.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        String username = user.getUsername();
        if (repository.existsByUsername(username)) {
            throw new DuplicateEntityException(STR."User with name \{username} already exists");
        }

        return save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(STR."User \{username} is not found"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
}
