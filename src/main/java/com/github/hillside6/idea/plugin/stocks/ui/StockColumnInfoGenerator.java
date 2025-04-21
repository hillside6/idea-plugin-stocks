package com.github.hillside6.idea.plugin.stocks.ui;

import com.github.hillside6.idea.plugin.stocks.dao.model.Config;
import com.github.hillside6.idea.plugin.stocks.ui.config.ViewStock;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.ColumnInfo;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.jetbrains.annotations.NotNull;

/**
 * 显示列生成
 *
 * @author hillside6
 * @since 2021/01/15
 */
public class StockColumnInfoGenerator {
    private StockColumnInfoGenerator() {
    }

    /**
     * 代码
     */
    private static final ColumnInfo<ViewStock, String> STOCK_CODE_COLUMN_INFO = new ColumnInfo<>("代码") {
        @Override
        public String valueOf(ViewStock viewStock) {
            return toStr(viewStock.getStockCode());
        }

        @Override
        public @NotNull Comparator<ViewStock> getComparator() {
            return Comparator.comparing(ViewStock::getId);
        }

        @Override
        public TableCellRenderer getRenderer(ViewStock viewStock) {
            return generateHoldTableCellRenderer(viewStock.getHoldCount());
        }
    };
    /**
     * 名称
     */
    private static final ColumnInfo<ViewStock, String> STOCK_NAME_COLUMN_INFO = new ColumnInfo<>("名称") {
        @Override
        public String valueOf(ViewStock viewStock) {
            return toStr(viewStock.getStockName());
        }

        @Override
        public @NotNull Comparator<ViewStock> getComparator() {
            return Comparator.comparing(ViewStock::getStockName);
        }

        @Override
        public TableCellRenderer getRenderer(ViewStock viewStock) {
            return generateHoldTableCellRenderer(viewStock.getHoldCount());
        }
    };
    /**
     * 最新
     */
    private static final ColumnInfo<ViewStock, String> LAST_PRICE_COLUMN_INFO = new ColumnInfo<>("最新") {
        @Override
        public String valueOf(ViewStock viewStock) {
            return convertPrice(viewStock.getStockName(), viewStock.getLastPrice());
        }

        @Override
        public @NotNull Comparator<ViewStock> getComparator() {
            return Comparator.comparing(ViewStock::getLastPrice, bigDecimalComparator());
        }

        @Override
        public TableCellRenderer getRenderer(ViewStock viewStock) {
            return generateTableCellRenderer(viewStock.getChangePercent());
        }
    };
    /**
     * 涨跌
     */
    private static final ColumnInfo<ViewStock, String> CHANGE_COLUMN_INFO = new ColumnInfo<>("涨跌") {
        @Override
        public String valueOf(ViewStock viewStock) {
            return convertPrice(viewStock.getStockName(), viewStock.getChange());
        }

        @Override
        public @NotNull Comparator<ViewStock> getComparator() {
            return Comparator.comparing(ViewStock::getChange, bigDecimalComparator());
        }

        @Override
        public TableCellRenderer getRenderer(ViewStock viewStock) {
            return generateTableCellRenderer(viewStock.getChangePercent());
        }
    };
    /**
     * 涨幅
     */
    private static final ColumnInfo<ViewStock, String> CHANGE_PERCENT_COLUMN_INFO = new ColumnInfo<>("涨幅") {
        @Override
        public String valueOf(ViewStock viewStock) {
            if (viewStock.getChangePercent() == null) {
                return "";
            }
            return viewStock.getChangePercent().setScale(2, RoundingMode.HALF_UP) + "%";
        }

        @Override
        public @NotNull Comparator<ViewStock> getComparator() {
            return Comparator.comparing(ViewStock::getChangePercent, bigDecimalComparator());
        }

        @Override
        public TableCellRenderer getRenderer(ViewStock viewStock) {
            return generateTableCellRenderer(viewStock.getChangePercent());
        }
    };
    /**
     * 昨收
     */
    private static final ColumnInfo<ViewStock, String> PRE_CLOSE_PRICE_COLUMN_INFO = new ColumnInfo<>("昨收") {
        @Override
        public String valueOf(ViewStock viewStock) {
            return convertPrice(viewStock.getStockName(), viewStock.getPreClosePrice());
        }

        @Override
        public @NotNull Comparator<ViewStock> getComparator() {
            return Comparator.comparing(ViewStock::getPreClosePrice, bigDecimalComparator());
        }
    };
    /**
     * 持仓数量
     */
    private static final ColumnInfo<ViewStock, String> HOLD_COUNT_COLUMN_INFO = new ColumnInfo<>("持仓数量") {
        @Override
        public String valueOf(ViewStock viewStock) {
            if (viewStock.getHoldCount() == null || viewStock.getHoldCount() <= 0) {
                return "";
            }
            return viewStock.getHoldCount().toString();
        }
    };
    /**
     * 持仓价格
     */
    private static final ColumnInfo<ViewStock, String> HOLD_PRICE_COLUMN_INFO = new ColumnInfo<>("成本价") {
        @Override
        public String valueOf(ViewStock viewStock) {
            return convertPrice(viewStock.getStockName(), viewStock.getHoldPrice());
        }
    };
    /**
     * 今日持仓盈亏
     */
    private static final ColumnInfo<ViewStock, String> TODAY_HOLD_PROFIT_COLUMN_INFO = new ColumnInfo<>("今日盈亏") {
        @Override
        public String valueOf(ViewStock viewStock) {
            if (viewStock.getTodayHoldProfit() == null) {
                return "";
            }
            return viewStock.getTodayHoldProfit().setScale(2, RoundingMode.HALF_UP).toString();
        }

        @Override
        public @NotNull Comparator<ViewStock> getComparator() {
            return Comparator.comparing(ViewStock::getTodayHoldProfit, bigDecimalComparator());
        }

        @Override
        public TableCellRenderer getRenderer(ViewStock viewStock) {
            return generateTableCellRenderer(viewStock.getTodayHoldProfit());
        }
    };
    /**
     * 持仓盈亏
     */
    private static final ColumnInfo<ViewStock, String> HOLD_PROFIT_COLUMN_INFO = new ColumnInfo<>("持仓盈亏") {
        @Override
        public String valueOf(ViewStock viewStock) {
            if (viewStock.getHoldProfit() == null) {
                return "";
            }
            return viewStock.getHoldProfit().setScale(2, RoundingMode.HALF_UP).toString();
        }

        @Override
        public @NotNull Comparator<ViewStock> getComparator() {
            return Comparator.comparing(ViewStock::getHoldProfit, bigDecimalComparator());
        }

        @Override
        public TableCellRenderer getRenderer(ViewStock viewStock) {
            return generateTableCellRenderer(viewStock.getHoldProfit());
        }
    };

