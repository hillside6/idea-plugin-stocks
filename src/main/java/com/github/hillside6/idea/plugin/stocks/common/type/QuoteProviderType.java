package com.github.hillside6.idea.plugin.stocks.common.type;

import lombok.Getter;

/**
 * 行情提供者类型
 *
 * @author hillside6
 * @since 2024/01/12
 */
@Getter
public enum QuoteProviderType {
    TENCENT("腾讯"),
    ;
    private final String desc;

    QuoteProviderType(String desc) {
        this.desc = desc;
    }

    /**
     * 行情提供者转换
     */
    public static QuoteProviderType parse(String desc) {
        for (QuoteProviderType providerType : values()) {
            if (providerType.getDesc().equals(desc)) {
                return providerType;
            }
        }
        return null;
    }
}
