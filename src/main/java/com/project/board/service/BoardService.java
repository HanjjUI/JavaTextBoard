package com.project.board.service;

import com.project.board.dto.BoardDto;
import com.project.board.entity.Board;
import com.project.board.repository.BoardRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

    private final BoardRepository repo;

    public BoardService(BoardRepository repo){
        this.repo = repo;
    }

    public Page<BoardDto> findAll(Pageable p, String k, String a){
        if(k==null) k="";
        if(a==null) a="";

        return repo.findByTitleContainingAndAuthorContaining(k,a,p)
                .map(this::toDto);
    }

    @Transactional
    public BoardDto findById(Long id){
        Board b = repo.findById(id).orElseThrow();
        b.increaseViewCount();
        return toDto(b);
    }

    public BoardDto save(BoardDto d, String user){
        return toDto(repo.save(Board.create(d.getTitle(), d.getContent(), user)));
    }

    @Transactional
    public BoardDto update(Long id, BoardDto d, String user){
        Board b = repo.findById(id).orElseThrow();
        if(!b.getAuthor().equals(user)) throw new RuntimeException("권한 없음");
        b.update(d.getTitle(), d.getContent());
        return toDto(b);
    }

    public void delete(Long id, String user){
        Board b = repo.findById(id).orElseThrow();
        if(!b.getAuthor().equals(user)) throw new RuntimeException("권한 없음");
        repo.delete(b);
    }

    private BoardDto toDto(Board b){
        return BoardDto.builder()
                .id(b.getId())
                .title(b.getTitle())
                .content(b.getContent())
                .author(b.getAuthor())
                .createdAt(b.getCreatedAt())
                .viewCount(b.getViewCount())
                .build();
    }
}