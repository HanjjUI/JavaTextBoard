package com.project.board.service;

import com.project.board.dto.BoardDto;
import com.project.board.entity.Board;
import com.project.board.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

    private final BoardRepository repo;

    public BoardService(BoardRepository repo) {
        this.repo = repo;
    }

    public Page<BoardDto> findAll(Pageable pageable, String searchType, String keyword) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedSearchType = searchType == null ? "titleAuthor" : searchType.trim();

        if (normalizedKeyword.isEmpty()) {
            return repo.findAll(pageable).map(this::toDto);
        }

        return switch (normalizedSearchType) {
            case "title" -> repo.findByTitleContainingIgnoreCase(normalizedKeyword, pageable).map(this::toDto);
            case "author" -> repo.findByAuthorContainingIgnoreCase(normalizedKeyword, pageable).map(this::toDto);
            default -> repo.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                    normalizedKeyword,
                    normalizedKeyword,
                    pageable
            ).map(this::toDto);
        };
    }

    @Transactional
    public BoardDto findById(Long id, boolean increaseViewCount) {
        Board board = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (increaseViewCount) {
            board.increaseViewCount();
        }

        return toDto(board);
    }

    @Transactional
    public BoardDto save(BoardDto dto, String user) {
        Long nextPostNumber = repo.findMaxPostNumber() + 1;

        Board board = Board.create(
                nextPostNumber,
                dto.getTitle(),
                dto.getContent(),
                user
        );

        return toDto(repo.save(board));
    }

    @Transactional
    public BoardDto update(Long id, BoardDto dto, String user) {
        Board board = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!board.getAuthor().equals(user)) {
            throw new RuntimeException("Forbidden");
        }

        board.update(dto.getTitle(), dto.getContent());
        return toDto(board);
    }

    public void delete(Long id, String user) {
        Board board = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!board.getAuthor().equals(user)) {
            throw new RuntimeException("Forbidden");
        }

        repo.delete(board);
    }

    private BoardDto toDto(Board board) {
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
