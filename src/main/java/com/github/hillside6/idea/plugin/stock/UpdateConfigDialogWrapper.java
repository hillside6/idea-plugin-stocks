package com.github.hillside6.idea.plugin.stock;

import com.github.hillside6.idea.plugin.stock.common.QuoteProviderType;
import com.github.hillside6.idea.plugin.stock.config.Config;
import com.github.hillside6.idea.plugin.stock.config.ConfigManager;
import com.github.hillside6.idea.plugin.stock.quote.ProviderManager;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.panels.VerticalLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author hillside6
 * @since 2021/01/15
 */
public class UpdateConfigDialogWrapper extends DialogWrapper {
    private JCheckBox simpleCheckBox;
    private ComboBox<String> providerComboBox;

    public UpdateConfigDialogWrapper() {
        super(true);
        init();
        setTitle("设置");
    }

    /**
     * 创建视图
     */
    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new VerticalLayout(0, SwingConstants.LEFT));

        Config config = ConfigManager.loadConfig();

        //简单版或复杂版
        JLabel isSimpleLabel = new JLabel("简单版显示:");
        simpleCheckBox = new JCheckBox("是");
        simpleCheckBox.setSelected(config.getSimple());
        JPanel isSimplePanel = new JPanel();
        isSimplePanel.add(isSimpleLabel);
        isSimplePanel.add(simpleCheckBox);
        mainPanel.add(isSimplePanel, VerticalLayout.TOP);

        //配置的行情数据源
        JLabel quoteAdapterLabel = new JLabel("行情数据源:");
        DefaultComboBoxModel<String> quoteAdapterModel = new DefaultComboBoxModel<>();
        for (QuoteProviderType providerType : ProviderManager.getProviderList()) {
            quoteAdapterModel.addElement(providerType.getDesc());
        }
        providerComboBox = new ComboBox<>(quoteAdapterModel);
        providerComboBox.setSelectedItem(config.getProviderType().getDesc());
        JPanel quoteAdapterPanel = new JPanel();
        quoteAdapterPanel.add(quoteAdapterLabel);
        quoteAdapterPanel.add(providerComboBox);
        mainPanel.add(quoteAdapterPanel, VerticalLayout.TOP);

        return mainPanel;
    }

    /**
     * 校验数据
     */
    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        return null;
    }

    /**
     * 覆盖默认的ok/cancel按钮
     */
    @NotNull
    @Override
    protected Action @NotNull [] createActions() {
        DialogWrapperAction okAction = new DialogWrapperAction("修改") {
            @Override
            protected void doAction(ActionEvent e) {
                Config config = ConfigManager.loadConfig();
                config.setSimple(simpleCheckBox.isSelected());
                config.setProviderType(QuoteProviderType.parse((String) providerComboBox.getSelectedItem()));
                ConfigManager.updateConfig(config);
                close(OK_EXIT_CODE);
            }
        };
        //默认获得焦点事件
        okAction.putValue(DialogWrapper.DEFAULT_ACTION, true);
        return new Action[]{okAction};
    }
}
