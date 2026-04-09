package com.project.board.entity;

import jakarta.persistence.*;

// ユーザーエンティティ
@Entity
@Table(name="users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ユーザー名（重複不可）
    @Column(unique = true, nullable = false)
    private String username;

    // パスワード（暗号化して保存）
    @Column(nullable = false)
    private String password;

    protected User(){}

    // コンストラクタで初期化
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
}