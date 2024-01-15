package com.github.hillside6.idea.plugin.stock.quote;

import com.github.hillside6.idea.plugin.stock.common.QuoteProviderType;
import com.github.hillside6.idea.plugin.stock.config.Config;

import java.util.ArrayList;
import java.util.List;
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
            Stream.of(new SinaProvider(), new TencentProvider())
                    .collect(Collectors.toMap(Provider::providerType, o -> o));

    /**
     * 获取行情提供者
     */
    public static Provider getQuoteProvider(Config config) {
        return providerMap.get(config.getProviderType());
    }

    /**
     * 获取行情列表
     */
    public static List<QuoteProviderType> getProviderList() {
        return new ArrayList<>(providerMap.keySet());
    }
}
