package com.project.board.repository;

import com.project.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Board> findByAuthorContainingIgnoreCase(String author, Pageable pageable);

    Page<Board> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title,
            String author,
            Pageable pageable
    );

    @Query("select coalesce(max(b.postNumber), 0) from Board b")
    Long findMaxPostNumber();

    List<Board> findByPostNumberIsNullOrderByIdAsc();
}
