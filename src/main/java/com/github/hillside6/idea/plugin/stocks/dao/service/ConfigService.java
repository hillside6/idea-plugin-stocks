package com.github.hillside6.idea.plugin.stocks.dao.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hillside6.idea.plugin.stocks.common.type.MarketType;
import com.github.hillside6.idea.plugin.stocks.dao.model.Config;
import com.github.hillside6.idea.plugin.stocks.dao.model.Stock;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hillside6
 * @since 2021/01/15
 */
public final class ConfigService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);
    private static Config config;

    private ConfigService() {
    }

    /**
     * 获取配置
     */
    public static Config getConfig() {
        if (config == null) {
            try {
                String userHome = System.getProperties().get("user.home").toString();
                File file = new File(userHome + "/.IdeaPlugin/stocks.config");
                if (!file.getParentFile().exists()) {
                    Files.createDirectories(file.getParentFile().toPath());
                }
                if (!file.exists()) {
                    config = new Config();
                    ObjectMapper objectMapper = new ObjectMapper();
                    Files.write(file.toPath(), objectMapper.writeValueAsBytes(config));
                }
                try (Stream<String> lines = Files.lines(file.toPath())) {
                    String configJsonStr = lines.collect(Collectors.joining("\n"));
                    ObjectMapper objectMapper = new ObjectMapper();
                    config = objectMapper.readValue(configJsonStr, Config.class);
                }
            } catch (Exception e) {
                logger.error("加载配置异常", e);
                config = new Config();
            }
        }
        return config;
    }

    /**
     * 保存配置
     */
    public static void saveConfig(Config config) {
        try {
            String userHome = System.getProperties().get("user.home").toString();
            File file = new File(userHome + "/.IdeaPlugin/stocks.config");
            if (!file.getParentFile().exists()) {
                Files.createDirectories(file.getParentFile().toPath());
            }
            ObjectMapper objectMapper = new ObjectMapper();
            Files.write(file.toPath(), objectMapper.writeValueAsBytes(config));
        } catch (Exception e) {
            logger.error("更新配置异常", e);
        }
    }

    /**
     * 添加股票
     */
    public static void addStock(String stockCode, MarketType marketType, Integer holdCount, BigDecimal holdPrice) {
        Stock stock = new Stock(stockCode, marketType, holdCount, holdPrice);
        //如果存在之前的，修改，否则新增到最前
        Stock oldStock = config.getStockList().stream()
            .filter(s -> s.getStockMarketCode().equalsIgnoreCase(stock.getStockMarketCode()))
            .findFirst().orElse(null);
        if (oldStock != null) {
            oldStock.setHoldCount(stock.getHoldCount());
            oldStock.setHoldPrice(stock.getHoldPrice());
        } else {
            config.getStockList().addFirst(stock);
        }
        saveConfig(config);
    }

    /**
     * 删除股票
     */
    public static void deleteStock(List<String> stockMarketCodeList) {
        //删除重复的股票
        config.getStockList().removeIf(stock -> stockMarketCodeList.contains(stock.getStockMarketCode()));
        saveConfig(config);
    }

    /**
     * 上移股票
     */
    public static void upStock(List<String> stockMarketCodeList) {
        List<Stock> stockList = config.getStockList();
        List<Stock> result = move(stockList, stockMarketCodeList);
        config.setStockList(result);
        saveConfig(config);
    }

    /**
     * 下移股票
     */
    public static void downStock(List<String> stockMarketCodeList) {
        List<Stock> stockList = config.getStockList();
        List<Stock> result = move(stockList.reversed(), stockMarketCodeList.reversed());
        config.setStockList(result.reversed());
        saveConfig(config);
    }

    /**
     * 移动股票顺序
     */
    private static List<Stock> move(List<Stock> stockList, List<String> stockMarketCodeList) {
        List<Stock> result = new ArrayList<>();
        //占位
        result.add(new Stock());
        for (Stock stock : stockList) {
            boolean found = false;
            for (String stockMarketCode : stockMarketCodeList) {
                if (stock.getStockMarketCode().equalsIgnoreCase(stockMarketCode)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                int index = result.size() - 1;
                result.add(index, stock);
            } else {
                result.add(stock);
            }
        }
        result.removeIf(stock -> stock.getStockMarketCode() == null);
        return result;
    }
}
