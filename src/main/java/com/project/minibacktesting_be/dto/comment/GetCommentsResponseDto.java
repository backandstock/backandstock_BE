package com.project.minibacktesting_be.dto.comment;

import com.project.minibacktesting_be.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class GetCommentsResponseDto {
    private Long commentId;
    private String content;
    private String nickname;
    private Long parentId;
    private LocalDateTime CreatedAt;
    private List<GetCommentsResponseDto> replyList = new ArrayList<>();
    private String profileImg;
    private Long userId;

    public GetCommentsResponseDto(Comment c) {
        this.commentId = c.getId();
        this.content = c.getContent();
        this.nickname = c.getUser().getNickname();
        this.parentId = c.getParentComment().getId();
        this.CreatedAt = c.getCreatedAt();
        this.profileImg = c.getUser().getProfileImg();
        this.userId = c.getUser().getId();
    }
}
