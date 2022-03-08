package com.project.minibacktesting_be.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CommentRegisterRequestDto {
    private String content;
    private String nickname;
}
