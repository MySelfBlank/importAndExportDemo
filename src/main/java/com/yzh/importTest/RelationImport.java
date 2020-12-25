package com.yzh.importTest;

import com.yzh.utilts.FileTools;

import java.io.File;
import java.util.List;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/25 9:48
 */
public class RelationImport {
    public static void upLoadRelation(String url){
        List<File> files = FileTools.getFiles(url);
        for (File file : files) {

        }
    }
}
