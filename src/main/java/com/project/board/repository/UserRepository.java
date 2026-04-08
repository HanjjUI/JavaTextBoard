package com.project.board.repository;

import com.project.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// ユーザーRepository（DBアクセス担当）
public interface UserRepository extends JpaRepository<User, Long> {

    // ユーザー名で検索（存在しない場合はOptionalで返す）
    Optional<User> findByUsername(String username);
}