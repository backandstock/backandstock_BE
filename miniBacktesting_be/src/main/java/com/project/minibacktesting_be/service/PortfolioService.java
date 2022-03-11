package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.backtesting.BacktestingCal;
import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.portfolio.PortfolioResponseDto;
import com.project.minibacktesting_be.model.PortStock;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.model.User;
import com.project.minibacktesting_be.repository.PortStockRepository;
import com.project.minibacktesting_be.repository.PortfolioRepository;
import com.project.minibacktesting_be.repository.StockRepository;
import com.project.minibacktesting_be.repository.UserRepository;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PortfolioService {
    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortStockRepository portStockRepository;
    private final BacktestingCal backtestingCal;

    //포트폴리오 입력값 저장
    @Transactional
    public PortfolioResponseDto savePortfolio(BacktestingRequestDto backtestingRequestDto, UserDetailsImpl userDetails) {

        Long userId = userDetails.getUser().getId();
        Optional<User> userTemp = userRepository.findById(userId);
        User user = userTemp.get();


        // 1. 포트폴리오 갯수 validation
        List<Portfolio> portfolioNum = portfolioRepository.findAllByUser(user);
        if(portfolioNum.size() > 2) {
            throw new IllegalArgumentException("저장 가능한 포트폴리오는 최대 3개 입니다.");
        }

        // 2. 포트폴리오 저장
        Portfolio portfolio = Portfolio.createPortfolio(backtestingRequestDto.getStartDate(),
                backtestingRequestDto.getEndDate(), backtestingRequestDto.getSeedMoney(), user);

        double finalYield;
        finalYield = backtestingCal.getResult(backtestingRequestDto).getFinalYield();
        portfolio.updateFinalYield(finalYield);


//        Long portId = portfolioRepository.save(portfolio).getId();
//        Optional<Portfolio> stockPortfolio = portfolioRepository.findById(portId);

        // 3. 포트폴리오 상세 내용 저장
        List<PortStock> portStockList = new ArrayList<>();
        List<Integer> ratioList = backtestingRequestDto.getRatioList();
        List<String> stockList = backtestingRequestDto.getStockList();
        for (int i = 0; i < stockList.size(); i++) {
            String stockName = stockList.get(i);
//            Stock stock = stockRepository.findByStockNameAndDate(stockName, portfolioRequestDto.getStartDate());
            Integer ratio = ratioList.get(i);
            PortStock portStock = PortStock.builder()
                    .portfolio(portfolio)
                    .ratio(ratio)
                    .stockName(stockName)
                    .build();
            portStockRepository.save(portStock);
            portStockList.add(portStock);
        }

        portfolio.addPortStocks(portStockList);

        portfolioRepository.save(portfolio);

        // 4. 저장한 포트폴리오 아이디 반환
        PortfolioResponseDto portfolioResponseDto = new PortfolioResponseDto();
        portfolioResponseDto.setPortId(portfolio.getId());

        return portfolioResponseDto;
    }

}
