package com.github.hillside6.idea.plugin.stock.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.hillside6.idea.plugin.stock.common.QuoteProviderType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置
 *
 * @author hillside6
 * @since 2021/01/15
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config implements Serializable {
    /**
     * 简单版或复杂版
     */
    private Boolean simple = true;
    /**
     * 配置的行情数据源
     */
    private QuoteProviderType providerType = QuoteProviderType.TENCENT;
    /**
     * 存储的股票
     */
    private List<Stock> stockList = new ArrayList<>();

    public Boolean getSimple() {
        return simple;
    }

    public void setSimple(Boolean simple) {
        this.simple = simple;
    }

    public QuoteProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(QuoteProviderType providerType) {
        this.providerType = providerType;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }
}
