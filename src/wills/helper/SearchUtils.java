package wills.helper;

import java.util.ArrayList;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagValue;
import org.apache.http.util.TextUtils;
import wills.widgets.SearchResultShowFrame;

public class SearchUtils {
  public static boolean handleSearch(Project project, String searchPath,
      String searchContent, boolean fuzzy) {
    return handleSearch1(project, searchPath, searchContent,
        fuzzy);
  }

  private static boolean handleSearch1(
      Project project,
      String searchPath,
      String searchString,
      boolean fuzzy) {
    if (TextUtils.isEmpty(searchString)) {
      showErrorInformation(project,
          "搜索内容不能为空");
      return false;
    }

    XmlFile xmlFile = getTargetXmlFile(project, searchPath);
    if (xmlFile == null) {
      showErrorInformation(project,
          "请检查目标资源文件是否存在");
      return false;
    }
    ArrayList<ElementWrapper> dataList = findContentKey(xmlFile, searchString, fuzzy);
    if (dataList.isEmpty()) {
      showErrorInformation(project, "没有找到对应的文案");
      return false;
    } else {
      showSearchList(project, dataList, searchString);
      return true;
    }
  }


  private static XmlFile getTargetXmlFile(Project project, String searchPath) {
    VirtualFile virtualFile = VirtualFileManager.getInstance()
        .findFileByUrl("file://" + searchPath);
    if (virtualFile == null) {
      //这个时候可能文件刚被删掉了
      return null;
    }
    PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
    return (XmlFile) psiFile;
  }

  private static void showErrorInformation(Project project, String errorMsg) {
    Messages.showMessageDialog(project,
        errorMsg,
        "Search ResultResult",
        Messages.getInformationIcon());
  }

  //根据指定的搜索内容，返回符合条件的 String
  private static ArrayList<ElementWrapper> findContentKey(XmlFile xmlFile,
      String targetContent, boolean fuzzy) {
    XmlTag xmlTags[] = xmlFile.getRootTag().getSubTags();

    ArrayList<ElementWrapper> wrapperList = new ArrayList<>();
    for (XmlTag xmlTag : xmlTags) {
      XmlTagValue xmlTagValue = xmlTag.getValue();
      String text = xmlTagValue.getText();
      boolean match = fuzzy ?
          text.contains(targetContent)
          : text.equals(targetContent);
      if (match) {
        ElementWrapper wrapper = new ElementWrapper();
        wrapper.value = text;
        XmlAttribute xmlAttribute = xmlTag.getAttribute("name");
        wrapper.key = xmlAttribute.getValue();
        wrapperList.add(wrapper);
      }
    }
    return wrapperList;
  }

  private static void showSearchList(Project project,
      ArrayList<ElementWrapper> data, String lastSearchContent) {
    new SearchResultShowFrame(project, data, lastSearchContent);
  }
}
