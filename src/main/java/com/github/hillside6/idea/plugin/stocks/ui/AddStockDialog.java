package com.github.hillside6.idea.plugin.stocks.ui;

import com.github.hillside6.idea.plugin.stocks.common.type.MarketType;
import com.github.hillside6.idea.plugin.stocks.dao.service.ConfigService;
import com.intellij.openapi.ui.Messages;
import java.math.BigDecimal;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import lombok.Getter;

/**
 * 添加股票
 */
public class AddStockDialog extends JDialog {
    private JPanel contentPane;
    private JButton okButton;
    @Getter
    private JTextField stockCodeTextField;
    @Getter
    private JComboBox<String> marketComboBox;
    @Getter
    private JTextField holdCountTextField;
    @Getter
    private JTextField holdPriceTextField;

    public AddStockDialog() {
        setTitle("新增股票");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(okButton);

        okButton.addActionListener(e -> {
            String stockCode = stockCodeTextField.getText().trim();
            if (stockCode.isBlank()) {
                Messages.showMessageDialog("股票代码不能为空!", "Error!", Messages.getErrorIcon());
                return;
            }
            Object selectedItem = marketComboBox.getSelectedItem();
            MarketType marketType = MarketType.parse((String) selectedItem);
            if (marketType == null) {
                Messages.showMessageDialog("股票市场不能为空!", "Error!", Messages.getErrorIcon());
                return;
            }
            Integer holdCount = null;
            String holdCountText = holdCountTextField.getText().trim();
            if (!holdCountText.isBlank()) {
                try {
                    holdCount = Integer.parseInt(holdCountText);
                    if (holdCount < 0) {
                        Messages.showMessageDialog("持仓数量不能小于0!", "Error!", Messages.getErrorIcon());
                        return;
                    }
                } catch (Exception ex) {
                    Messages.showMessageDialog("持仓数量格式错误!", "Error!", Messages.getErrorIcon());
                    return;
                }
            }
            BigDecimal holdPrice = null;
            String holdPriceText = holdPriceTextField.getText().trim();
            if (!holdPriceText.isBlank()) {
                try {
                    holdPrice = new BigDecimal(holdPriceText);
                } catch (Exception ex) {
                    Messages.showMessageDialog("成本价格式错误!", "Error!", Messages.getErrorIcon());
                    return;
                }
            }
            ConfigService.addStock(stockCode, marketType, holdCount, holdPrice);
            dispose();
        });
    }
}
