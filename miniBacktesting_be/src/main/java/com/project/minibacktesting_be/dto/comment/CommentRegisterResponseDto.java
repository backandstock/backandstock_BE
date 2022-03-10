package com.project.minibacktesting_be.dto.comment;

import lombok.Builder;
import lombok.Data;

// Getter, Setter기능 통합
@Data
@Builder
public class CommentRegisterResponseDto {
    private Long commentId;
}
