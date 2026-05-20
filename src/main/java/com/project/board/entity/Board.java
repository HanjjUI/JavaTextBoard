package com.project.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
public class Board {

    private static final ZoneId TOKYO_ZONE = ZoneId.of("Asia/Tokyo");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(length = 500)
    private String imageUrl;

    protected Board() {
    }

    public static Board create(String title, String content, String author, String imageUrl) {
        Board board = new Board();
        board.title = title;
        board.content = content;
        board.author = author;
        board.imageUrl = imageUrl;
        return board;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now(TOKYO_ZONE);
    }

    public void update(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public int getViewCount() {
        return viewCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
