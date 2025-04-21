package com.github.hillside6.idea.plugin.stocks.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.hillside6.idea.plugin.stocks.common.type.MarketType;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 存储的股票
 *
 * @author hillside6
 * @since 2025/4/14
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class Stock implements Serializable {
    public static final String SPLIT_SYMBOL = "_";
    /**
     * 代码, 如 600000_SH
     */
    private String stockMarketCode;
    /**
     * 持仓数量
     */
    private Integer holdCount;
    /**
     * 持仓价格
     */
    private BigDecimal holdPrice;

    public Stock(String stockCode, MarketType marketType, Integer holdCount, BigDecimal holdPrice) {
        this.stockMarketCode = stockCode + SPLIT_SYMBOL + marketType;
        this.holdCount = holdCount;
        this.holdPrice = holdPrice;
    }

    /**
     * 股票代码
     */
    @JsonIgnore
    public String getStockCode() {
        return stockMarketCode.split(SPLIT_SYMBOL)[0];
    }

    /**
     * 股票市场类型
     */
    @JsonIgnore
    public MarketType getMarketType() {
        return MarketType.valueOf(stockMarketCode.split(SPLIT_SYMBOL)[1]);
    }
}