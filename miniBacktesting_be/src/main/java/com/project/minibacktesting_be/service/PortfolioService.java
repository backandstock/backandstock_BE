package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.backtesting.BacktestingCal;
import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;
import com.project.minibacktesting_be.dto.portfolio.*;
import com.project.minibacktesting_be.model.PortStock;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.model.User;
import com.project.minibacktesting_be.repository.*;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PortfolioService {
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortStockRepository portStockRepository;
    private final BacktestingCal backtestingCal;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;

    //포트폴리오 저장
    @Transactional
    public PortfolioSaveResponseDto savePortfolio(BacktestingRequestDto backtestingRequestDto, UserDetailsImpl userDetails) {

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
        PortfolioSaveResponseDto portfolioSaveResponseDto = new PortfolioSaveResponseDto();
        portfolioSaveResponseDto.setPortId(portfolio.getId());

        return portfolioSaveResponseDto;
    }

    //내 포트폴리오 전체 불러오기
    @Transactional(readOnly = true)
    public List<PortfolioResponseDto> getAllMyPortfolio(UserDetailsImpl userDetails) {
        List<PortfolioResponseDto> portfolioResponseDtoList = new ArrayList<>();

        Optional<User> userTemp = userRepository.findById(userDetails.getUser().getId());
        User user = userTemp.get();

        List<Portfolio> portfolioList = portfolioRepository.findAllByUser(user);

        if(!portfolioList.isEmpty()) {
            for(Portfolio eachPortfolio : portfolioList){
                Long postId = eachPortfolio.getId();
                Boolean myBest = eachPortfolio.getMyBest();

                LocalDate startDate  = eachPortfolio.getStartDate();
                LocalDate endDate = eachPortfolio.getEndDate();
                Long seedMoney = eachPortfolio.getSeedMoney();

                List<PortStock> portStocks = portStockRepository.findByPortfolio(eachPortfolio);
                List<String> stockList = new ArrayList<>();
                for (PortStock portStockName : portStocks) {
                    stockList.add(portStockName.getStockName());
                }
                List<Integer> ratioList = new ArrayList<>();
                for (PortStock portStockRatio : portStocks) {
                    ratioList.add(portStockRatio.getRatio());
                }

                BacktestingRequestDto backtestingRequestDto = new BacktestingRequestDto();
                backtestingRequestDto.setStartDate(startDate);
                backtestingRequestDto.setEndDate(endDate);
                backtestingRequestDto.setSeedMoney(seedMoney);
                backtestingRequestDto.setStockList(stockList);
                backtestingRequestDto.setRatioList(ratioList);
                BacktestingResponseDto myPortBacktestingCal = backtestingCal.getResult(backtestingRequestDto);

//                BacktestingCal backtestingCal = new BacktestingCal(startDate, endDate, seedMoney, stockList, ratioList);
//                backtestingCal

                PortfolioResponseDto portfolioResponseDto = new PortfolioResponseDto();
                portfolioResponseDto.setPortId(postId);
                portfolioResponseDto.setMyBest(myBest);
                portfolioResponseDto.setPortBacktestingCal(myPortBacktestingCal);

                portfolioResponseDtoList.add(portfolioResponseDto);
            }
        }
        return portfolioResponseDtoList;
    }

    //포트폴리오 상세보기
    @Transactional(readOnly = true)
    public PortfolioDetailsResponseDto getPortfolio(Long portId) {
        Portfolio portfolio = portfolioRepository.findById(portId).orElseThrow(
                () -> new NullPointerException("해당 포트폴리오를 찾을 수 없습니다.")
        );

        Boolean myBest = portfolio.getMyBest();

        LocalDate startDate  = portfolio.getStartDate();
        LocalDate endDate = portfolio.getEndDate();
        Long seedMoney = portfolio.getSeedMoney();
        int likesCnt = portfolio.getLikesCnt();

        List<PortStock> portStocks = portStockRepository.findByPortfolio(portfolio);
        List<String> stockList = new ArrayList<>();
        for (PortStock portStockName : portStocks){
            stockList.add(portStockName.getStockName());
        }
        List<Integer> ratioList = new ArrayList<>();
        for (PortStock portStockRatio : portStocks){
            ratioList.add(portStockRatio.getRatio());
        }

        BacktestingRequestDto backtestingRequestDto = new BacktestingRequestDto();
        backtestingRequestDto.setStartDate(startDate);
        backtestingRequestDto.setEndDate(endDate);
        backtestingRequestDto.setSeedMoney(seedMoney);
        backtestingRequestDto.setStockList(stockList);
        backtestingRequestDto.setRatioList(ratioList);
        BacktestingResponseDto portBacktestingCal = backtestingCal.getResult(backtestingRequestDto);

        PortfolioDetailsResponseDto portfolioDetailsResponseDto = new PortfolioDetailsResponseDto();
        portfolioDetailsResponseDto.setPortId(portId);
        portfolioDetailsResponseDto.setMyBest(myBest);
        portfolioDetailsResponseDto.setLikesCnt(likesCnt);
        portfolioDetailsResponseDto.setCommentCnt(portfolio.getComments().size());
        portfolioDetailsResponseDto.setPortBacktestingCal(portBacktestingCal);

        return portfolioDetailsResponseDto;
    }

    //포트폴리오 비교하기
    @Transactional
    public List<PortfolioCompareResponseDto> comparePortfolio(PortfolioCompareRequestDto portfolioCompareRequestDto, UserDetailsImpl userDetails) {
        List<PortfolioCompareResponseDto> portfolioCompareResponseDtoList = new ArrayList<>();

        Optional<User> userTemp = userRepository.findById(userDetails.getUser().getId());
        User user = userTemp.get();
        List<Portfolio> portfolioList = portfolioRepository.findAllByUser(user);

        List<Long> portIdList = portfolioCompareRequestDto.getPortIdList();

        for(int i = 0; i < portIdList.size(); i++){
            Long comparePortId = portIdList.get(i);
            for(Portfolio eachPort : portfolioList){
                Long eachPortId = eachPort.getId();
                if(comparePortId == eachPortId){
                    LocalDate startDate = eachPort.getStartDate();
                    LocalDate endDate = eachPort.getEndDate();
                    Long seedMoney = eachPort.getSeedMoney();

                    List<PortStock> portStocks = portStockRepository.findByPortfolio(eachPort);
                    List<String> stockList = new ArrayList<>();
                    for(PortStock portStockName : portStocks){
                        stockList.add(portStockName.getStockName());
                    }
                    List<Integer> ratioList = new ArrayList<>();
                    for(PortStock portStockRatio : portStocks){
                        ratioList.add(portStockRatio.getRatio());
                    }
                    BacktestingRequestDto backtestingRequestDto = new BacktestingRequestDto();
                    backtestingRequestDto.setStartDate(startDate);
                    backtestingRequestDto.setEndDate(endDate);
                    backtestingRequestDto.setSeedMoney(seedMoney);
                    backtestingRequestDto.setStockList(stockList);
                    backtestingRequestDto.setRatioList(ratioList);
                    BacktestingResponseDto compareBacktestingCal = backtestingCal.getResult(backtestingRequestDto);

                    PortfolioCompareResponseDto portfolioCompareResponseDto = new PortfolioCompareResponseDto();
                    portfolioCompareResponseDto.setPortId(comparePortId);
                    portfolioCompareResponseDto.setPortBacktestingCal(compareBacktestingCal);

                    portfolioCompareResponseDtoList.add(portfolioCompareResponseDto);
                }
            }
        }
        return portfolioCompareResponseDtoList;
    }

    //포트폴리오 삭제
    @Transactional
    public HashMap<String, Long> deletePortfolio(Long portId, UserDetailsImpl userDetails) {
        Portfolio portfolio = portfolioRepository.findById(portId).orElseThrow(
                () -> new NullPointerException("해당 포트폴리오를 찾을 수 없습니다.")
        );

        if(!portfolio.getUser().getId().equals(userDetails.getUser().getId())){
            throw new IllegalArgumentException("나의 포트폴리오만 삭제할 수 있습니다.");
        }

        HashMap<String, Long> responseId = new HashMap<>();
        responseId.put("portId", portfolio.getId());


        likesRepository.deleteAllByPortfolioId(portId);
        commentRepository.deleteAllByPortfolioId(portId);
        portStockRepository.deleteAllByPortfolioId(portId);
        portfolioRepository.delete(portfolio);

        return responseId;
    }

    //포트폴리오 자랑하기
    @Transactional
    public PortfolioMyBestResponseDto myBestPortfolio(PortfolioMyBestRequestDto portfolioMyBestRequestDto, UserDetailsImpl userDetails) {
        Portfolio portfolio = portfolioRepository.findById(portfolioMyBestRequestDto.getPortId()).orElseThrow(
                () -> new IllegalArgumentException("포트폴리오가 존재하지 않습니다.")
        );
        if(!portfolio.getUser().getId().equals(userDetails.getUser().getId())){
            throw new IllegalArgumentException("나의 포트폴리오만 자랑할 수 있습니다.");
        }
        if(portfolioMyBestRequestDto.isMyBest()){
            portfolio.setMyBest(true);
        } else {
            portfolio.setMyBest(false);
        }
        PortfolioMyBestResponseDto portfolioMyBestResponseDto = new PortfolioMyBestResponseDto();
        portfolioMyBestResponseDto.setMyBest(portfolio.getMyBest());
        return portfolioMyBestResponseDto;
    }
}
