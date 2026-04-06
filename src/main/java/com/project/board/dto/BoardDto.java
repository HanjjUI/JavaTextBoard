package com.project.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * BoardDto
 * - 데이터 전달 객체
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private String author;
}