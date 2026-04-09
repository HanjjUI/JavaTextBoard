package com.project.board.dto;

import com.project.board.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

// 掲示板DTO（画面に渡すデータ用）
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private int viewCount;

    // Entity → DTO変換メソッド
    public static BoardDto from(Board b){
        return BoardDto.builder()
                .id(b.getId())
                .title(b.getTitle())
                .content(b.getContent())
                .author(b.getAuthor())
                .createdAt(b.getCreatedAt())
                .viewCount(b.getViewCount())
                .build();
    }
}