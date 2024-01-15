package com.github.hillside6.idea.plugin.stock;

import com.github.hillside6.idea.plugin.stock.common.MarketType;
import com.github.hillside6.idea.plugin.stock.config.ConfigManager;
import com.github.hillside6.idea.plugin.stock.config.Stock;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.panels.VerticalLayout;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * @author hillside6
 * @since 2021/01/15
 */
public class AddStockDialogWrapper extends DialogWrapper {
    private final Pattern codePattern = Pattern.compile("^(\\d{6})$");

    private JTextField codeTextField;

    public AddStockDialogWrapper() {
        super(true);
        init();
        setTitle("请输入股票代码");
        setOKButtonText("新增");
        setCancelButtonText("取消");
    }

    /**
     * 创建视图
     */
    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new VerticalLayout(0, SwingConstants.LEFT));

        JLabel codeLabel = new JLabel("代码:");
        codeTextField = new JTextField();
        codeTextField.setPreferredSize(new Dimension(150, 30));
        codeTextField.grabFocus();
        JPanel codePanel = new JPanel();
        codePanel.add(codeLabel);
        codePanel.add(codeTextField);
        mainPanel.add(codePanel, VerticalLayout.TOP);
        return mainPanel;
    }

    @Override
    protected void doOKAction() {
        String code = codeTextField.getText();
        if (code == null || code.isBlank()) {
            Messages.showMessageDialog("代码不能为空!", "Error!", Messages.getErrorIcon());
        } else if (!codePattern.matcher(code).matches()) {
            Messages.showMessageDialog("代码格式错误!", "Error!", Messages.getErrorIcon());
        } else {
            MarketType marketType;
            if (code.startsWith("6")) {
                marketType = MarketType.SH;
            } else {
                marketType = MarketType.SZ;
            }
            Stock stock = new Stock(code, marketType);
            ConfigManager.addStock(stock);
            close(OK_EXIT_CODE);
        }
    }
}
