package com.project.minibacktesting_be.controller;

import com.project.minibacktesting_be.dto.comment.CommentRegisterRequestDto;
import com.project.minibacktesting_be.dto.comment.CommentRegisterResponseDto;
import com.project.minibacktesting_be.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/community/comment/{portId}")
    public CommentRegisterResponseDto registerComment(@PathVariable Long portId, @RequestBody CommentRegisterRequestDto requestDto){
        return commentService.registerComment(portId, requestDto);
    }



}
