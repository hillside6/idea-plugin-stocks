package com.github.hillside6.idea.plugin.stock.quote;

import com.github.hillside6.idea.plugin.stock.common.QuoteProviderType;
import com.github.hillside6.idea.plugin.stock.common.StockItem;
import com.github.hillside6.idea.plugin.stock.config.Stock;

import java.util.List;

/**
 * 行情提供者
 *
 * @author hillside6
 * @since 2021/01/15
 */
public interface Provider {
    /**
     * 提供者类型
     */
    QuoteProviderType providerType();

    /**
     * 加载行情
     *
     * @param stockList 储存的股票列表
     * @return 需要显示的行情
     */
    List<StockItem> load(List<Stock> stockList);
}
