package com.github.hillside6.idea.plugin.stocks.ui.config;

import com.github.hillside6.idea.plugin.stocks.common.type.MarketType;
import com.github.hillside6.idea.plugin.stocks.dao.model.Stock;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Data;

/**
 * 股票
 *
 * @author hillside6
 * @since 2021/01/15
 */
@Data
public class ViewStock implements Serializable {
    /**
     * 用于排序的id，从1开始
     */
    private Integer id;
    /**
     * 代码, 如 600000_SH
     */
    private String stockMarketCode;
    /**
     * 代码
     */
    private String stockCode;
    /**
     * 市场
     */
    private MarketType marketType;
    /**
     * 名称
     */
    private String stockName;
    /**
     * 最后报价
     */
    private BigDecimal lastPrice;
    /**
     * 涨跌
     */
    private BigDecimal change;
    /**
     * 涨跌幅
     */
    private BigDecimal changePercent;
    /**
     * 昨收价格
     */
    private BigDecimal preClosePrice;
    /**
     * 持仓数量
     */
    private Integer holdCount;
    /**
     * 成本价
     */
    private BigDecimal holdPrice;
    /**
     * 今日持仓盈亏
     */
    private BigDecimal todayHoldProfit;
    /**
     * 持仓盈亏
     */
    private BigDecimal holdProfit;

    public ViewStock(Stock stock) {
        this.stockMarketCode = stock.getStockMarketCode();
        this.stockCode = stock.getStockCode();
        this.marketType = stock.getMarketType();
        this.holdCount = stock.getHoldCount();
        this.holdPrice = stock.getHoldPrice();
    }

    /**
     * 计算涨跌和涨跌幅
     */
    public void calculate(String stockName, BigDecimal lastPrice, BigDecimal preClosePrice) {
        this.stockName = stockName;
        this.lastPrice = lastPrice;
        this.preClosePrice = preClosePrice;
        if (lastPrice == null || lastPrice.compareTo(BigDecimal.ZERO) == 0
            || preClosePrice == null || preClosePrice.compareTo(BigDecimal.ZERO) == 0) {
            change = null;
            changePercent = null;
            todayHoldProfit = null;
            holdProfit = null;
        } else {
            //计算涨跌和涨跌幅
            change = lastPrice.subtract(preClosePrice).setScale(10, RoundingMode.HALF_UP);
            changePercent = change.multiply(BigDecimal.valueOf(100)).divide(preClosePrice, 2, RoundingMode.HALF_UP);
            //计算今日盈亏和盈亏
            if (holdCount != null && holdCount > 0 && holdPrice != null) {
                todayHoldProfit = lastPrice.subtract(preClosePrice).multiply(BigDecimal.valueOf(holdCount))
                    .setScale(2, RoundingMode.HALF_UP);
                holdProfit = lastPrice.subtract(holdPrice).multiply(BigDecimal.valueOf(holdCount))
                    .setScale(2, RoundingMode.HALF_UP);
            }
        }
    }
}
