package org.diy.plugin;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

public class MyPluginRegistration implements ApplicationComponent {

    public MyPluginRegistration() {
    }

    @Override
    public void initComponent() {
        // TODO: insert component initialization logic here
        System.out.println("Called action registerer");
        ActionManager actionManager = ActionManager.getInstance();
        GenerateAction generateAction = new GenerateAction();
        actionManager.registerAction("MyPlugin",generateAction);

        DefaultActionGroup windowMenu = (DefaultActionGroup) actionManager.getAction("GenerateGroup");

        windowMenu.addSeparator();
        windowMenu.add(generateAction);

    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "MyPlugin";
    }
}
