package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.dto.comment.CommentRegisterRequestDto;
import com.project.minibacktesting_be.dto.comment.CommentRegisterResponseDto;
import com.project.minibacktesting_be.model.Comment;
import com.project.minibacktesting_be.presentcheck.PresentCheck;
import com.project.minibacktesting_be.repository.CommentRepository;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {
    private final CommentRepository commentRepository;

    public CommentRegisterResponseDto registerReply(Long commentId, CommentRegisterRequestDto requestDto, UserDetailsImpl userDetails) {
        Comment comment =  PresentCheck.commentIsPresentCheck(commentId, commentRepository);
        if(comment.getDeep() > 1){
            throw new IllegalArgumentException("대댓글은 3개까지만 작성 가능합니다.");
        }
        Comment reply = Comment.commentBuilder()
                .portfolio(comment.getPortfolio())
                .user(userDetails.getUser())
                .content(requestDto.getContent())
                .nickname(userDetails.getUser().getNickname())
                .comment(comment)
                .deep(comment.getDeep()+1L)
                .groupId(comment.getGroupId())
                .build();
        return CommentRegisterResponseDto.builder().commentId(commentRepository.save(reply).getId()).build();
    }
}
