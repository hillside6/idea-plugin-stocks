package com.github.hillside6.idea.plugin.stock.common;

/**
 * 行情提供者类型
 *
 * @author hillside6
 * @since 2024/01/12
 */
public enum QuoteProviderType {
    TENCENT("腾讯"),
    SINA("新浪"),
    ;
    private final String desc;

    QuoteProviderType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static QuoteProviderType parse(String desc) {
        for (QuoteProviderType providerType : values()) {
            if (providerType.getDesc().equals(desc)) {
                return providerType;
            }
        }
        return null;
    }
}
