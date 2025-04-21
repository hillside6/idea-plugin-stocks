package com.github.hillside6.idea.plugin.stocks.provider;

import com.github.hillside6.idea.plugin.stocks.common.type.QuoteProviderType;
import com.github.hillside6.idea.plugin.stocks.dao.model.Stock;
import com.github.hillside6.idea.plugin.stocks.ui.config.ViewStock;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 行情提供者
 *
 * @author hillside6
 * @since 2021/01/15
 */
public abstract class Provider {
    /**
     * 提供者类型
     */
    public abstract QuoteProviderType providerType();

    /**
     * 加载行情
     *
     * @param stockList 储存的股票列表
     * @return 结果
     */
    protected abstract List<ProviderResult> loadInner(List<Stock> stockList);

    /**
     * 加载行情
     */
    public List<ViewStock> load(List<Stock> stockList) {
        List<ProviderResult> resultList = loadInner(stockList);
        Map<String, ProviderResult> resultMap = resultList.stream().collect(Collectors
            .toMap(ProviderResult::getStockMarketCode, Function.identity()));

        List<ViewStock> viewStockList = stockList.stream().map(ViewStock::new).toList();
        for (int i = 0; i < viewStockList.size(); i++) {
            viewStockList.get(i).setId(i + 1);
        }

        for (ViewStock viewStock : viewStockList) {
            ProviderResult result = resultMap.get(viewStock.getStockMarketCode());
            if (result != null) {
                viewStock.calculate(result.getStockName(), result.getLastPrice(), result.getPreClosePrice());
            }
        }
        return viewStockList;
    }
}
