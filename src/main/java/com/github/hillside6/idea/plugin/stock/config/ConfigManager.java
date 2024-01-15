package com.github.hillside6.idea.plugin.stock.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hillside6
 * @since 2021/01/15
 */
public final class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    private ConfigManager() {
    }

    /**
     * 加载配置
     */
    public static Config loadConfig() {
        try {
            String userHome = System.getProperties().get("user.home").toString();
            File file = new File(userHome + "/.IdeaPlugin/stock.config");
            if (!file.getParentFile().exists()) {
                Files.createDirectories(file.getParentFile().toPath());
            }
            if (!file.exists()) {
                Config config = new Config();
                ObjectMapper objectMapper = new ObjectMapper();
                Files.write(file.toPath(), objectMapper.writeValueAsBytes(config));
                return config;
            }
            try (Stream<String> lines = Files.lines(file.toPath())) {
                String configJsonStr = lines.collect(Collectors.joining("\n"));
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(configJsonStr, Config.class);
            }
        } catch (Exception e) {
            logger.error("加载配置异常", e);
            return new Config();
        }
    }

    /**
     * 更新配置
     */
    public static void updateConfig(Config config) {
        try {
            String userHome = System.getProperties().get("user.home").toString();
            File file = new File(userHome + "/.IdeaPlugin/stock.config");
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
    public static void addStock(Stock stock) {
        Config config = loadConfig();
        //删除重复的股票
        config.getStockList().removeIf(s -> stock.getCode().equalsIgnoreCase(s.getCode()));
        //将新加的股票放到前面
        List<Stock> stockList = new ArrayList<>();
        stockList.add(stock);
        stockList.addAll(config.getStockList());
        config.setStockList(stockList);
        updateConfig(config);
    }

    /**
     * 删除股票
     */
    public static void deleteStock(List<String> stockCodeList) {
        Config config = loadConfig();
        if (config == null) {
            config = new Config();
        }
        //删除重复的股票
        config.getStockList().removeIf(stock -> stockCodeList.contains(stock.getCode()));
        updateConfig(config);
    }

    /**
     * 上移股票
     */
    public static void upStock(String stockCode) {
        Config config = loadConfig();
        if (config == null) {
            config = new Config();
        }
        List<Stock> stockList = config.getStockList();
        int preIndex = -1;
        for (int i = 0; i < stockList.size(); i++) {
            if (stockCode.equals(stockList.get(i).getCode())) {
                preIndex = i;
                break;
            }
        }
        if (preIndex >= 1) {
            Stock preStock = stockList.get(preIndex);
            stockList.set(preIndex, stockList.get(preIndex - 1));
            stockList.set(preIndex - 1, preStock);
        }
        config.setStockList(stockList);
        updateConfig(config);
    }

    /**
     * 下移股票
     */
    public static void downStock(String stockCode) {
        Config config = loadConfig();
        if (config == null) {
            config = new Config();
        }
        List<Stock> stockList = config.getStockList();
        int preIndex = -1;
        for (int i = 0; i < stockList.size(); i++) {
            if (stockCode.equals(stockList.get(i).getCode())) {
                preIndex = i;
                break;
            }
        }
        if (preIndex >= 0 && preIndex < stockList.size() - 1) {
            Stock preStock = stockList.get(preIndex);
            stockList.set(preIndex, stockList.get(preIndex + 1));
            stockList.set(preIndex + 1, preStock);
        }
        config.setStockList(stockList);
        updateConfig(config);
    }
}
