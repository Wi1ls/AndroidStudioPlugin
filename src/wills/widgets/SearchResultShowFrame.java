package wills.widgets;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import wills.helper.ClipboardUtils;
import wills.helper.ElementWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.util.ArrayList;

public class SearchResultShowFrame extends JFrame {


  public SearchResultShowFrame(Project project,
      ArrayList<ElementWrapper> data,String lastSearchContent) throws HeadlessException {
    setTitle("Search Result");
    setLayout(new GridLayout(3, 1));
    setSize(500, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();

    JButton backButton = new JButton();
    backButton.setText("返回到上一级");
    backButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        new SearchDialogModelFrame(project,
            lastSearchContent);
        dispose();
      }
    });
    panel.add(backButton);

    // 创建一个 JList 实例
    final JList<String> list = new JList<>();
    list.setPreferredSize(new Dimension(480, 100));
    // 允许可间断的多选
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    String[] dataArray = new String[data.size()];
    for (int i = 0; i < data.size(); i++) {
      dataArray[i] = "\n" + data.get(i).toString() + "\n";
    }

    // 设置选项数据（内部将自动封装成 ListModel ）
    list.setListData(dataArray);

    // 设置默认选中项
    list.setSelectedIndex(1);
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

    JScrollPane scrollPane = new JScrollPane();
    scrollPane.setPreferredSize(new Dimension(500, 300));

    scrollPane.setViewportView(list);

    // 添加到内容面板容器
    panel.add(scrollPane);


    JButton jButton = new JButton();
    jButton.setText("复制钥匙到剪切板");
    jButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int index = list.getSelectedIndex();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 封装文本内容
        Transferable trans = new StringSelection(data.get(index).key);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(trans, null);
      }
    });

    panel.add(jButton);
    setContentPane(panel);

    getRootPane().registerKeyboardAction(
        (e) ->
            dispose()
        , "command",
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_IN_FOCUSED_WINDOW);

    setVisible(true);

  }
}
