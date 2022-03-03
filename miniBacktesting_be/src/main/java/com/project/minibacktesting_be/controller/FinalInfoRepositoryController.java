package com.project.minibacktesting_be.controller;

import com.project.minibacktesting_be.model.FinalInfo;
import com.project.minibacktesting_be.model.StockInfo;
import com.project.minibacktesting_be.repository.FinalInfoRepository;
import com.project.minibacktesting_be.repository.StockInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FinalInfoRepositoryController {
    private final FinalInfoRepository finalInfoRepository;
    private final StockInfoRepository stockInfoRepository;

    @GetMapping("/finalInfo")
    public List<FinalInfo> GetFinalInfo(){
        return finalInfoRepository.findAll();
    }

//    @GetMapping("/finaltoStock")
//    public List<StockInfo> TransferFinalInfoToStockInfo(){
//        List<FinalInfo> finalInfoList =  finalInfoRepository.findAll();
//        return List<StockInfo> stockInfoList = new ArrayList<>();
//    }
}
