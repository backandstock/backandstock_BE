package com.project.minibacktesting_be.controller;

import com.project.minibacktesting_be.dto.CommentRegisterRequestDto;
import com.project.minibacktesting_be.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/community/comment/{portId}")
    public void registerComment(@PathVariable Long portId, @RequestBody CommentRegisterRequestDto requestDto){
//        commentService.registerComment(portId, requestDto);
    }
}
