package wills.helper;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import org.apache.http.util.TextUtils;

/**
 * Copyright (c) 2019, Bongmi
 * All rights reserved
 * Author: wi1ls@bongmi.com
 */

public class ClipboardUtils {
  public static String getClipBoardText() {
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable content = clipboard.getContents(null);
    if (content.isDataFlavorSupported(DataFlavor.stringFlavor)) {
      //文本类型
      try {
        String text = (String) content.getTransferData(DataFlavor.stringFlavor);
        if (TextUtils.isEmpty(text)) {
          return "";
        }
        return text;
      } catch (Exception e) {
        return "";
      }
    }
    return "";
  }
}
