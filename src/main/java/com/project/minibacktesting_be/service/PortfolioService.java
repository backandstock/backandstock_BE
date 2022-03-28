package com.project.minibacktesting_be.service;

import com.project.minibacktesting_be.backtesting.BacktestingCal;
import com.project.minibacktesting_be.dto.backtesting.BacktestingRequestDto;
import com.project.minibacktesting_be.dto.backtesting.BacktestingResponseDto;
import com.project.minibacktesting_be.dto.portfolio.*;
import com.project.minibacktesting_be.exception.portfolio.PortfolioNotFoundException;
import com.project.minibacktesting_be.exception.portfolio.PortfolioSaveOverException;
import com.project.minibacktesting_be.exception.user.UserMatchException;
import com.project.minibacktesting_be.model.PortStock;
import com.project.minibacktesting_be.model.Portfolio;
import com.project.minibacktesting_be.model.User;
import com.project.minibacktesting_be.presentcheck.PresentCheck;
import com.project.minibacktesting_be.repository.*;
import com.project.minibacktesting_be.security.provider.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class PortfolioService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


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
            throw new PortfolioSaveOverException("Saving portfolio excess error", userId);
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

        List<Portfolio> portfolioList = portfolioRepository.findPortfolioFetchPortStock(user);

        if(!portfolioList.isEmpty()) {
            for(Portfolio eachPortfolio : portfolioList){
                Long postId = eachPortfolio.getId();
                Boolean myBest = eachPortfolio.getMyBest();

                LocalDate startDate  = eachPortfolio.getStartDate();
                LocalDate endDate = eachPortfolio.getEndDate();
                Long seedMoney = eachPortfolio.getSeedMoney();

                List<PortStock> portStocks = eachPortfolio.getPortStocks();
                List<String> stockList = new ArrayList<>();
                for (PortStock portStockName : portStocks) {
                    stockList.add(portStockName.getStockName());
                }
                List<Integer> ratioList = new ArrayList<>();
                for (PortStock portStockRatio : portStocks) {
                    ratioList.add(portStockRatio.getRatio());
                }

                ValueOperations<String, Object> vop = redisTemplate.opsForValue();
                BacktestingResponseDto myPortBacktestingCal;

                if(vop.get("port"+eachPortfolio.getId()) != null){
                    myPortBacktestingCal = (BacktestingResponseDto) vop.get("port"+eachPortfolio.getId());
                    log.info("redis port : {}", eachPortfolio.getId());
//                    System.out.println("redis port"+eachPortfolio.getId());
                }else{
                    BacktestingRequestDto backtestingRequestDto = new BacktestingRequestDto();
                    backtestingRequestDto.setStartDate(startDate);
                    backtestingRequestDto.setEndDate(endDate);
                    backtestingRequestDto.setSeedMoney(seedMoney);
                    backtestingRequestDto.setStockList(stockList);
                    backtestingRequestDto.setRatioList(ratioList);
                    myPortBacktestingCal = backtestingCal.getResult(backtestingRequestDto);
                    vop.set("port"+eachPortfolio.getId(), myPortBacktestingCal);
                    log.info("db port : {}", eachPortfolio.getId());
//                    System.out.println("db port"+eachPortfolio.getId());

                }

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
//        Portfolio portfolio = portfolioRepository.findById(portId).orElseThrow(
//                () -> new PortfolioNotFoundException(portId)
//        );

        Portfolio portfolio = PresentCheck.portfoliIsPresentCheck(portId, portfolioRepository);


        Boolean myBest = portfolio.getMyBest();
        String nickname = portfolio.getUser().getNickname();

        LocalDate startDate  = portfolio.getStartDate();
        LocalDate endDate = portfolio.getEndDate();
        Long seedMoney = portfolio.getSeedMoney();
        long likesCnt = portfolio.getLikesCnt();

        List<PortStock> portStocks = portStockRepository.findByPortfolio(portfolio);
        List<String> stockList = new ArrayList<>();
        for (PortStock portStockName : portStocks){
            stockList.add(portStockName.getStockName());
        }
        List<Integer> ratioList = new ArrayList<>();
        for (PortStock portStockRatio : portStocks){
            ratioList.add(portStockRatio.getRatio());
        }


        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
        BacktestingResponseDto portBacktestingCal;

        if(vop.get("port"+portfolio.getId()) != null){
            portBacktestingCal = (BacktestingResponseDto) vop.get("port"+portfolio.getId());
            log.info("redis portDetail : {}", portfolio.getId());
//            System.out.println("redis portDetail" + portfolio.getId());
        }else{
            BacktestingRequestDto backtestingRequestDto = new BacktestingRequestDto();
            backtestingRequestDto.setStartDate(startDate);
            backtestingRequestDto.setEndDate(endDate);
            backtestingRequestDto.setSeedMoney(seedMoney);
            backtestingRequestDto.setStockList(stockList);
            backtestingRequestDto.setRatioList(ratioList);
            portBacktestingCal = backtestingCal.getResult(backtestingRequestDto);
            vop.set("port"+portfolio.getId(), portBacktestingCal);
            log.info("db portDetail : {}", portfolio.getId());
//            System.out.println("db portDetail" + portfolio.getId());

        }

        PortfolioDetailsResponseDto portfolioDetailsResponseDto = new PortfolioDetailsResponseDto();
        portfolioDetailsResponseDto.setPortId(portId);
        portfolioDetailsResponseDto.setMyBest(myBest);
        portfolioDetailsResponseDto.setLikesCnt((int) likesCnt);
        portfolioDetailsResponseDto.setNickname(nickname);
        portfolioDetailsResponseDto.setStockRatio(ratioList);
        portfolioDetailsResponseDto.setCommentCnt(portfolio.getComments().size());
        portfolioDetailsResponseDto.setPortBacktestingCal(portBacktestingCal);

        return portfolioDetailsResponseDto;
    }

    //포트폴리오 비교하기
    @Transactional
    public PortfolioCompareDto.Response comparePortfolio(PortfolioCompareDto.Request portCompareRequestDto, UserDetailsImpl userDetails) {
        Optional<User> userTemp = userRepository.findById(userDetails.getUser().getId());
        User user = userTemp.get();
        //내 포트폴리오 리스트
        List<Portfolio> portfolioList = portfolioRepository.findPortfolioFetchPortStock(user);
        //비교할 포트폴리오 Id 리스트
        List<Long> portIdList = portCompareRequestDto.getPortIdList();

        List<PortfolioRankDto> portfolioRankDtos = new ArrayList<>();
        List<PortfolioHighYieldDto> portfolioHighYieldDtos = new ArrayList<>();
        List<PortfolioLowYieldDto> portfolioLowYieldDtos = new ArrayList<>();

        for(int i = 0; i < portIdList.size(); i++){
            Long comparePortId = portIdList.get(i);
            for(Portfolio eachPort : portfolioList){
                Long eachPortId = eachPort.getId();
                if(comparePortId.equals(eachPortId)){
                    LocalDate startDate = eachPort.getStartDate();
                    LocalDate endDate = eachPort.getEndDate();
                    Long seedMoney = eachPort.getSeedMoney();

                    List<PortStock> portStocks = eachPort.getPortStocks();
                    List<String> stockList = new ArrayList<>();
                    for(PortStock portStockName : portStocks){
                        stockList.add(portStockName.getStockName());
                    }
                    List<Integer> ratioList = new ArrayList<>();
                    for(PortStock portStockRatio : portStocks){
                        ratioList.add(portStockRatio.getRatio());
                    }

                    ValueOperations<String, Object> vop = redisTemplate.opsForValue();
                    BacktestingResponseDto compareBacktestingCal;

                    if(vop.get("port"+eachPortId) != null){
                        compareBacktestingCal = (BacktestingResponseDto) vop.get("port"+eachPortId);
                        log.info("redis portDetail : {}", eachPortId);
//                        System.out.println("redis portDetail" + eachPortId);
                    }else{
                        BacktestingRequestDto backtestingRequestDto = new BacktestingRequestDto();
                        backtestingRequestDto.setStartDate(startDate);
                        backtestingRequestDto.setEndDate(endDate);
                        backtestingRequestDto.setSeedMoney(seedMoney);
                        backtestingRequestDto.setStockList(stockList);
                        backtestingRequestDto.setRatioList(ratioList);
                        compareBacktestingCal = backtestingCal.getResult(backtestingRequestDto);

                        vop.set("port"+eachPortId, compareBacktestingCal);
                        log.info("db port : {}", eachPortId);
//                        System.out.println("db port" + eachPortId);
                }

                    PortfolioRankDto portfolioRankDto = PortfolioRankDto.builder()
                            .portId(comparePortId)
                            .finalYield(compareBacktestingCal.getFinalYield())
                            .finalMoney(compareBacktestingCal.getFinalMoney())
                            .stockName(stockList)
                            .stockRatio(ratioList)
                            .startDate(YearMonth.from(startDate))
                            .endDate(YearMonth.from(endDate))
                            .build();
                    portfolioRankDtos.add(portfolioRankDto);

                    List<String> months = compareBacktestingCal.getMonths();
                    List<Double> monthYield = compareBacktestingCal.getMonthYield();
                    Double highYield = Collections.max(monthYield);
                    Double lowYield = Collections.min(monthYield);

                    PortfolioHighYieldDto portfolioHighYieldDto = PortfolioHighYieldDto.builder()
                            .portId(comparePortId)
                            .highYield(highYield)
                            .highYieldDate(YearMonth.parse(months.get(monthYield.indexOf(highYield))))
                            .build();
                    portfolioHighYieldDtos.add(portfolioHighYieldDto);

                    PortfolioLowYieldDto portfolioLowYieldDto = PortfolioLowYieldDto.builder()
                            .portId(comparePortId)
                            .lowYield(lowYield)
                            .lowYieldDate(YearMonth.parse(months.get(monthYield.indexOf(lowYield))))
                            .build();
                    portfolioLowYieldDtos.add(portfolioLowYieldDto);
                }
            }
        }
        Collections.sort(portfolioRankDtos, (r1, r2) -> {
            double r1FinalYield = r1.getFinalYield();
            double r2FinalYield = r2.getFinalYield();
            if(r1FinalYield == r2FinalYield){
                return Double.compare(r1.getPortId(),r2.getPortId());
            }
            return Double.compare(r2FinalYield,r1FinalYield);
        });

        Collections.sort(portfolioHighYieldDtos, (h1, h2) -> {
            double h1HighYield = h1.getHighYield();
            double h2HighYield = h2.getHighYield();
            if(h1HighYield == h2HighYield){
                return Double.compare(h1.getPortId(),h2.getPortId());
            }
            return Double.compare(h2HighYield,h1HighYield);
        });

        Collections.sort(portfolioLowYieldDtos, (l1, l2) -> {
            double l1LowYield = l1.getLowYield();
            double l2LowYield = l2.getLowYield();
            if(l1LowYield == l2LowYield){
                return Double.compare(l1.getPortId(),l2.getPortId());
            }
            return Double.compare(l1LowYield,l2LowYield);
        });

        PortfolioCompareDto.Response portfolioCompareDto = PortfolioCompareDto.Response.builder()
                .portfolioRanks(portfolioRankDtos)
                .portfolioHighYield(portfolioHighYieldDtos.get(0))
                .portfolioLowYield(portfolioLowYieldDtos.get(0))
                .build();
        return portfolioCompareDto;

    }

    //포트폴리오 삭제
    @Transactional
    public HashMap<String, Long> deletePortfolio(Long portId, UserDetailsImpl userDetails) {
//        Portfolio portfolio = portfolioRepository.findById(portId).orElseThrow(
//                () -> new PortfolioNotFoundException(portId)
//        );

        Portfolio portfolio = PresentCheck.portfoliIsPresentCheck(portId, portfolioRepository);

        if(!portfolio.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new UserMatchException("Delete portfolio user matching error",
                    userDetails.getUser().getId(),
                    portfolio.getUser().getId());

        }


        ValueOperations<String, Object> vop = redisTemplate.opsForValue();

        HashMap<String, Long> responseId = new HashMap<>();
        responseId.put("portId", portfolio.getId());

        likesRepository.deleteAllByPortfolioId(portId);
        commentRepository.deleteAllByPortfolioId(portId);
        portStockRepository.deleteAllByPortfolioId(portId);
        portfolioRepository.delete(portfolio);

        if(vop.get("port"+portfolio.getId()) != null){
            vop.getOperations().delete("port"+portfolio.getId());
            log.info("redis delete : {}", portfolio.getId());
//            System.out.println("redis delete" + portfolio.getId());
        }

        return responseId;
    }

    //포트폴리오 자랑하기
    @Transactional
    public PortfolioMyBestDto.Response myBestPortfolio(PortfolioMyBestDto.Request portfolioMyBestRequestDto, UserDetailsImpl userDetails) {
//        Portfolio portfolio = portfolioRepository.findById(portfolioMyBestRequestDto.getPortId()).orElseThrow(
//                () -> new PortfolioNotFoundException(portfolioMyBestRequestDto.getPortId())
//        );
        Portfolio portfolio = PresentCheck.portfoliIsPresentCheck(portfolioMyBestRequestDto.getPortId(), portfolioRepository);

        if(!portfolio.getUser().getId().equals(userDetails.getUser().getId())){
            throw new UserMatchException("Boast portfolio user matching error",
                    userDetails.getUser().getId(),
                    portfolio.getUser().getId());
        }
        if(portfolioMyBestRequestDto.isMyBest()){
            portfolio.updateMyBest(true);
        } else {
            portfolio.updateMyBest(false);
        }
        PortfolioMyBestDto.Response portfolioMyBestResponseDto = PortfolioMyBestDto.Response.builder()
                .myBest(portfolio.getMyBest())
                .build();
        return portfolioMyBestResponseDto;
    }
}
