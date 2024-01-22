package com.github.hillside6.idea.plugin.stocks.provider;

import com.github.hillside6.idea.plugin.stocks.common.QuoteProviderType;
import com.github.hillside6.idea.plugin.stocks.config.Stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 行情提供者
 *
 * @author hillside6
 * @since 2021/01/15
 */
public abstract class Provider {
    /**
     * 提供者类型
     */
    public abstract QuoteProviderType providerType();

    /**
     * 加载行情
     *
     * @param stockList 储存的股票列表
     * @return 结果
     */
    protected abstract List<ProviderResult> loadInner(List<Stock> stockList);

    /**
     * 加载行情
     *
     * @param stockList 储存的股票列表
     */
    public void load(List<Stock> stockList) {
        List<ProviderResult> resultList = loadInner(stockList);
        Map<String, ProviderResult> resultMap = resultList.stream().collect(Collectors
                .toMap(result -> result.getCode() + result.getMarketType(), result -> result));

        for (Stock stock : stockList) {
            stock.setName("");
            stock.setLastPrice(BigDecimal.ZERO);
            stock.setPreviousClosePrice(BigDecimal.ZERO);
            stock.setChange(BigDecimal.ZERO);
            stock.setChangePercent(BigDecimal.ZERO);
            ProviderResult result = resultMap.get(stock.getCode() + stock.getMarketType());
            if (result != null) {
                stock.setName(result.getName());
                stock.setLastPrice(result.getLastPrice());
                stock.setPreviousClosePrice(result.getPreviousClosePrice());
                //计算涨跌和涨跌幅
                if (result.getLastPrice() != null && result.getLastPrice().compareTo(BigDecimal.ZERO) != 0
                        && result.getPreviousClosePrice().compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal change = result.getLastPrice().subtract(result.getPreviousClosePrice())
                            .setScale(10, RoundingMode.HALF_UP);
                    stock.setChange(change);
                    stock.setChangePercent(change.multiply(BigDecimal.valueOf(100))
                            .divide(result.getPreviousClosePrice(), 10, RoundingMode.HALF_UP));
                }
            }
        }
    }
}
