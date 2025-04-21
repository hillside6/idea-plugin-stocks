package com.github.hillside6.idea.plugin.stocks.provider;

import java.math.BigDecimal;
import lombok.Data;

/**
 * 请求行情结果
 *
 * @author hillside6
 * @since 2024/01/16
 */
@Data
public class ProviderResult {
    /**
     * 代码
     */
    private String stockMarketCode;
    /**
     * 名称
     */
    private String stockName;
    /**
     * 最后报价
     */
    private BigDecimal lastPrice;
    /**
     * 昨收价格
     */
    private BigDecimal preClosePrice;

    public ProviderResult(String stockMarketCode, String stockName, BigDecimal lastPrice, BigDecimal preClosePrice) {
        this.stockMarketCode = stockMarketCode;
        this.stockName = stockName;
        this.lastPrice = lastPrice;
        this.preClosePrice = preClosePrice;
    }
}