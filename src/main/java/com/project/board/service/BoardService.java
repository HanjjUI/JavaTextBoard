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

    public BoardDto save(BoardDto dto) {
        Board board = Board.create(dto.getTitle(), dto.getContent(), dto.getAuthor());
        Board saved = boardRepository.save(board);
        return toDto(saved);
    }

    public List<BoardDto> findAll() {
        return boardRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public BoardDto findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 없습니다."));
        board.increaseViewCount();
        return toDto(board);
    }

    public void delete(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new RuntimeException("삭제할 게시글이 없습니다.");
        }
        boardRepository.deleteById(id);
    }

    private BoardDto toDto(Board board) {
        BoardDto dto = new BoardDto();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setAuthor(board.getAuthor());
        return dto;
    }
}