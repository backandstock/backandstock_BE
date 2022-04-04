package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.dto.comment.CommentRequestDto;
import com.project.minibacktesting_be.dto.comment.CommentResponseDto;
import com.project.minibacktesting_be.dto.comment.GetCommentsResponseDto;
import com.project.minibacktesting_be.exception.user.UserMatchException;
import com.project.minibacktesting_be.model.Comment;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.presentcheck.PresentCheck;
import com.project.minibacktesting_be.repository.CommentRepository;
import com.project.minibacktesting_be.repository.PortfolioRepository;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import com.project.minibacktesting_be.security.vailidation.Validation;
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


    public CommentResponseDto registerComment(Long portId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        // DB 내부 portfolio 확인
        Portfolio portfolio = PresentCheck.portfoliIsPresentCheck(portId, portfolioRepository);

        //content 유효성검사
        Validation.validationComment(requestDto.getContent());

        Comment comment = Comment.commentBuilder()
                .content(requestDto.getContent())
                .portfolio(portfolio)
                .user(userDetails.getUser())
                .build();


        log.info(comment.getContent());
        log.info("portId {}", comment.getPortfolio().getId());
        log.info("nickname {}", comment.getUser().getNickname());

        // 저장시 대댓글 활용부분 저장
        Comment savedComment = commentRepository.save(comment);
        log.info("savedComment {}", savedComment.getContent());
        savedComment.firstRegistration(savedComment);


        commentRepository.save(savedComment);

        return CommentResponseDto.builder().commentId(savedComment.getId()).build();
    }

    public void updateComment(Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        // DB 내부 comment 확인
        Comment comment = PresentCheck.commentIsPresentCheck(commentId, commentRepository);

        //content 유효성검사
        Validation.validationComment(requestDto.getContent());

        // 작성자 User와 로그인 User 체크
        if (!userDetails.getUser().getId().equals(comment.getUser().getId())) {
            throw new UserMatchException("Comment update user matching error - loginUserId : " +
                    userDetails.getUser().getId() +
                    " / portfolioUserId : " +
                    comment.getUser().getId());
        }

        comment.update(requestDto.getContent());
        commentRepository.save(comment);
    }

    public List<GetCommentsResponseDto> getComments(Long portId) {
        // DB 내부 portfolio 확인
        Portfolio portfolio = PresentCheck.portfoliIsPresentCheck(portId, portfolioRepository);

        List<GetCommentsResponseDto> dtoList = new ArrayList<>();

        List<Comment> commentList = commentRepository.findAllByPortfolio(portfolio);

        for (Comment comment : commentList) {
            if(!comment.getId().equals(comment.getParentComment().getId())){
                for (GetCommentsResponseDto getCommentsResponseDto : dtoList) {
                    if (getCommentsResponseDto.getCommentId().equals(comment.getParentComment().getId())) {
                        getCommentsResponseDto.getReplyList().add(new GetCommentsResponseDto(comment));
                        break;
                    }
                }
            }else{
                dtoList.add(new GetCommentsResponseDto(comment));
            }
        }
        return dtoList;
    }

    public void deleteComment(Long commentId, UserDetailsImpl userDetails) {
        // DB 내부 comment 확인
        Comment comment = PresentCheck.commentIsPresentCheck(commentId, commentRepository);

        // 작성자 User와 로그인 User 체크
        if (!userDetails.getUser().getId().equals(comment.getUser().getId())) {
            throw new UserMatchException("Comment delete user matching error - loginUserId : " +
                    userDetails.getUser().getId() +
                    " / portfolioUserId : " +
                    comment.getUser().getId());
        }
        comment.deleteComment();
        List<Comment> comments = commentRepository.findAllByParentComment(comment);
        commentRepository.deleteAll(comments);
        commentRepository.delete(comment);
    }
}