    /**
     * 生成显示的表格列
     */
    @SuppressWarnings("rawtypes")
    public static ColumnInfo[] generate(Config config) {
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        columnInfoList.add(STOCK_CODE_COLUMN_INFO);
        columnInfoList.add(STOCK_NAME_COLUMN_INFO);
        columnInfoList.add(LAST_PRICE_COLUMN_INFO);
        if (Boolean.TRUE.equals(config.getShowChange())) {
            columnInfoList.add(CHANGE_COLUMN_INFO);
        }
        columnInfoList.add(CHANGE_PERCENT_COLUMN_INFO);
        if (Boolean.TRUE.equals(config.getShowPreClosePrice())) {
            columnInfoList.add(PRE_CLOSE_PRICE_COLUMN_INFO);
        }
        if (Boolean.TRUE.equals(config.getShowHoldCount())) {
            columnInfoList.add(HOLD_COUNT_COLUMN_INFO);
        }
        if (Boolean.TRUE.equals(config.getShowHoldPrice())) {
            columnInfoList.add(HOLD_PRICE_COLUMN_INFO);
        }
        if (Boolean.TRUE.equals(config.getShowTodayHoldProfit())) {
            columnInfoList.add(TODAY_HOLD_PROFIT_COLUMN_INFO);
        }
        if (Boolean.TRUE.equals(config.getShowHoldProfit())) {
            columnInfoList.add(HOLD_PROFIT_COLUMN_INFO);
        }
        return columnInfoList.toArray(new ColumnInfo[0]);
    }

    /**
     * 获取单元格渲染
     */
    public static DefaultTableCellRenderer generateTableCellRenderer(BigDecimal compareValue) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super
                    .getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (compareValue == null || compareValue.compareTo(BigDecimal.ZERO) == 0) {
                    component.setForeground(JBColor.BLACK);
                } else if (BigDecimal.ZERO.compareTo(compareValue) < 0) {
                    component.setForeground(Color.decode("#dd0000"));
                } else if (BigDecimal.ZERO.compareTo(compareValue) > 0) {
                    component.setForeground(Color.decode("#009944"));
                }
                return component;
            }
        };
    }

    /**
     * 获取单元格渲染
     */
    public static DefaultTableCellRenderer generateHoldTableCellRenderer(Integer holdCount) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super
                    .getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (holdCount == null || holdCount <= 0) {
                    component.setForeground(JBColor.BLACK);
                } else {
                    component.setForeground(Color.decode("#ff5121"));
                }
                return component;
            }
        };
    }

    /**
     * 价格转换
     */
    private static String convertPrice(String stockName, BigDecimal price) {
        if (price == null) {
            return "";
        }
        int scale = 2;
        if (toStr(stockName).toUpperCase().contains("ETF")) {
            scale = 3;
        }
        return price.setScale(scale, RoundingMode.HALF_UP).toString();
    }

    /**
     * 字符串转换
     */
    private static String toStr(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * 数字非空比较
     */
    private static Comparator<BigDecimal> bigDecimalComparator() {
        return (o1, o2) -> {
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) {
                return -1;
            } else if (o2 == null) {
                return 1;
            }
            return o1.compareTo(o2);
        };
    }
}
