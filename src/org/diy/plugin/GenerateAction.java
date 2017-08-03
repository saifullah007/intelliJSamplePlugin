package org.diy.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.List;

public class GenerateAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        PsiClass psiClass = getPsiClassFromActionEvent(e);
        GenerateDialog dialog = new GenerateDialog(psiClass);
        dialog.show();
        if (dialog.isOK()) {
            System.out.println("Dialog ok button is clicked");
            generateToStringMethod(psiClass,dialog.getFields());
        }
    }

    private void generateToStringMethod(PsiClass psiClass, List<PsiField> fields) {

        new WriteCommandAction.Simple(psiClass.getProject(),psiClass.getContainingFile()){

            @Override
            protected void run() throws Throwable {
                StringBuilder stringBuilder = new StringBuilder("public void customToString(){");
                stringBuilder
                        .append("return \"")
                        .append(psiClass.getName())
                        .append("\"");

                for (PsiField psiField : fields) {
                    stringBuilder.append("+\"").append(psiField.getName()).append("\"+").append(psiField.getName());
                }
                stringBuilder.append(";}");

                System.out.println("Creating method!");
                PsiMethod methodFromText = JavaPsiFacade.getElementFactory(getProject()).createMethodFromText(stringBuilder.toString(), psiClass);
                PsiMethod[] allMethods = psiClass.getAllMethods();
                PsiMethod lastMethod = null;
                if (allMethods!=null && allMethods.length>0){
                     lastMethod = allMethods[allMethods.length - 1];
                }

                if (lastMethod!=null){
                    psiClass.addAfter(methodFromText,lastMethod);
                }else {
                    psiClass.add(methodFromText);
                }


            }
        }.execute();
    }

    @Override
    public void update(AnActionEvent e) {
        PsiClass psiClass = getPsiClassFromActionEvent(e);
        e.getPresentation().setEnabled(psiClass!=null);
    }

    public static PsiClass getPsiClassFromActionEvent(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile ==null || editor == null) {
            e.getPresentation().setEnabled(false);
            return null;
        }


        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);

        PsiClass psiClass = PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
        if (psiClass == null) {
            e.getPresentation().setEnabled(false);
            return psiClass;
        }

        return psiClass;
    }
}
