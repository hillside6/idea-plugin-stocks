package com.github.hillside6.idea.plugin.stock.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author hillside6
 * @since 2021/01/15
 */
public class StockItem {
    /**
     * 顺序
     */
    private int order;
    /**
     * 代码
     */
    private String code;
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
    /**
     * 涨跌
     */
    private BigDecimal change;
    /**
     * 涨跌幅
     */
    private BigDecimal changePercent;

    public StockItem(int order, String code, String name, BigDecimal lastPrice, BigDecimal previousClosePrice) {
        this.order = order;
        this.code = code;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
