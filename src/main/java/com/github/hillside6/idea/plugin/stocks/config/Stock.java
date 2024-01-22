package com.github.hillside6.idea.plugin.stocks.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.hillside6.idea.plugin.stocks.common.MarketType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 股票
 *
 * @author hillside6
 * @since 2021/01/15
 */
public class Stock implements Serializable {
    /**
     * 代码
     */
    private String code;
    /**
     * 市场
     */
    private MarketType marketType;
    /**
     * 名称
     */
    @JsonIgnore
    private String name;
    /**
     * 最后报价
     */
    @JsonIgnore
    private BigDecimal lastPrice;
    /**
     * 昨收价格
     */
    @JsonIgnore
    private BigDecimal previousClosePrice;
    /**
     * 涨跌
     */
    @JsonIgnore
    private BigDecimal change;
    /**
     * 涨跌幅
     */
    @JsonIgnore
    private BigDecimal changePercent;

    public Stock() {
    }

    public Stock(String code, MarketType marketType, String name
            , BigDecimal lastPrice, BigDecimal previousClosePrice) {
        this.code = code;
        this.marketType = marketType;
        this.name = name;
        this.lastPrice = lastPrice;
        this.previousClosePrice = previousClosePrice;
        //计算涨跌和涨跌幅
        if (lastPrice == null || lastPrice.compareTo(BigDecimal.ZERO) == 0 || previousClosePrice.compareTo(BigDecimal.ZERO) == 0) {
            change = BigDecimal.ZERO;
            changePercent = BigDecimal.ZERO;
        } else {
            change = lastPrice.subtract(previousClosePrice).setScale(10, RoundingMode.HALF_UP);
            changePercent = change.multiply(BigDecimal.valueOf(100)).divide(previousClosePrice, 10, RoundingMode.HALF_UP);
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MarketType getMarketType() {
        return marketType;
    }

    public void setMarketType(MarketType marketType) {
        this.marketType = marketType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public BigDecimal getPreviousClosePrice() {
        return previousClosePrice;
    }

    public void setPreviousClosePrice(BigDecimal previousClosePrice) {
        this.previousClosePrice = previousClosePrice;
    }

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    public BigDecimal getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(BigDecimal changePercent) {
        this.changePercent = changePercent;
    }
}
