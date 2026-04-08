package com.project.board.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// 掲示板エンティティ
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 조회수 (初期値0)
    private int viewCount = 0;

    // 내용 (長文対応)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // 제목
    @Column(nullable = false)
    private String title;

    // 작성자
    @Column(nullable = false)
    private String author;

    // 생성일
    private LocalDateTime createdAt;

    // JPA 기본 생성자 (外部から生成不可)
    protected Board(){}

    // 생성 메서드 (オブジェクト生成管理)
    public static Board create(String title, String content, String author){
        Board b = new Board();
        b.title = title;
        b.content = content;
        b.author = author;
        return b;
    }

    // DB 저장 직전에 실행 (生成日時自動設定)
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 게시글 수정
    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    // 조회수 증가
    public void increaseViewCount(){
        this.viewCount++;
    }

    // getter
    public Long getId(){ return id; }
    public String getTitle(){ return title; }
    public String getContent(){ return content; }
    public String getAuthor(){ return author; }
    public int getViewCount(){ return viewCount; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
}