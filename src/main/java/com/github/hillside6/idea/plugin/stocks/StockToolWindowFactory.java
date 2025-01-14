package com.github.hillside6.idea.plugin.stocks;

import com.github.hillside6.idea.plugin.stocks.config.Config;
import com.github.hillside6.idea.plugin.stocks.config.ConfigManager;
import com.github.hillside6.idea.plugin.stocks.config.Stock;
import com.github.hillside6.idea.plugin.stocks.provider.Provider;
import com.github.hillside6.idea.plugin.stocks.provider.ProviderManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.panels.HorizontalLayout;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.table.TableView;
import com.intellij.util.ui.ListTableModel;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author hillside6
 * @since 2021/01/15
 */
public class StockToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        //按钮栏
        JButton refreshBtn = new JButton("刷新");
        refreshBtn.setPreferredSize(new Dimension(45, 30));
        JButton addBtn = new JButton("新增");
        addBtn.setPreferredSize(new Dimension(45, 30));
        JButton deleteBtn = new JButton("删除");
        deleteBtn.setPreferredSize(new Dimension(45, 30));
        JButton upBtn = new JButton("上移");
        upBtn.setPreferredSize(new Dimension(45, 30));
        JButton downBtn = new JButton("下移");
        downBtn.setPreferredSize(new Dimension(45, 30));
        JButton configBtn = new JButton("设置");
        configBtn.setPreferredSize(new Dimension(45, 30));
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new HorizontalLayout(5, SwingConstants.CENTER));
        btnPanel.add(refreshBtn, HorizontalLayout.LEFT);
        btnPanel.add(addBtn, HorizontalLayout.LEFT);
        btnPanel.add(deleteBtn, HorizontalLayout.LEFT);
        btnPanel.add(upBtn, HorizontalLayout.LEFT);
        btnPanel.add(downBtn, HorizontalLayout.LEFT);
        btnPanel.add(configBtn, HorizontalLayout.LEFT);
        btnPanel.setBounds(5, 5, 300, 30);
        //数据表格栏
        TableView<Stock> tableView = new TableView<>();
        JBScrollPane jbScrollPane = new JBScrollPane(tableView);
        jbScrollPane.setBounds(0, 40, 1, 1);
        //主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.add(btnPanel);
        mainPanel.add(jbScrollPane);
        //主面板大小变动事件
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Dimension dimension = mainPanel.getSize();
                jbScrollPane.setSize(new Dimension(dimension.width, Math.max(dimension.height - 40, 0)));
            }
        });
        //新增事件
        addBtn.addActionListener(e -> {
            AddStockDialog dialog = new AddStockDialog();
            dialog.setLocationRelativeTo(null);
            dialog.pack();
            dialog.setVisible(true);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    updateData(tableView);
                }
            });
        });
        //删除事件
        bindDeleteEvent(deleteBtn, tableView);
        //刷新事件
        refreshBtn.addActionListener(e -> updateData(tableView));
        //up事件
        bindUpEvent(upBtn, tableView);
        //down事件
        bindDownEvent(downBtn, tableView);
        //配置
        configBtn.addActionListener(e -> {
            boolean isOk = new UpdateConfigDialogWrapper().showAndGet();
            if (isOk) {
                updateData(tableView);
            }
        });

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(mainPanel, null, false);
        toolWindow.getContentManager().addContent(content);
        updateData(tableView);
    }

    private void bindDownEvent(JButton downBtn, TableView<Stock> tableView) {
        downBtn.addActionListener(e -> {
            List<Stock> stockList = tableView.getSelectedObjects();
            if (CollectionUtils.isEmpty(stockList)) {
                return;
            }
            stockList = new ArrayList<>(stockList);
            Collections.reverse(stockList);
            for (Stock stock : stockList) {
                ConfigManager.downStock(stock == null ? "" : stock.getCode());
            }
            updateData(tableView);
        });
    }

    private void bindUpEvent(JButton upBtn, TableView<Stock> tableView) {
        upBtn.addActionListener(e -> {
            List<Stock> stockList = tableView.getSelectedObjects();
            if (CollectionUtils.isEmpty(stockList)) {
                return;
            }
            for (Stock stock : stockList) {
                ConfigManager.upStock(stock == null ? "" : stock.getCode());
            }
            updateData(tableView);
        });
    }

    private void bindDeleteEvent(JButton deleteBtn, TableView<Stock> tableView) {
        deleteBtn.addActionListener(e -> {
            List<Stock> stockList = tableView.getSelectedObjects();
            if (CollectionUtils.isEmpty(stockList)) {
                return;
            }
            String str = stockList.stream().map(Stock::getName).collect(Collectors.joining(","));
            int status = Messages.showOkCancelDialog(str, "是否删除自选股?", "是", "否", Messages.getWarningIcon());
            if (status == Messages.OK) {
                ConfigManager.deleteStock(stockList.stream().map(Stock::getCode).toList());
                updateData(tableView);
            }
        });
    }

    private void updateData(TableView<Stock> tableView) {
        Config config = ConfigManager.loadConfig();
        Provider provider = ProviderManager.getQuoteProvider(config);
        provider.load(config.getStockList());
        ListTableModel<Stock> tableModel = new ListTableModel<>(StockColumnInfoGenerator
                .generate(config), config.getStockList());
        tableView.setModelAndUpdateColumns(tableModel);
    }
}
