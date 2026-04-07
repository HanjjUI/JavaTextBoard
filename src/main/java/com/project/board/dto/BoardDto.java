package com.project.board.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private int viewCount;
}