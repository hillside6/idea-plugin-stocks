package com.github.hillside6.idea.plugin.stocks.ui;

import com.github.hillside6.idea.plugin.stocks.dao.model.Config;
import com.github.hillside6.idea.plugin.stocks.dao.service.ConfigService;
import com.github.hillside6.idea.plugin.stocks.provider.Provider;
import com.github.hillside6.idea.plugin.stocks.provider.ProviderManager;
import com.github.hillside6.idea.plugin.stocks.ui.config.ViewStock;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.TableView;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.ListTableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

/**
 * 主面板
 *
 * @author hillside6
 * @since 2025/4/15
 */
public class MainPanel {
    @Getter
    private JPanel panel;
    private JButton refreshButton;
    private JButton addButton;
    private JButton deleteButton;
    private JButton upButton;
    private JButton downButton;
    private JButton settingButton;
    private JScrollPane tableScrollPane;
    private JLabel todayTotalHoldProfitTextLabel;
    private JLabel todayTotalHoldProfitLabel;
    private JLabel totalHoldProfitTextLabel;
    private JLabel totalHoldProfitLabel;
    private TableView<ViewStock> tableView;

    /**
     * 创建UI组件
     */
    private void createUIComponents() {
        tableView = new TableView<>();
        tableScrollPane = new JBScrollPane(tableView);
    }

