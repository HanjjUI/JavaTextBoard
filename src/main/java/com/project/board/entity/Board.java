package com.project.board.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    protected Board() {}

    private Board(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public static Board create(String title, String content, String author) {
        return new Board(title, content, author);
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public int getViewCount() { return viewCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}