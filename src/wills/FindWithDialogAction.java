package wills;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import wills.helper.ClipboardUtils;
import wills.widgets.SearchDialogModelFrame;

//主动打开搜索框
public class FindWithDialogAction extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    String clipText = ClipboardUtils.getClipBoardText();
    new SearchDialogModelFrame(e.getProject(), clipText);
  }
}
