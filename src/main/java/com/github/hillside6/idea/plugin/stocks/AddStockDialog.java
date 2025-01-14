package com.github.hillside6.idea.plugin.stocks;

import com.github.hillside6.idea.plugin.stocks.common.MarketType;
import com.github.hillside6.idea.plugin.stocks.config.ConfigManager;
import com.intellij.openapi.ui.Messages;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddStockDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextField codeTextField;
    private JComboBox<String> marketComboBox;

    public AddStockDialog() {
        setTitle("新增股票");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> {
            String code = codeTextField.getText();
            if (code == null || code.isBlank()) {
                Messages.showMessageDialog("代码不能为空!", "Error!", Messages.getErrorIcon());
                return;
            }
            Object selectedItem = marketComboBox.getSelectedItem();
            MarketType marketType = MarketType.parse((String) selectedItem);
            if (marketType == null) {
                Messages.showMessageDialog("市场不能为空!", "Error!", Messages.getErrorIcon());
                return;
            }
            ConfigManager.addStock(code, marketType);
            dispose();
        });
    }
}
