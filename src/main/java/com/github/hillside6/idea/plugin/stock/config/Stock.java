package com.github.hillside6.idea.plugin.stock.config;

import com.github.hillside6.idea.plugin.stock.common.MarketType;

import java.io.Serializable;

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

    public Stock() {
    }

    public Stock(String code, MarketType marketType) {
        this.code = code;
        this.marketType = marketType;
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
}
