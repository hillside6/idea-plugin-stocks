package com.github.hillside6.idea.plugin.stocks;

import com.github.hillside6.idea.plugin.stocks.ui.MainPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author hillside6
 * @since 2021/01/15
 */
public class StockToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        //主面板
        MainPanel mainPanel = new MainPanel();
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(mainPanel.getPanel(), null, false);
        toolWindow.getContentManager().addContent(content);
    }
}
