package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.dto.portfolio.PortfolioSaveResponseDto;
import com.project.minibacktesting_be.exception.portfolio.PortfolioNotFoundException;
import com.project.minibacktesting_be.exception.user.UserNotFoundException;
import com.project.minibacktesting_be.model.Likes;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.model.User;
import com.project.minibacktesting_be.presentcheck.PresentCheck;
import com.project.minibacktesting_be.repository.LikesRepository;
import com.project.minibacktesting_be.repository.PortfolioRepository;
import com.project.minibacktesting_be.repository.UserRepository;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@RequiredArgsConstructor
@Service
public class LikesService {

    private final LikesRepository likesRepository;
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    @Transactional
    public PortfolioSaveResponseDto postLikes(Long portId,UserDetailsImpl userDetails) {

        // 포트폴리오 찾기
//        Portfolio portfolio = getPortfolio(portId);
        Portfolio portfolio = PresentCheck.portfoliIsPresentCheck(portId, portfolioRepository);


       // 유저 찾기
        User user = getUser(userDetails);

        Likes likes = new Likes(portfolio, user);
        likesRepository.save(likes);


        // 해당 포스팅의 좋아요 수
        List<Likes> likesList = likesRepository.findByPortfolio(portfolio);

        portfolio.updateLikesCnt((long) likesList.size());
        PortfolioSaveResponseDto portfolioSaveResponseDto = new PortfolioSaveResponseDto();
        portfolioSaveResponseDto.setPortId(portfolio.getId());
        return portfolioSaveResponseDto;
    }



    @Transactional
    public PortfolioSaveResponseDto postDislikes(Long portId,UserDetailsImpl userDetails) {
        // 포트폴리오 찾기
        Portfolio portfolio = PresentCheck.portfoliIsPresentCheck(portId, portfolioRepository);
        // 유저 찾기
        User user = getUser(userDetails);

        // likes 삭제하기
        List<Likes> likes = likesRepository.findByPortfolioAndUser(portfolio, user);
        likesRepository.deleteById(likes.get(0).getId());

        // 해당 포스팅의 좋아요 수
        List<Likes> likesList = likesRepository.findByPortfolio(portfolio);
        portfolio.updateLikesCnt((long) likesList.size());
        PortfolioSaveResponseDto portfolioSaveResponseDto = new PortfolioSaveResponseDto();
        portfolioSaveResponseDto.setPortId(portfolio.getId());
        return portfolioSaveResponseDto;
    }


    private User getUser(UserDetailsImpl userDetails) {
//        User user =
        return userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                () -> new UserNotFoundException("User not Found")
        );
    }


}
