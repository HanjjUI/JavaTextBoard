package com.project.board.service;

import com.project.board.entity.User;
import com.project.board.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo){
        this.repo = repo;
    }

    public void signup(String u, String p){
        repo.save(new User(u, encoder.encode(p)));
    }

    public User login(String u, String p){
        return repo.findByUsername(u)
                .filter(x -> encoder.matches(p, x.getPassword()))
                .orElse(null);
    }
}