package com.project.minibacktesting_be.dto.comment;

import com.project.minibacktesting_be.model.Comment;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetCommentsResponseDto {
    private Long commentId;
    private String content;
    private String nickname;
    private Long parentId;
    private List<GetCommentsResponseDto> replyList = new ArrayList<>();

    public GetCommentsResponseDto(Comment c) {
        this.commentId = c.getId();
        this.content = c.getContent();
        this.nickname = c.getNickname();
        this.parentId = c.getParentComment().getId();
    }
}
