package com.github.hillside6.idea.plugin.stock.quote;

import com.github.hillside6.idea.plugin.stock.common.QuoteProviderType;
import com.github.hillside6.idea.plugin.stock.common.StockItem;
import com.github.hillside6.idea.plugin.stock.common.util.HttpUtil;
import com.github.hillside6.idea.plugin.stock.common.MarketType;
import com.github.hillside6.idea.plugin.stock.config.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新浪行情提供
 *
 * @author hillside6
 * @since 2020/12/30
 */
public class SinaProvider implements Provider {
    private static final Logger logger = LoggerFactory.getLogger(SinaProvider.class);

    @Override
    public QuoteProviderType providerType() {
        return QuoteProviderType.SINA;
    }

    @Override
    public List<StockItem> load(List<Stock> stockList) {
        List<StockItem> stockItemList = new LinkedList<>();
        try {
            //请求参数转换
            String queryStr = stockList.stream().map(stock -> {
                if (stock.getMarketType() == MarketType.SZ) {
                    return "sz" + stock.getCode();
                } else {
                    return "sh" + stock.getCode();
                }
            }).collect(Collectors.joining(","));
            //请求
            String datas = HttpUtil.get("http://hq.sinajs.cn/list=" + queryStr);
            datas = datas.replace("var hq_str_sh", "").replace("var hq_str_sz", "").replace("=\"", ",");
            int order = 1;
            for (String data : datas.split("\n")) {
                String[] strings = data.split(",");
                String code = strings[0];
                String name = strings[1];
                BigDecimal previousClosePrice = new BigDecimal(strings[3]).setScale(10, RoundingMode.HALF_UP);
                BigDecimal lastPrice = new BigDecimal(strings[4]).setScale(10, RoundingMode.HALF_UP);

                StockItem stockItem = new StockItem(order++, code, name, lastPrice, previousClosePrice);
                stockItemList.add(stockItem);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("加载新浪行情异常", e);
        } catch (Exception e) {
            logger.error("加载新浪行情异常", e);
        }
        return stockItemList;
    }
}
