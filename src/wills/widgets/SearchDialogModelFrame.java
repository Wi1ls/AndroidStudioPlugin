package wills.widgets;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.http.util.TextUtils;
import wills.helper.SearchUtils;
import wills.storage.StorageManager;

public class SearchDialogModelFrame extends JFrame {
  private String searchPath;

  public SearchDialogModelFrame(Project project,
      String defaultContent) throws HeadlessException {
    setTitle("搜索");
    setLayout(new GridLayout(4, 1));
    setSize(500, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    JPanel panel1 = new JPanel();
    JTextArea jTextArea1 = new JTextArea();
    JTextArea jTextArea = jTextArea1;
    jTextArea.setSize(500, 100);
    jTextArea.setWrapStyleWord(true);
    jTextArea.setEditable(false);
    jTextArea.setLineWrap(true);
    searchPath = StorageManager.FindDocumentUtils.getFilePath();
    if (TextUtils.isEmpty(searchPath)) {
      jTextArea.setText("请按下方按钮选择String文件");
    } else {
      jTextArea.setText(searchPath);
    }
    panel1.add(jTextArea);

    JButton fileSelectorButton = new JButton();
    fileSelectorButton.setText("选择String文件");
    panel1.add(fileSelectorButton);

    fileSelectorButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter xmlFileFilter = new FileNameExtensionFilter("xml Filter", ".xml");
        fileChooser.setFileFilter(xmlFileFilter);
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
          //获取基本信息
          String filePath = fileChooser.getSelectedFile().getAbsolutePath();
          searchPath = filePath;
          jTextArea.setText(filePath);
          StorageManager.FindDocumentUtils.saveFilePath(filePath);
        }
      }
    });


    JPanel panel2 = new JPanel();
    JTextField jTextField = new JTextField(35);
    jTextField.setText(defaultContent);
    panel2.add(jTextField);


    JPanel panel3 = new JPanel();
    JButton searchButton = new JButton();
    searchButton.setText("开始搜索");
    searchButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        doSearch(project, jTextField);
      }
    });

    panel3.add(searchButton);

    add(panel1);
    add(panel2);
    add(panel3);

    getRootPane().registerKeyboardAction(
        (e) ->
            dispose()
        , "command",
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_IN_FOCUSED_WINDOW);

    getRootPane().registerKeyboardAction((e) -> {
          doSearch(project, jTextField);
        }
        , KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
        JComponent.WHEN_IN_FOCUSED_WINDOW);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowActivated(WindowEvent e) {
        jTextField.grabFocus();
        jTextField.requestFocus();
        jTextField.selectAll();
      }
    });

    setVisible(true);
  }

  private void doSearch(Project project, JTextField jTextField) {
    if (TextUtils.isEmpty(searchPath)) {
      //没有选择的文件
      Messages.showMessageDialog(project,
          "请选择要检索的资源文件",
          "ERROR",
          Messages.getErrorIcon());
      return;
    }
    String searchText = jTextField.getText();
    if (TextUtils.isEmpty(searchText)) {
      //输入的文本有问题
      Messages.showMessageDialog(project,
          "查找的内容不能为空",
          "ERROR",
          Messages.getErrorIcon());
    } else {
      if (SearchUtils.handleSearch(project, searchPath, searchText)) {
        dispose();
      }
    }
  }
}
