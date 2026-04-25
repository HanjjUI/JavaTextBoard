package com.project.board.config;

import com.project.board.entity.Board;
import com.project.board.repository.BoardRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BoardDataInitializer implements ApplicationRunner {

    private final BoardRepository boardRepository;

    public BoardDataInitializer(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        List<Board> boardsWithoutPostNumber = boardRepository.findByPostNumberIsNullOrderByIdAsc();
        long nextPostNumber = boardRepository.findTopByOrderByPostNumberDesc()
                .map(board -> board.getPostNumber() + 1)
                .orElse(1L);

        for (Board board : boardsWithoutPostNumber) {
            board.assignPostNumber(nextPostNumber++);
        }
    }
}
