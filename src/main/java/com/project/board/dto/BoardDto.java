package com.project.board.dto;

import com.project.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private Long id;
    private Long postNumber;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private int viewCount;

    public static BoardDto from(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .postNumber(board.getPostNumber())
                .title(board.getTitle())
                .content(board.getContent())
                .author(board.getAuthor())
                .createdAt(board.getCreatedAt())
                .viewCount(board.getViewCount())
                .build();
    }
}
