package com.yzh.importTest.importUtils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.dao.EModel;
import com.yzh.dao.ERelation;
import com.yzh.importTest.requestEntity.ModelEntity;
import com.yzh.importTest.requestEntity.RelationEntity;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.FileTools;
import onegis.common.utils.JsonUtils;
import onegis.psde.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.yzh.importTest.importUtils.IdCache.relationNewIdAndOldId;
import static com.yzh.importTest.importUtils.ModelImportUtil.modelImportHandle;
import static com.yzh.utilts.FileTools.login;

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
    public static void upLoadRelation(String url,String fieldIdPath,String modelIdPath) throws Exception {
        String relationStr = FileTools.readFile(url);
        String fieldIdCache = FileTools.readFile(fieldIdPath);
        String modelIdCache = FileTools.readFile(modelIdPath);
        IdCache.fieldOldIdAndNewIdCache=JSONUtil.toBean(fieldIdCache, Map.class);
        IdCache.modelNewIdAndOldId=JSONUtil.toBean(modelIdCache,Map.class);
        //改转换方法
        List<ERelation> relations = JsonUtils.jsonToList(relationStr, ERelation.class);
        for (ERelation relation : relations) {
            logger.debug("关系开始导入==========》读取文件");
            RelationEntity relationEntity = new RelationEntity();
            //将获取到的关系文本内容转换为对象
            relationEntity.setId(relation.getId());
            relationEntity.setName(relation.getName());
            relationEntity.setMappingType(relation.getMappingType());
            relationEntity.setModel(exchangeModel(relation.getModel()));
            if (relation.getFields()==null) {
                relationEntity.setFields(relation.getFields());
            }
            //处理响应的数据
            String paramStr = JSONUtil.parseObj(relationEntity).toString();
            String response = HttpUtil.post(MyApi.insertRelation.getValue()+"?token="+ UserInfo.token,paramStr );
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

    public static ModelEntity exchangeModel(Model model) throws Exception {

        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setpLanguage(model.getpLanguage().getValue());
        if (IdCache.modelNewIdAndOldId.get(model.getId().toString())==null){

        }
        modelEntity.setId(Long.parseLong(String.valueOf(IdCache.modelNewIdAndOldId.get(model.getId().toString()))));
        EModel eModel = modelToEModel(model);
        modelImportHandle(modelEntity,eModel);
        return modelEntity;
    }
     public static EModel modelToEModel(Model model){
        EModel eModel = new EModel();
        eModel.setName(model.getName());
        return eModel;
    }
    public static void main(String[] args) throws Exception {

        login("ceshi@yzh.com", "123456");

        upLoadRelation("E:\\test\\测试八个方面1223\\test.relation","E:\\test\\测试八个方面1223\\fieldId.text","E:\\test\\测试八个方面1223\\modelId.text");
        JSON parse = JSONUtil.parse(relationNewIdAndOldId);
        FileTools.exportFile(parse,"E:\\test\\中原工_yzh","relationId.text");
    }
}
