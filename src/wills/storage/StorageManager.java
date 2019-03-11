package wills.storage;

import com.intellij.ide.util.PropertiesComponent;

public class StorageManager {

    public static class FindDocumentUtils{
        private static final String FILTER_XML="strings.xml";
        public static  void saveFilePath(String filePath){
            PropertiesComponent.getInstance().setValue(FILTER_XML,filePath);
        }
        public static String getFilePath(){
            return PropertiesComponent.getInstance().getValue(FILTER_XML);
        }

    }
}
