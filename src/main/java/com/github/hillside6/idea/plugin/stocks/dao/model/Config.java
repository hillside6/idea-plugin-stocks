package com.github.hillside6.idea.plugin.stocks.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.hillside6.idea.plugin.stocks.common.type.QuoteProviderType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * 配置
 *
 * @author hillside6
 * @since 2021/01/15
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Config implements Serializable {
    /**
     * 显示昨收价格
     */
    private Boolean showPreClosePrice = true;
    /**
     * 显示涨跌
     */
    private Boolean showChange = true;
    /**
     * 显示持仓数量
     */
    private Boolean showHoldCount = true;
    /**
     * 显示持仓价格
     */
    private Boolean showHoldPrice = true;
    /**
     * 显示今日持仓盈亏
     */
    private Boolean showTodayHoldProfit = true;
    /**
     * 显示持仓盈亏
     */
    private Boolean showHoldProfit = true;
    /**
     * 显示今日持仓总盈亏
     */
    private Boolean showTodayTotalHoldProfit = true;
    /**
     * 显示持仓总盈亏
     */
    private Boolean showTotalHoldProfit = true;
    /**
     * 高亮显示持仓股
     */
    private Boolean showHoldHighLight = true;
    /**
     * 配置的行情数据源
     */
    private QuoteProviderType providerType = QuoteProviderType.TENCENT;
    /**
     * 存储的股票
     */
    private List<Stock> stockList = new ArrayList<>();
}
