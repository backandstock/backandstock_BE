package com.project.minibacktesting_be.controller;

import com.project.minibacktesting_be.dto.comment.CommentRegisterRequestDto;
import com.project.minibacktesting_be.dto.comment.CommentRegisterResponseDto;
import com.project.minibacktesting_be.dto.comment.CommentUpdateRequestDto;
import com.project.minibacktesting_be.dto.comment.GetCommentsResponseDto;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import com.project.minibacktesting_be.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/community/comment/{portId}")
    public List<GetCommentsResponseDto> getComments(@PathVariable Long portId){
        return commentService.getComments(portId);
    }

    @PostMapping("/community/comment/{portId}")
    public CommentRegisterResponseDto registerComment(@PathVariable Long portId,
                                                      @RequestBody CommentRegisterRequestDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.registerComment(portId, requestDto, userDetails);
    }

    @PutMapping("/community/comment/{commentId}")
    public void updateComment(@PathVariable Long commentId,
                              @RequestBody CommentUpdateRequestDto requestDto,
                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.updateComment(commentId, requestDto, userDetails);
    }

    @DeleteMapping("/community/comment/{commentId}")
    public void deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(commentId, userDetails);
    }
}
