package com.project.minibacktesting_be.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder(builderMethodName = "commentBuilder")
public class Comment extends Timestamped{
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    // cascadeType.ALL 설정시 오류발생.
    // cascadeType.MERGE로 업데이트하는 방식으로 문제 해결
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String content;

    // 대댓글 구현용 필드 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Comment parentComment;

    public void update(String content) {
        this.content = content;
    }

    public void firstRegistration(Comment comment){
        this.parentComment = comment;
    }

    public void deleteComment(){
        this.parentComment = null;
    }
}