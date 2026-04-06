package com.project.board.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Board Entity
 *
 * 역할:
 * - 게시글 데이터 저장
 *
 * 특징:
 * - 생성 메서드(create)로 생성 통제
 * - 조회수 증가, 수정 기능 포함
 */
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = false)
    private String author;

    @Column(nullable = false)
    private int viewCount = 0;

    // 기본 생성자 (JPA 필수)
    protected Board() {}

    // 생성자 (외부 사용 금지)
    private Board(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    // 🔥 생성 메서드 (실무 핵심)
    public static Board create(String title, String content, String author) {
        return new Board(title, content, author);
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 비즈니스 로직
    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Getter만 제공 (불변성 유지)
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public int getViewCount() { return viewCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}