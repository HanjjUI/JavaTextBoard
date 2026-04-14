package com.project.board.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// 掲示板の投稿を表すエンティティです
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 閲覧数です
    // 最初は0から始まります
    private int viewCount = 0;

    // 投稿内容です
    // 長い文章も保存できるようにTEXT型にしています
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // 投稿タイトルです
    @Column(nullable = false)
    private String title;

    // 投稿者の名前です
    @Column(nullable = false)
    private String author;

    // 投稿が作成された日時です
    private LocalDateTime createdAt;

    // JPAで必要な基本コンストラクタです
    // 外部から勝手に生成されないようにprotectedにしています
    protected Board(){}

    // Boardオブジェクトを作成するためのメソッドです
    // new の代わりにこちらを使うようにしています
    public static Board create(String title, String content, String author){
        Board b = new Board();
        b.title = title;
        b.content = content;
        b.author = author;
        return b;
    }

    // DBに保存する直前に実行されます
    // 作成日時を自動で入れるための処理です
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 投稿のタイトルと内容を修正するときに使います
    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    // 閲覧数を1増やすメソッドです
    public void increaseViewCount(){
        this.viewCount++;
    }

    // getterです
    public Long getId(){ return id; }
    public String getTitle(){ return title; }
    public String getContent(){ return content; }
    public String getAuthor(){ return author; }
    public int getViewCount(){ return viewCount; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
}