    public MainPanel() {
        $$$setupUI$$$();

        //刷新
        refreshButton.addActionListener(e -> updateData());
        //新增
        addButton.addActionListener(e -> {
            AddStockDialog dialog = new AddStockDialog();
            dialog.setLocationRelativeTo(null);
            dialog.pack();
            dialog.setVisible(true);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateData();
                }
            });
        });
        //删除
        deleteButton.addActionListener(e -> {
            List<ViewStock> viewStockList = tableView.getSelectedObjects();
            if (CollectionUtils.isEmpty(viewStockList)) {
                return;
            }
            String str = viewStockList.stream().map(ViewStock::getStockName).collect(Collectors.joining(","));
            int status = Messages.showOkCancelDialog(str, "是否删除自选股?", "是", "否", Messages.getWarningIcon());
            if (status == Messages.OK) {
                ConfigService.deleteStock(viewStockList.stream().map(ViewStock::getStockMarketCode).toList());
                updateData();
            }
        });
        //上移
        upButton.addActionListener(e -> {
            List<ViewStock> viewStockList = tableView.getSelectedObjects();
            if (CollectionUtils.isEmpty(viewStockList)) {
                return;
            }
            ConfigService.upStock(viewStockList.stream().map(ViewStock::getStockMarketCode).toList());
            updateData();
        });
        //下移
        downButton.addActionListener(e -> {
            List<ViewStock> viewStockList = tableView.getSelectedObjects();
            if (CollectionUtils.isEmpty(viewStockList)) {
                return;
            }
            ConfigService.downStock(viewStockList.stream().map(ViewStock::getStockMarketCode).toList());
            updateData();
        });
        //配置
        settingButton.addActionListener(e -> {
            SettingDialog dialog = new SettingDialog();
            dialog.setTitle("设置");
            dialog.setLocationRelativeTo(null);
            dialog.pack();
            dialog.setVisible(true);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateData();
                }
            });
        });
        tableView.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    List<ViewStock> viewStockList = tableView.getSelectedObjects();
                    ViewStock viewStock = viewStockList.stream().findFirst().orElse(null);
                    if (viewStock != null) {
                        AddStockDialog dialog = new AddStockDialog();
                        dialog.setTitle("修改股票");
                        dialog.setLocationRelativeTo(null);
                        dialog.pack();
                        dialog.getStockCodeTextField().setText(viewStock.getStockCode());
                        dialog.getMarketComboBox().setSelectedItem(viewStock.getMarketType().getMarketName());
                        dialog.getHoldCountTextField()
                            .setText(viewStock.getHoldCount() == null ? "" : viewStock.getHoldCount().toString());
                        dialog.getHoldPriceTextField()
                            .setText(viewStock.getHoldPrice() == null ? "" : viewStock.getHoldPrice().toPlainString());
                        dialog.setVisible(true);
                        dialog.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                updateData();
                            }
                        });
                    }
                }
            }
        });

        updateData();
    }

    /**
     * 数据更新
     */
    private void updateData() {
        Config config = ConfigService.getConfig();
        Provider provider = ProviderManager.getQuoteProvider(config.getProviderType());
        List<ViewStock> viewStockList = provider.load(config.getStockList());
        ListTableModel<ViewStock> tableModel = new ListTableModel<>(StockColumnInfoGenerator.generate(config), viewStockList);
        tableView.setModelAndUpdateColumns(tableModel);
        //今日总盈亏
        if (Boolean.TRUE.equals(config.getShowTodayTotalHoldProfit())) {
            todayTotalHoldProfitTextLabel.setVisible(true);
            todayTotalHoldProfitLabel.setVisible(true);
            BigDecimal todayTotalHoldProfit = viewStockList.stream().map(ViewStock::getTodayHoldProfit)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (todayTotalHoldProfit.compareTo(BigDecimal.ZERO) == 0) {
                todayTotalHoldProfitLabel.setForeground(JBColor.BLACK);
            } else if (BigDecimal.ZERO.compareTo(todayTotalHoldProfit) < 0) {
                todayTotalHoldProfitLabel.setForeground(Color.decode("#dd0000"));
            } else if (BigDecimal.ZERO.compareTo(todayTotalHoldProfit) > 0) {
                todayTotalHoldProfitLabel.setForeground(Color.decode("#009944"));
            }
            todayTotalHoldProfitLabel.setText(todayTotalHoldProfit.setScale(2, RoundingMode.HALF_UP).toPlainString());
        } else {
            todayTotalHoldProfitTextLabel.setVisible(false);
            todayTotalHoldProfitLabel.setVisible(false);
        }
        //总盈亏
        if (Boolean.TRUE.equals(config.getShowTotalHoldProfit())) {
            totalHoldProfitTextLabel.setVisible(true);
            totalHoldProfitLabel.setVisible(true);
            BigDecimal totalHoldProfit = viewStockList.stream().map(ViewStock::getHoldProfit)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalHoldProfit.compareTo(BigDecimal.ZERO) == 0) {
                totalHoldProfitLabel.setForeground(JBColor.BLACK);
            } else if (BigDecimal.ZERO.compareTo(totalHoldProfit) < 0) {
                totalHoldProfitLabel.setForeground(Color.decode("#dd0000"));
            } else if (BigDecimal.ZERO.compareTo(totalHoldProfit) > 0) {
                totalHoldProfitLabel.setForeground(Color.decode("#009944"));
            }
            totalHoldProfitLabel.setText(totalHoldProfit.setScale(2, RoundingMode.HALF_UP).toPlainString());
        } else {
            totalHoldProfitTextLabel.setVisible(false);
            totalHoldProfitLabel.setVisible(false);
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, true));
        refreshButton = new JButton();
        refreshButton.setText("刷新");
        panel2.add(refreshButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(45, 30), new Dimension(45, 30), new Dimension(45, 30), 0, false));
        downButton = new JButton();
        downButton.setText("下移");
        panel2.add(downButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(45, 30), new Dimension(45, 30), new Dimension(45, 30), 0, false));
        upButton = new JButton();
        upButton.setText("上移");
        panel2.add(upButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(45, 30), new Dimension(45, 30), new Dimension(45, 30), 0, false));
        deleteButton = new JButton();
        deleteButton.setText("删除");
        panel2.add(deleteButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(45, 30), new Dimension(45, 30), new Dimension(45, 30), 0, false));
        addButton = new JButton();
        addButton.setText("新增");
        panel2.add(addButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(45, 30), new Dimension(45, 30), new Dimension(45, 30), 0, false));
        settingButton = new JButton();
        settingButton.setText("设置");
        panel2.add(settingButton, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(45, 30), new Dimension(45, 30), new Dimension(45, 30), 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, true));
        todayTotalHoldProfitTextLabel = new JLabel();
        todayTotalHoldProfitTextLabel.setEnabled(true);
        todayTotalHoldProfitTextLabel.setText("今日盈亏");
        panel3.add(todayTotalHoldProfitTextLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        todayTotalHoldProfitLabel = new JLabel();
        todayTotalHoldProfitLabel.setText("");
        panel3.add(todayTotalHoldProfitLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        totalHoldProfitTextLabel = new JLabel();
        totalHoldProfitTextLabel.setText("持仓盈亏");
        panel3.add(totalHoldProfitTextLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        totalHoldProfitLabel = new JLabel();
        totalHoldProfitLabel.setText("");
        panel3.add(totalHoldProfitLabel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.add(tableScrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
