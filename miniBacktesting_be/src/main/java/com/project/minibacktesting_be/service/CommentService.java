package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.dto.comment.CommentRegisterRequestDto;
import com.project.minibacktesting_be.dto.comment.CommentRegisterResponseDto;
import com.project.minibacktesting_be.model.Comment;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.repository.CommentRepository;
import com.project.minibacktesting_be.repository.PortfolioReposirory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PortfolioReposirory portfolioReposirory;

    public CommentRegisterResponseDto registerComment(Long portId, CommentRegisterRequestDto requestDto) {
        Optional<Portfolio> portfolio = portfolioReposirory.findById(portId);
        if(!portfolio.isPresent()){
            throw new IllegalArgumentException("해당 포트폴리오를 찾을 수 없습니다.");
        }

        Comment comment = Comment.commentBuilder()
                .nickname(requestDto.getNickname())
                .content(requestDto.getContent())
                .portfolio(portfolio.get())
                .build();

        return CommentRegisterResponseDto.builder().commentId(commentRepository.save(comment).getId()).build();
    }
}
