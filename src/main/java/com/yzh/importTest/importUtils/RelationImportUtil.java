package com.yzh.importTest.importUtils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.importTest.requestEntity.RelationEntity;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.FileTools;
import onegis.psde.relation.Relation;

import java.util.ArrayList;
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

        List<RelationEntity> params = new ArrayList<>();
        for (Relation relation : relations) {
            RelationEntity relationEntity =new RelationEntity();

            params.clear();
            relationEntity.setName(relation.getName());
            relationEntity.setMappingType(relation.getMappingType());
            relationEntity.setCode(relation.getCode());
            relationEntity.setModel(relation.getModel());
            relationEntity.setFields(relation.getFields());
            
            params.add(relationEntity);

            String paramStr= JSONUtil.parseArray(params).toString();
            HttpUtil.post(MyApi.insertRelation.getValue().replace("@token", UserInfo.token),paramStr);
        }
    }

    public static void main(String[] args) {
        upLoadRelation("E:\\test\\测试八个方面1223\\test.relation");
    }
}
