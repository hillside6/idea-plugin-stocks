package com.github.hillside6.idea.plugin.stock;

import com.github.hillside6.idea.plugin.stock.common.StockItem;
import com.github.hillside6.idea.plugin.stock.config.Config;
import com.github.hillside6.idea.plugin.stock.config.ConfigManager;
import com.github.hillside6.idea.plugin.stock.quote.Provider;
import com.github.hillside6.idea.plugin.stock.quote.ProviderManager;
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
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        TableView<StockItem> tableView = new TableView<>();
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
            boolean isOk = new AddStockDialogWrapper().showAndGet();
            if (isOk) {
                updateData(tableView);
            }
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
        Content content = contentFactory.createContent(mainPanel, "我的自选", false);
        toolWindow.getContentManager().addContent(content);
        updateData(tableView);
    }

    private void bindDownEvent(JButton downBtn, TableView<StockItem> tableView) {
        downBtn.addActionListener(e -> {
            List<StockItem> stockItemList = tableView.getSelectedObjects();
            if (CollectionUtils.isEmpty(stockItemList)) {
                return;
            }
            stockItemList = new ArrayList<>(stockItemList);
            Collections.reverse(stockItemList);
            for (StockItem stockItem : stockItemList) {
                ConfigManager.downStock(stockItem == null ? "" : stockItem.getCode());
            }
            updateData(tableView);
        });
    }

    private void bindUpEvent(JButton upBtn, TableView<StockItem> tableView) {
        upBtn.addActionListener(e -> {
            List<StockItem> stockItemList = tableView.getSelectedObjects();
            if (CollectionUtils.isEmpty(stockItemList)) {
                return;
            }
            for (StockItem stockItem : stockItemList) {
                ConfigManager.upStock(stockItem == null ? "" : stockItem.getCode());
            }
            updateData(tableView);
        });
    }

    private void bindDeleteEvent(JButton deleteBtn, TableView<StockItem> tableView) {
        deleteBtn.addActionListener(e -> {
            List<StockItem> stockItemList = tableView.getSelectedObjects();
            if (CollectionUtils.isEmpty(stockItemList)) {
                return;
            }
            String str = stockItemList.stream().map(StockItem::getName).collect(Collectors.joining(","));
            int status = Messages.showOkCancelDialog(str, "是否删除自选股?", "是", "否", Messages.getWarningIcon());
            if (status == Messages.OK) {
                ConfigManager.deleteStoredStock(stockItemList.stream().map(StockItem::getCode).toList());
                updateData(tableView);
            }
        });
    }

    private void updateData(TableView<StockItem> tableView) {
        Config config = ConfigManager.loadConfig();
        Provider provider = ProviderManager.getQuoteProvider(config);
        List<StockItem> stockItemList = provider.load(config.getStockList());
        ListTableModel<StockItem> tableModel = new ListTableModel<>(StockColumnInfoGenerator.generate(config), stockItemList);
        tableView.setModelAndUpdateColumns(tableModel);
    }
}
