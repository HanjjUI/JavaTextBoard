package com.project.board.service;

import com.project.board.entity.User;
import com.project.board.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public void signup(String username, String password) {
        if (repo.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("DUPLICATE_USERNAME");
        }

        User user = new User(username, encoder.encode(password));
        user.setCreatedAt(LocalDateTime.now());
        repo.save(user);
    }

    public User login(String username, String password) {
        return repo.findByUsername(username)
                .filter(user -> encoder.matches(password, user.getPassword()))
                .orElse(null);
    }
}
