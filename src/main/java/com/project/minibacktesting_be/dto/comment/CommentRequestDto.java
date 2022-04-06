package com.project.minibacktesting_be.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

// Getter, Setter기능 통합
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentRequestDto {
    private String content;

}
