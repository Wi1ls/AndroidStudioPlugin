package wills.widgets;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import wills.helper.ElementWrapper;

public class SearchResultShowFrame extends BaseFrame {


  public SearchResultShowFrame(Project project,
      ArrayList<ElementWrapper> data,
      String lastSearchContent) throws HeadlessException {
    setTitle("Search Result");
    Box boxMain = Box.createVerticalBox();
    setSize(500, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    Box boxBack = Box.createHorizontalBox();
    boxBack.add(Box.createGlue());
    JButton btnBack = new JButton();
    btnBack.setText("back");
    btnBack.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        new SearchDialogModelFrame(project,
            lastSearchContent);
        dispose();
      }
    });
    boxBack.add(btnBack);
    boxBack.add(Box.createGlue());
    boxMain.add(boxBack);

    // 创建一个 JList 实例
    final JBList<String> list = new JBList<>();
    // 允许可间断的多选
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    String[] dataArray = new String[data.size()];
    for (int i = 0; i < data.size(); i++) {
      dataArray[i] = "\n" + data.get(i).toString() + "\n";
    }

    // 设置选项数据（内部将自动封装成 ListModel ）
    list.setListData(dataArray);

    // 设置默认选中项
    list.setSelectedIndex(0);
    list.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          int index = list.getSelectedIndex();
          Messages.showMessageDialog(
              data.get(index).value,
              "Detail",
              null);
        }
      }
    });

    JBScrollPane scrollPane = new JBScrollPane();
    scrollPane.getViewport().setPreferredSize(new Dimension(300, 300));
    scrollPane.setViewportView(list);
    scrollPane.setHorizontalScrollBar(new JScrollBar());
    scrollPane.setVerticalScrollBar(new JScrollBar());
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    boxMain.add(scrollPane);


    Box boxCopy = Box.createHorizontalBox();
    boxCopy.add(Box.createGlue());
    JButton btnCopy = new JButton();
    btnCopy.setText("copy key($name)");
    btnCopy.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int index = list.getSelectedIndex();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 封装文本内容
        Transferable trans = new StringSelection(data.get(index).key);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(trans, null);
        dispose();
      }
    });
    boxCopy.add(btnCopy);
    boxCopy.add(Box.createGlue());
    boxMain.add(boxCopy);

    add(boxMain);

    getRootPane().registerKeyboardAction(
        (e) ->
            dispose()
        , "command",
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_IN_FOCUSED_WINDOW);

    setVisible(true);
  }
}
