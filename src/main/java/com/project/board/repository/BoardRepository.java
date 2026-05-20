package com.project.board.repository;

import com.project.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b where b.deleted is null or b.deleted = false")
    Page<Board> findActive(Pageable pageable);

    @Query("""
            select b
            from Board b
            where (b.deleted is null or b.deleted = false)
              and lower(b.title) like lower(concat('%', :title, '%'))
            """)
    Page<Board> findActiveByTitle(@Param("title") String title, Pageable pageable);

    @Query("""
            select b
            from Board b
            where (b.deleted is null or b.deleted = false)
              and lower(b.author) like lower(concat('%', :author, '%'))
            """)
    Page<Board> findActiveByAuthor(@Param("author") String author, Pageable pageable);

    @Query("""
            select b
            from Board b
            where (b.deleted is null or b.deleted = false)
              and (
                  lower(b.title) like lower(concat('%', :keyword, '%'))
                  or lower(b.author) like lower(concat('%', :keyword, '%'))
              )
            """)
    Page<Board> findActiveByTitleOrAuthor(@Param("keyword") String keyword, Pageable pageable);
}
