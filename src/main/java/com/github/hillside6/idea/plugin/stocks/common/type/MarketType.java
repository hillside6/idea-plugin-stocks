package com.github.hillside6.idea.plugin.stocks.common.type;

import lombok.Getter;

/**
 * 市场类型
 *
 * @author hillside6
 * @since 2021/01/15
 */
public enum MarketType {
    SH("上海"),
    SZ("深圳"),
    HK("香港"),
    US("美国");

    @Getter
    private final String marketName;

    MarketType(String marketName) {
        this.marketName = marketName;
    }

    /**
     * 市场转换
     */
    public static MarketType parse(String marketName) {
        for (MarketType marketType : MarketType.values()) {
            if (marketType.marketName.equals(marketName)) {
                return marketType;
            }
        }
        return null;
    }
}