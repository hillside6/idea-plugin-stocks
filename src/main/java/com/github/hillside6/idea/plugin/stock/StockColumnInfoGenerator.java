package com.github.hillside6.idea.plugin.stock;

import com.github.hillside6.idea.plugin.stock.config.Config;
import com.github.hillside6.idea.plugin.stock.config.Stock;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

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
    private static final ColumnInfo<Stock, String> CODE_COLUMN_INFO = new ColumnInfo<>("代码") {
        @Override
        public String valueOf(Stock stock) {
            return stock.getCode();
        }
    };
    /**
     * 代码
     */
    private static final ColumnInfo<Stock, String> SORTABLE_CODE_COLUMN_INFO = new ColumnInfo<>("代码") {
        @Override
        public String valueOf(Stock stock) {
            return stock.getCode();
        }

        @Override
        public @Nullable Comparator<Stock> getComparator() {
            return Comparator.comparing(Stock::getCode);
        }
    };
    /**
     * 代码
     */
    private static final ColumnInfo<Stock, String> NAME_COLUMN_INFO = new ColumnInfo<>("名称") {
        @Override
        public String valueOf(Stock stock) {
            return stock.getName();
        }

        @Override
        public @Nullable Comparator<Stock> getComparator() {
            return Comparator.comparing(Stock::getName);
        }
    };
    /**
     * 最新
     */
    private static final ColumnInfo<Stock, String> LAST_PRICE_COLUMN_INFO = new ColumnInfo<>("最新") {
        @Override
        public String valueOf(Stock stock) {
            return stock.getLastPrice().setScale(2, RoundingMode.HALF_UP).toString();
        }

        @Override
        public @Nullable Comparator<Stock> getComparator() {
            return Comparator.comparing(Stock::getLastPrice);
        }

        @Override
        public TableCellRenderer getRenderer(Stock stock) {
            return new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    renderColor(component, stock);
                    return component;
                }
            };
        }
    };
    /**
     * 涨跌
     */
    private static final ColumnInfo<Stock, String> CHANGE_COLUMN_INFO = new ColumnInfo<>("涨跌") {
        @Override
        public String valueOf(Stock stock) {
            return stock.getChange().setScale(2, RoundingMode.HALF_UP).toString();
        }

        @Override
        public @Nullable Comparator<Stock> getComparator() {
            return Comparator.comparing(Stock::getChange);
        }

        @Override
        public TableCellRenderer getRenderer(Stock stock) {
            return new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    renderColor(component, stock);
                    return component;
                }
            };
        }
    };
    /**
     * 涨幅
     */
    private static final ColumnInfo<Stock, String> CHANGE_PERCENT_COLUMN_INFO = new ColumnInfo<>("涨幅") {
        @Override
        public String valueOf(Stock stock) {
            return stock.getChangePercent().setScale(2, RoundingMode.HALF_UP) + "%";
        }

        @Override
        public @Nullable Comparator<Stock> getComparator() {
            return Comparator.comparing(Stock::getChangePercent);
        }

        @Override
        public TableCellRenderer getRenderer(Stock stock) {
            return new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    renderColor(component, stock);
                    return component;
                }
            };
        }
    };
    /**
     * 昨收
     */
    private static final ColumnInfo<Stock, String> PREVIOUS_CLOSE_PRICE_COLUMN_INFO = new ColumnInfo<>("昨收") {
        @Override
        public String valueOf(Stock stock) {
            return stock.getPreviousClosePrice().setScale(2, RoundingMode.HALF_UP).toString();
        }

        @Override
        public @Nullable Comparator<Stock> getComparator() {
            return Comparator.comparing(Stock::getPreviousClosePrice);
        }
    };

    /**
     * 生成显示的表格列
     */
    @SuppressWarnings("rawtypes")
    public static ColumnInfo[] generate(Config config) {
        if (Boolean.TRUE.equals(config.getSimple())) {
            return new ColumnInfo[]{CODE_COLUMN_INFO, NAME_COLUMN_INFO, LAST_PRICE_COLUMN_INFO, CHANGE_PERCENT_COLUMN_INFO};
        } else {
            return new ColumnInfo[]{SORTABLE_CODE_COLUMN_INFO, NAME_COLUMN_INFO,
                    LAST_PRICE_COLUMN_INFO, CHANGE_COLUMN_INFO, CHANGE_PERCENT_COLUMN_INFO, PREVIOUS_CLOSE_PRICE_COLUMN_INFO};
        }
    }

    /**
     * 颜色渲染
     */
    private static void renderColor(Component component, Stock stock) {
        if (BigDecimal.ZERO.compareTo(stock.getChangePercent()) < 0) {
            component.setForeground(Color.decode("#dd0000"));
        } else if (BigDecimal.ZERO.compareTo(stock.getChangePercent()) > 0) {
            component.setForeground(Color.decode("#009944"));
        } else {
            component.setForeground(JBColor.BLACK);
        }
    }
}
