package com.yzh.importTest.importUtils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.importTest.requestEntity.RelationEntity;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.FileTools;
import onegis.psde.relation.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.yzh.importTest.importUtils.IdCache.relationNewIdAndOldId;

;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/25 11:43
 */
public class RelationImportUtil {
    //日志工厂
    private static final Logger logger = LoggerFactory.getLogger(FieldImportUtil.class);


    /**
     * 导入关系,将id保存下来
     *
     * @param url
     */
    public static void upLoadRelation(String url) {
        String relationStr = FileTools.readFile(url);
        List<Relation> relations = FileTools.jsonArray2List(relationStr, Relation.class);

        List<RelationEntity> params = new ArrayList<>();
        for (Relation relation : relations) {
            logger.debug("关系开始导入==========》读取文件");
            RelationEntity relationEntity = new RelationEntity();
            //将获取到的关系文本内容转换为对象
            params.clear();
            relationEntity.setName(relation.getName());
            relationEntity.setMappingType(relation.getMappingType());
            relationEntity.setCode(relation.getCode());
            relationEntity.setModel(relation.getModel());
            relationEntity.setFields(relation.getFields());

            params.add(relationEntity);
            //处理响应的数据
            String paramStr = JSONUtil.parseArray(params).toString();
            String response = HttpUtil.post(MyApi.insertRelation.getValue().replace("@token", UserInfo.token), paramStr);
            if (FileTools.judgeImportState(response)) {
                logger.error("name为" + relation.getName() + "的关系导入失败");
                continue;
            }
            //对新老的id进行处理
            JSONArray arrays = FileTools.formatData2JSONArray(response);

            relationNewIdAndOldId.put(relation.getId(), arrays.get(0, JSONObject.class).getLong("id"));
            logger.info("id" + relation.getId() + "新id为" + arrays.get(0, JSONObject.class).getLong("id"));
        }
    }

    public static void main(String[] args) {

        upLoadRelation("E:\\test\\测试八个方面1223\\test.relation");
        JSON parse = JSONUtil.parse(relationNewIdAndOldId);
        FileTools.exportFile(parse,"E:\\test\\中原工_yzh","relationId.text");
    }
}
