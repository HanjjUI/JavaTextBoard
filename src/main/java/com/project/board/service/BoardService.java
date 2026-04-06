package com.project.board.service;

import com.project.board.dto.BoardDto;
import com.project.board.entity.Board;
import com.project.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 생성
    public BoardDto save(BoardDto dto) {

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목 필수");
        }
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("내용 필수");
        }
        if (dto.getAuthor() == null || dto.getAuthor().isBlank()) {
            throw new IllegalArgumentException("작성자 필수");
        }

        Board board = Board.create(
                dto.getTitle(),
                dto.getContent(),
                dto.getAuthor()
        );

        Board saved = boardRepository.save(board);
        return toDto(saved);
    }

    // 전체 조회
    public List<BoardDto> findAll() {
        return boardRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // 단일 조회
    public BoardDto findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        board.increaseViewCount();
        return toDto(board);
    }

    // 수정
    public BoardDto update(Long id, BoardDto dto) {

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        board.update(dto.getTitle(), dto.getContent());

        return toDto(board);
    }

    // 삭제
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    // 제목 검색
    public List<BoardDto> searchByTitle(String keyword) {
        return boardRepository.findByTitleContaining(keyword)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // 작성자 검색
    public List<BoardDto> searchByAuthor(String author) {
        return boardRepository.findByAuthor(author)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private BoardDto toDto(Board b) {
        return BoardDto.builder()
                .id(b.getId())
                .title(b.getTitle())
                .content(b.getContent())
                .author(b.getAuthor())
                .createdAt(b.getCreatedAt())
                .build();
    }
}