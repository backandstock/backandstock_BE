package com.project.minibacktesting_be.backtesting;

import com.project.minibacktesting_be.model.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class StockYieldCal {

    public List<Double> getStockYieldList(List<Stock> targetStocks,
                                          Long seedMoney,
                                          String option) {

        Double targetNum = seedMoney.doubleValue() / targetStocks.get(0).getClose();
        // 코스피 수익금 계산
        if (option.equals("yieldMoney")) {
            return targetStocks.
                    stream().
                    map(Stock::getClose).
                    map(s -> s * targetNum).
                    collect(Collectors.toList());

        } else if(option.equals("yieldPct")) {
            List<Double> yieldPctList = targetStocks.
                    stream().
                    map(Stock :: getYieldPct).
                    map(s -> s*100).
                    collect(Collectors.toList());
            // 첫달 수익률은 0
            yieldPctList.set(0,0.0);

            return yieldPctList;

        }else{
            return null;
        }

    }


}
