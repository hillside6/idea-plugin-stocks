package com.github.hillside6.idea.plugin.stocks.provider;

import com.github.hillside6.idea.plugin.stocks.common.MarketType;

import java.math.BigDecimal;

/**
 * 请求行情结果
 *
 * @author zhangwenqiang
 * @since 2024/01/16
 */
public class ProviderResult {
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
    private String name;
    /**
     * 最后报价
     */
    private BigDecimal lastPrice;
    /**
     * 昨收价格
     */
    private BigDecimal previousClosePrice;

    public ProviderResult(String code, MarketType marketType, String name
            , BigDecimal lastPrice, BigDecimal previousClosePrice) {
        this.code = code;
        this.marketType = marketType;
        this.name = name;
        this.lastPrice = lastPrice;
        this.previousClosePrice = previousClosePrice;
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
}