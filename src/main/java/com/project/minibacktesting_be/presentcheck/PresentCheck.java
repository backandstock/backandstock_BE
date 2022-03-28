package com.project.minibacktesting_be.presentcheck;

import com.project.minibacktesting_be.exception.comment.CommentNotFoundException;
import com.project.minibacktesting_be.exception.portfolio.PortfolioNotFoundException;
import com.project.minibacktesting_be.model.Comment;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.repository.CommentRepository;
import com.project.minibacktesting_be.repository.PortfolioRepository;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;

import java.util.Optional;

public class PresentCheck {
    // 상위 부모나 인터페이스 만들어서 레포지토리를 상속받으면
    // 캐스팅가능하다
    // 상위에 파인드 바이아이디
    //
    public static Portfolio portfoliIsPresentCheck(Long id, PortfolioRepository repository){
        Optional<Portfolio> optionalPortfolio = repository.findById(id);
        if(!optionalPortfolio.isPresent()){
            throw new PortfolioNotFoundException(id);
        }

        return optionalPortfolio.get();
    }

    public static Comment commentIsPresentCheck(Long commentId, CommentRepository repository){
        Optional<Comment> optionalComment = repository.findById(commentId);
        if(!optionalComment.isPresent()){
            throw new CommentNotFoundException(commentId);
        }

        return optionalComment.get();
    }

}
