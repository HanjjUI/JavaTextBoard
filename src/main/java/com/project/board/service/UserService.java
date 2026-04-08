package com.project.board.service;

import com.project.board.entity.User;
import com.project.board.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// ユーザーサービス（ビジネスロジック担当）
@Service
public class UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo){
        this.repo = repo;
    }

    // 会員登録
    public void signup(String u, String p){

        // 重複チェック
        if(repo.findByUsername(u).isPresent()){
            throw new RuntimeException("既に存在するユーザーです");
        }

        // パスワードは暗号化して保存
        User user = new User(u, encoder.encode(p));
        repo.save(user);
    }

    // ログイン処理
    public User login(String u, String p){

        return repo.findByUsername(u)
                // パスワード一致チェック
                .filter(user -> encoder.matches(p, user.getPassword()))
                .orElse(null);
    }
}