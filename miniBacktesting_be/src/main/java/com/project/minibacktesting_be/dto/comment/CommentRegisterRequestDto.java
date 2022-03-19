package com.project.minibacktesting_be.dto.comment;

import lombok.Data;
import lombok.RequiredArgsConstructor;

// Getter, Setter기능 통합
@Data
@RequiredArgsConstructor
public class CommentRegisterRequestDto {
    private String content;
}
