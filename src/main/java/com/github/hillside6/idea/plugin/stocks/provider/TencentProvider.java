package com.github.hillside6.idea.plugin.stocks.provider;

import com.github.hillside6.idea.plugin.stocks.common.MarketType;
import com.github.hillside6.idea.plugin.stocks.common.QuoteProviderType;
import com.github.hillside6.idea.plugin.stocks.common.util.HttpUtil;
import com.github.hillside6.idea.plugin.stocks.config.Stock;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 腾讯行情提供
 * https://qt.gtimg.cn/r=1231.12q=s_sz300059
 * v_s_sz300059="51~东方财富~300059~12.85~0.02~0.16~422717~54298~~2037.62~GP-A-CYB";
 *
 * @author hillside6
 * @since 2021/01/15
 */
public class TencentProvider extends Provider {
    private static final Logger logger = LoggerFactory.getLogger(TencentProvider.class);

    @Override
    public QuoteProviderType providerType() {
        return QuoteProviderType.TENCENT;
    }

    @Override
    public List<ProviderResult> loadInner(List<Stock> stockList) {
        List<ProviderResult> resultList = new LinkedList<>();
        try {
            //请求参数转换
            SecureRandom random = new SecureRandom();
            String queryStr = "r=" + random.nextDouble() + "q=" + stockList.stream().map(
                    stock -> switch (stock.getMarketType()) {
                        case SH -> "s_sh" + stock.getCode();
                        case SZ -> "s_sz" + stock.getCode();
                        case HK -> "s_hk" + stock.getCode();
                        case US -> "s_us" + stock.getCode();
                    }).collect(Collectors.joining(","));
            //请求
            String datas = HttpUtil.get("https://qt.gtimg.cn/" + queryStr);
            for (String data : datas.split(";")) {
                String[] strings = data.split("~");
                if (strings.length < 5) {
                    continue;
                }
                String code = strings[2];
                String name = strings[1];
                MarketType marketType;
                if (data.contains("sz")) {
                    marketType = MarketType.SZ;
                } else if (data.contains("sh")) {
                    marketType = MarketType.SH;
                } else if (data.contains("hk")) {
                    marketType = MarketType.HK;
                } else if (data.contains("us")) {
                    marketType = MarketType.US;
                    code = code.split("\\.")[0];
                } else {
                    continue;
                }
                BigDecimal lastPrice = new BigDecimal(strings[3]).setScale(10, RoundingMode.HALF_UP);
                BigDecimal changePrice = new BigDecimal(strings[4]).setScale(10, RoundingMode.HALF_UP);
                BigDecimal previousClosePrice = lastPrice.subtract(changePrice);
                ProviderResult result = new ProviderResult(code, marketType, name, lastPrice, previousClosePrice);
                resultList.add(result);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("加载腾讯行情异常", e);
        } catch (Exception e) {
            logger.error("加载腾讯行情异常", e);
        }
        return resultList;
    }
}
