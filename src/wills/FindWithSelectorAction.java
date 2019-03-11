package wills;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import org.jetbrains.annotations.NotNull;
import wills.helper.SearchUtils;
import org.apache.http.util.TextUtils;
import wills.storage.StorageManager;
import wills.widgets.SelectSearchFileFrame;
//根据选择的内容，调用搜索
public class FindWithSelectorAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
        if (mEditor == null) {
            return;
        }
        SelectionModel mSelectionModel = mEditor.getSelectionModel();
        String selectedText = mSelectionModel.getSelectedText();
        if (TextUtils.isEmpty(selectedText)) {
            return;
        }
        String searchFilePath = StorageManager.FindDocumentUtils.getFilePath();
        if (TextUtils.isEmpty(searchFilePath)) {
            new SelectSearchFileFrame(e.getProject(), selectedText);
        } else {
            SearchUtils.handleSearch(e.getProject(), searchFilePath, selectedText);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor != null) {
            SelectionModel mSelectionModel = editor.getSelectionModel();
            String selectedText = mSelectionModel.getSelectedText();
            if (!TextUtils.isEmpty(selectedText)) {
                e.getPresentation().setEnabled(true);
                return;
            }
        }
        e.getPresentation().setEnabled(false);

    }
}
