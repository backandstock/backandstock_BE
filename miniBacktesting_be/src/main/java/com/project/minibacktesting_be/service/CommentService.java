package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.dto.comment.CommentRegisterRequestDto;
import com.project.minibacktesting_be.dto.comment.CommentRegisterResponseDto;
import com.project.minibacktesting_be.dto.comment.CommentUpdateRequestDto;
import com.project.minibacktesting_be.dto.comment.GetCommentsResponseDto;
import com.project.minibacktesting_be.model.Comment;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.presentcheck.PresentCheck;
import com.project.minibacktesting_be.repository.CommentRepository;
import com.project.minibacktesting_be.repository.PortfolioRepository;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import com.project.minibacktesting_be.vailidation.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PortfolioRepository portfolioRepository;

    public CommentRegisterResponseDto registerComment(Long portId, CommentRegisterRequestDto requestDto, UserDetailsImpl userDetails) {
        // DB 내부 portfolio 확인
        Portfolio portfolio = PresentCheck.portfoliIsPresentCheck(portId, portfolioRepository);

        //content 유효성검사
        Validation.validationComment(requestDto.getContent());

        Comment comment = Comment.commentBuilder()
                .nickname(userDetails.getUser().getNickname())
                .content(requestDto.getContent())
                .portfolio(portfolio)
                .user(userDetails.getUser())
                .build();

        // 저장시 대댓글 활용부분 저장
        Comment savedComment = commentRepository.save(comment);
        savedComment.firstRegistration(savedComment);
        commentRepository.save(savedComment);

        return CommentRegisterResponseDto.builder().commentId(savedComment.getId()).build();
    }

    public void updateComment(Long commentId, CommentUpdateRequestDto requestDto, UserDetailsImpl userDetails) {
        // DB 내부 comment 확인
        Comment comment = PresentCheck.commentIsPresentCheck(commentId, commentRepository);

        //content 유효성검사
        Validation.validationComment(requestDto.getContent());

        // 작성자 User와 로그인 User 체크
        if (!userDetails.getUser().getId().equals(comment.getUser().getId())) {
            throw new IllegalArgumentException("댓글 수정은 작성자만 가능합니다.");
        }

        comment.update(requestDto.getContent());
        commentRepository.save(comment);
    }

    public List<GetCommentsResponseDto> getComments(Long portId) {
        // DB 내부 portfolio 확인
        Portfolio portfolio = PresentCheck.portfoliIsPresentCheck(portId, portfolioRepository);

        List<GetCommentsResponseDto> commentsResponseDtoList = new ArrayList<>();

        List<Comment> commentList = commentRepository.findAllByPortfolio(portfolio);

        for (Comment c : commentList) {
            commentsResponseDtoList.add(new GetCommentsResponseDto(c));
        }

        return commentsResponseDtoList;
    }

    public void deleteComment(Long commentId, UserDetailsImpl userDetails) {
        // DB 내부 comment 확인
        Comment comment = PresentCheck.commentIsPresentCheck(commentId, commentRepository);

        // 작성자 User와 로그인 User 체크
        if (!userDetails.getUser().getId().equals(comment.getUser().getId())) {
            throw new IllegalArgumentException("댓글 삭제는 작성자만 가능합니다.");
        }

        commentRepository.delete(comment);
        log.info("댓글삭제완료");
    }
}
