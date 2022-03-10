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
    @Column(name = "COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PORTFOLIO_ID")
    private Portfolio portfolio;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String nickname;

    public static CommentBuilder builder(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("필수 파라미터 누락");
        }
        return new CommentBuilder().id(id);
    }
}