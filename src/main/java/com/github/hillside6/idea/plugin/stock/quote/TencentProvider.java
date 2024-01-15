package com.github.hillside6.idea.plugin.stock.quote;

import com.github.hillside6.idea.plugin.stock.common.MarketType;
import com.github.hillside6.idea.plugin.stock.common.QuoteProviderType;
import com.github.hillside6.idea.plugin.stock.common.StockItem;
import com.github.hillside6.idea.plugin.stock.common.util.HttpUtil;
import com.github.hillside6.idea.plugin.stock.config.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 腾讯行情提供
 *
 * @author hillside6
 * @since 2021/01/15
 */
public class TencentProvider implements Provider {
    private static final Logger logger = LoggerFactory.getLogger(TencentProvider.class);

    @Override
    public QuoteProviderType providerType() {
        return QuoteProviderType.TENCENT;
    }

    @Override
    public List<StockItem> load(List<Stock> stockList) {
        List<StockItem> stockItemList = new LinkedList<>();
        try {
            //请求参数转换
            SecureRandom random = new SecureRandom();
            String queryStr = "r=" + random.nextDouble() + "q=" + stockList.stream().map(stock -> {
                if (stock.getMarketType() == MarketType.SZ) {
                    return "s_sz" + stock.getCode();
                } else {
                    return "s_sh" + stock.getCode();
                }
            }).collect(Collectors.joining(","));
            //请求
            String datas = HttpUtil.get("http://qt.gtimg.cn/" + queryStr);
            int order = 1;
            for (String data : datas.split(";")) {
                String[] strings = data.split("~");
                String code = strings[2];
                String name = strings[1];
                BigDecimal lastPrice = new BigDecimal(strings[3]).setScale(10, RoundingMode.HALF_UP);
                BigDecimal changePrice = new BigDecimal(strings[4]).setScale(10, RoundingMode.HALF_UP);
                BigDecimal previousClosePrice = lastPrice.subtract(changePrice);
                StockItem stockItem = new StockItem(order++, code, name, lastPrice, previousClosePrice);
                stockItemList.add(stockItem);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("加载腾讯行情异常", e);
        } catch (Exception e) {
            logger.error("加载腾讯行情异常", e);
        }
        return stockItemList;
    }
}
