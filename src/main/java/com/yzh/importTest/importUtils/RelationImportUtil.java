package com.yzh.importTest.importUtils;

import com.yzh.dao.ERelation;
import com.yzh.utilts.FileTools;
import onegis.psde.relation.Relation;

import java.util.List;

;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/25 11:43
 */
public class RelationImportUtil {
    /**
     *
     * @param url
     */
    public static void upLoadRelation(String url){
        String relationStr = FileTools.readFile(url);
        List<Relation> relations = FileTools.jsonArray2List(relationStr, Relation.class);

        for (Relation relation : relations) {
            ERelation eRelation  =new ERelation();
            eRelation.setName(relation.getName());
        }
    }
}
