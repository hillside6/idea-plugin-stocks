package com.github.hillside6.idea.plugin.stocks.provider;

import com.github.hillside6.idea.plugin.stocks.common.type.QuoteProviderType;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 行情提供者选择
 *
 * @author hillside6
 * @since 2021/01/15
 */
public final class ProviderManager {
    private ProviderManager() {
    }

    private static final Map<QuoteProviderType, Provider> providerMap =
        Stream.of(new TencentProvider())
            .collect(Collectors.toMap(Provider::providerType, o -> o));

    /**
     * 获取行情提供者
     */
    public static Provider getQuoteProvider(QuoteProviderType providerType) {
        return providerMap.get(providerType);
    }
}
