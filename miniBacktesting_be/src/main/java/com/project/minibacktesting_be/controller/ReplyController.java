//package com.project.minibacktesting_be.controller;
//
//import com.project.minibacktesting_be.dto.comment.CommentRegisterRequestDto;
//import com.project.minibacktesting_be.dto.comment.CommentRegisterResponseDto;
//import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
//import com.project.minibacktesting_be.service.ReplyService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class ReplyController {
//    private final ReplyService replyService;
//
//    // 대댓글 작성하기
//    @PostMapping("/community/reply/{commentId}")
//    public CommentRegisterResponseDto registerReply(@PathVariable Long commentId,
//                                                    @RequestBody CommentRegisterRequestDto requestDto,
//                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return replyService.registerReply(commentId, requestDto, userDetails);
//    }
//}
