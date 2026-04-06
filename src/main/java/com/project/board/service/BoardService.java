package com.project.board.service;

import com.project.board.dto.BoardDto;
import com.project.board.entity.Board;
import com.project.board.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * BoardService
 *
 * 역할:
 * - 게시글 비즈니스 로직 처리
 */
@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * 게시글 저장
     */
    public BoardDto save(BoardDto dto) {
        validateDto(dto);

        Board board = Board.create(
                dto.getTitle(),
                dto.getContent(),
                dto.getAuthor()
        );

        return toDto(boardRepository.save(board));
    }

    /**
     * 전체 조회
     */
    @Transactional(readOnly = true)
    public List<BoardDto> findAll() {
        return boardRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * 단일 조회 + 조회수 증가
     */
    public BoardDto findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음 id=" + id));

        board.increaseViewCount();

        return toDto(board);
    }

    /**
     * 삭제
     */
    public void delete(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제 대상 없음 id=" + id);
        }

        boardRepository.deleteById(id);
    }

    /**
     * DTO 검증
     */
    private void validateDto(BoardDto dto) {
        if (dto == null) throw new IllegalArgumentException("dto null");
        if (dto.getTitle() == null || dto.getTitle().isBlank())
            throw new IllegalArgumentException("제목 필수");
        if (dto.getContent() == null || dto.getContent().isBlank())
            throw new IllegalArgumentException("내용 필수");
        if (dto.getAuthor() == null || dto.getAuthor().isBlank())
            throw new IllegalArgumentException("작성자 필수");
    }

    /**
     * Entity → DTO
     */
    private BoardDto toDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .author(board.getAuthor())
                .build();
    }
}