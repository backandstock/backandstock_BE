package com.project.minibacktesting_be.dto.comment;

import com.project.minibacktesting_be.model.Comment;
import lombok.Data;

@Data
public class GetCommentsResponseDto {
    private Long commentId;
    private String content;
    private String nickname;

    public GetCommentsResponseDto(Comment c) {
        this.commentId = c.getId();
        this.content = c.getContent();
        this.nickname = c.getNickname();
    }
}
