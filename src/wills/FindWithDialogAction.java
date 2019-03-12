package wills;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.apache.http.util.TextUtils;
import wills.helper.ClipboardUtils;
import wills.widgets.SearchDialogModelFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

//主动打开搜索框
public class FindWithDialogAction extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    String clipText = ClipboardUtils.getClipBoardText();
    new SearchDialogModelFrame(e.getProject(), clipText);
  }


}
