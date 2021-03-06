package com.project.minibacktesting_be.controller;

import com.project.minibacktesting_be.dto.comment.CommentRequestDto;
import com.project.minibacktesting_be.dto.comment.CommentResponseDto;
import com.project.minibacktesting_be.dto.comment.GetCommentsResponseDto;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import com.project.minibacktesting_be.service.CommentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 코멘트 전체 가져오기
    @ApiOperation(value = "코멘트 가져오기")
    @GetMapping("/portfolios/{portId}/comments")
    public List<GetCommentsResponseDto> getComments(@PathVariable Long portId){
        log.info("코멘트 가져오기 portId : {}",portId);
        return commentService.getComments(portId);
    }

    // 코멘트 등록하기
    @ApiOperation(value = "코멘트 등록하기")
    @PostMapping("/portfolios/{portId}/comments")
    public CommentResponseDto registerComment(@PathVariable Long portId,
                                              @RequestBody CommentRequestDto requestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info("코멘트 등록하기 userId : {} portId : {}",
                userDetails.getUser().getId(),portId);

        return commentService.registerComment(portId, requestDto, userDetails);
    }

    // 코멘트 수정하기
    @ApiOperation(value = "코멘트 수정하기")
    @PutMapping("/comments/{commentId}")
    public void updateComment(@PathVariable Long commentId,
                              @RequestBody CommentRequestDto requestDto,
                              @AuthenticationPrincipal UserDetailsImpl userDetails){

        log.info("코멘트 수정하기 userId : {} commentId : {}",
                userDetails.getUser().getId(),commentId);
        commentService.updateComment(commentId, requestDto, userDetails);
    }

    // 코멘트 삭제하기
    @ApiOperation(value = "코멘트 삭제하기")
    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId,
                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info("코멘트 삭제하기 userId : {} commentId : {}",
                userDetails.getUser().getId(),commentId);
        commentService.deleteComment(commentId, userDetails);
    }



    // 대댓글 작성하기
    @ApiOperation(value = "대댓글 작성하기")
    @PostMapping("/comments/{commentId}")
    public CommentResponseDto registerReply(@PathVariable Long commentId,
                                            @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info("대댓글 작성하기 userId : {} commentId : {}",
                userDetails.getUser().getId(),commentId);
        return commentService.registerReply(commentId, requestDto, userDetails);
    }
}
