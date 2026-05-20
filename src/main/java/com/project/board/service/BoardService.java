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
            return repo.findActive(pageable).map(BoardDto::from);
        }

        return switch (normalizedSearchType) {
            case "title" -> repo.findActiveByTitle(
                    normalizedKeyword,
                    pageable
            ).map(BoardDto::from);
            case "author" -> repo.findActiveByAuthor(
                    normalizedKeyword,
                    pageable
            ).map(BoardDto::from);
            default -> repo.findActiveByTitleOrAuthor(
                    normalizedKeyword,
                    pageable
            ).map(BoardDto::from);
        };
    }

    @Transactional
    public BoardDto findById(Long id, boolean increaseViewCount) {
        Board board = findBoard(id);

        if (increaseViewCount) {
            board.increaseViewCount();
        }

        return BoardDto.from(board);
    }

    @Transactional
    public BoardDto save(BoardDto dto, String user) {
        Board board = Board.create(
                dto.getTitle(),
                dto.getContent(),
                user,
                dto.getImageUrl()
        );

        return BoardDto.from(repo.save(board));
    }

    @Transactional
    public BoardDto update(Long id, BoardDto dto, String user) {
        Board board = findBoard(id);
        validateOwner(board, user);

        board.update(dto.getTitle(), dto.getContent(), dto.getImageUrl());
        return BoardDto.from(board);
    }

    @Transactional
    public void delete(Long id, String user) {
        Board board = findBoard(id);
        validateOwner(board, user);

        board.softDelete();
    }

    private Board findBoard(Long id) {
        return repo.findById(id)
                .filter(board -> !board.isDeleted())
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    private void validateOwner(Board board, String user) {
        if (!board.getAuthor().equals(user)) {
            throw new RuntimeException("Forbidden");
        }
    }
}
