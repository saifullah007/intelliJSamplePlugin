package org.diy.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class GenerateDialog extends DialogWrapper {


    LabeledComponent<JPanel> myComponent;
    private CollectionListModel<PsiField> myFields;

    public GenerateDialog(PsiClass psiClass) {
        super(psiClass.getProject());
        setTitle("Select multiple Fields");


        myFields = new CollectionListModel<>(psiClass.getAllFields());

        JList fieldList = new JList(myFields);
        fieldList.setCellRenderer(new DefaultListCellRenderer());
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);

//        decorator.disableAddAction();

        JPanel panel = decorator.createPanel();

        myComponent = LabeledComponent.create(panel,"Fields to include");

//        It has to be called
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return myComponent;
    }

    public List<PsiField> getFields() {
        return myFields.getItems();
    }
}
