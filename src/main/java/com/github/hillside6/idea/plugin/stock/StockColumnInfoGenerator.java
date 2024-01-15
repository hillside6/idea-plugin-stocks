package com.github.hillside6.idea.plugin.stock;

import com.github.hillside6.idea.plugin.stock.common.StockItem;
import com.github.hillside6.idea.plugin.stock.config.Config;
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
     * 序号
     */
    private static final ColumnInfo<StockItem, Integer> ORDER_COLUMN_INFO = new ColumnInfo<>("序号") {
        @Override
        public Integer valueOf(StockItem stockItem) {
            return stockItem.getOrder();
        }

        @Override
        public @Nullable Comparator<StockItem> getComparator() {
            return Comparator.comparing(StockItem::getOrder);
        }
    };
    /**
     * 代码
     */
    private static final ColumnInfo<StockItem, String> CODE_COLUMN_INFO = new ColumnInfo<>("代码") {
        @Override
        public String valueOf(StockItem stockItem) {
            return stockItem.getCode();
        }
    };
    /**
     * 代码
     */
    private static final ColumnInfo<StockItem, String> SORTABLE_CODE_COLUMN_INFO = new ColumnInfo<>("代码") {
        @Override
        public String valueOf(StockItem stockItem) {
            return stockItem.getCode();
        }

        @Override
        public @Nullable Comparator<StockItem> getComparator() {
            return Comparator.comparing(StockItem::getCode);
        }
    };
    /**
     * 代码
     */
    private static final ColumnInfo<StockItem, String> NAME_COLUMN_INFO = new ColumnInfo<>("名称") {
        @Override
        public String valueOf(StockItem stockItem) {
            return stockItem.getName();
        }

        @Override
        public @Nullable Comparator<StockItem> getComparator() {
            return Comparator.comparing(StockItem::getName);
        }
    };
    /**
     * 最新
     */
    private static final ColumnInfo<StockItem, String> LAST_PRICE_COLUMN_INFO = new ColumnInfo<>("最新") {
        @Override
        public String valueOf(StockItem stockItem) {
            return stockItem.getLastPrice().setScale(2, RoundingMode.HALF_UP).toString();
        }

        @Override
        public @Nullable Comparator<StockItem> getComparator() {
            return Comparator.comparing(StockItem::getLastPrice);
        }

        @Override
        public TableCellRenderer getRenderer(StockItem stockItem) {
            return new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    renderColor(component, stockItem);
                    return component;
                }
            };
        }
    };
    /**
     * 涨跌
     */
    private static final ColumnInfo<StockItem, String> CHANGE_COLUMN_INFO = new ColumnInfo<>("涨跌") {
        @Override
        public String valueOf(StockItem stockItem) {
            return stockItem.getChange().setScale(2, RoundingMode.HALF_UP).toString();
        }

        @Override
        public @Nullable Comparator<StockItem> getComparator() {
            return Comparator.comparing(StockItem::getChange);
        }

        @Override
        public TableCellRenderer getRenderer(StockItem stockItem) {
            return new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    renderColor(component, stockItem);
                    return component;
                }
            };
        }
    };
    /**
     * 涨幅
     */
    private static final ColumnInfo<StockItem, String> CHANGE_PERCENT_COLUMN_INFO = new ColumnInfo<>("涨幅") {
        @Override
        public String valueOf(StockItem stockItem) {
            return stockItem.getChangePercent().setScale(2, RoundingMode.HALF_UP) + "%";
        }

        @Override
        public @Nullable Comparator<StockItem> getComparator() {
            return Comparator.comparing(StockItem::getChangePercent);
        }

        @Override
        public TableCellRenderer getRenderer(StockItem stockItem) {
            return new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    renderColor(component, stockItem);
                    return component;
                }
            };
        }
    };
    /**
     * 昨收
     */
    private static final ColumnInfo<StockItem, String> PREVIOUS_CLOSE_PRICE_COLUMN_INFO = new ColumnInfo<>("昨收") {
        @Override
        public String valueOf(StockItem stockItem) {
            return stockItem.getPreviousClosePrice().setScale(2, RoundingMode.HALF_UP).toString();
        }

        @Override
        public @Nullable Comparator<StockItem> getComparator() {
            return Comparator.comparing(StockItem::getPreviousClosePrice);
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
            return new ColumnInfo[]{ORDER_COLUMN_INFO, SORTABLE_CODE_COLUMN_INFO, NAME_COLUMN_INFO,
                    LAST_PRICE_COLUMN_INFO, CHANGE_COLUMN_INFO, CHANGE_PERCENT_COLUMN_INFO, PREVIOUS_CLOSE_PRICE_COLUMN_INFO};
        }
    }

    /**
     * 颜色渲染
     */
    private static void renderColor(Component component, StockItem stockItem) {
        if (BigDecimal.ZERO.compareTo(stockItem.getChangePercent()) < 0) {
            component.setForeground(Color.decode("#dd0000"));
        } else if (BigDecimal.ZERO.compareTo(stockItem.getChangePercent()) > 0) {
            component.setForeground(Color.decode("#009944"));
        } else {
            component.setForeground(JBColor.BLACK);
        }
    }
}
