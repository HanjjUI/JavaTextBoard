package com.project.board.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int viewCount = 0;

    @Column(columnDefinition = "TEXT")
    private String content;
    private String title;
    private String author;
    private LocalDateTime createdAt;

    protected Board(){}

    public static Board create(String title, String content, String author){
        Board b = new Board();
        b.title = title;
        b.content = content;
        b.author = author;
        b.createdAt = LocalDateTime.now();
        return b;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void increaseViewCount(){
        this.viewCount++;
    }

    public Long getId(){ return id; }
    public String getTitle(){ return title; }
    public String getContent(){ return content; }
    public String getAuthor(){ return author; }
    public int getViewCount(){ return viewCount; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
}