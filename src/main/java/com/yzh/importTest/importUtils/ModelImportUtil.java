package com.yzh.importTest.importUtils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.dao.EField;
import com.yzh.dao.EModel;
import com.yzh.importTest.requestEntity.ModelDefEntity;
import com.yzh.importTest.requestEntity.ModelEntity;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.FileTools;
import onegis.psde.model.ModelDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.yzh.importTest.importUtils.IdCache.fieldOldIdAndNewIdCache;
import static com.yzh.importTest.importUtils.IdCache.modelDefNewIdAndOldId;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2021/1/12 14:53
 */
public class ModelImportUtil {
    //日志工厂
    private static final Logger logger = LoggerFactory.getLogger(ModelImportUtil.class);

    /**
     * 对行为的导入
     * @param url
     */
    public static void modelImportHandle(String url){
        String modelStr = FileTools.readFile(url);
        List<EModel> models = FileTools.jsonArray2List(modelStr, EModel.class);

        List<ModelEntity> modelEntities = new ArrayList<>();
        for (EModel eModel : models) {
            ModelEntity modelEntity = new ModelEntity();
            modelEntity.setName(eModel.getName());
            JSONObject jsonObject = JSONUtil.parseObj(eModel.getMdef());
            ModelDefEntity modelDefEntity = jsonObject.toBean(ModelDefEntity.class);
            modelDefEntity.setId(modelDefNewIdAndOldId.get(modelDefEntity.getId()));
            ModelDef newModelDef = JSONUtil.parse(modelDefEntity).toBean(ModelDef.class);
            modelEntity.setMdef(newModelDef);
            if (!eModel.getMobj().getScript().equals("")||!eModel.getMobj().getScript().isEmpty()){

            }
        }

    }

    /**
     * 对行为类别进行导入
     * @param url
     */
    public static void modelDefImportHandle(String url){
        String modelDefStr = FileTools.readFile(url);
        List<ModelDef> modelDefs = FileTools.jsonArray2List(modelDefStr, ModelDef.class);

        List<ModelDefEntity> entities =new ArrayList<>();
        for (ModelDef modelDef : modelDefs) {
            logger.debug("行为类别开始导入======》文件对象转换");
            ModelDefEntity modelDefEntity = new ModelDefEntity();
            if (modelDef.getIcon().equals("")){
                modelDefEntity.setIcon(modelDef.getIcon());
            }
            modelDefEntity.setName(modelDef.getName());
            modelDefEntity.setType(modelDef.getType().getValue());
            //对输入类型id进行处理
            if (modelDef.getInTypes().equals("[]")||modelDef.getInTypes().isEmpty()) {
                modelDefEntity.setInTypes(modelDef.getInTypes());
            }else{
                JSONArray array = JSONUtil.parseArray(modelDef.getInTypes());
                List<EField> foreInFields = array.toList(EField.class);
                List<EField> eFields = new ArrayList<>();
                for (int i = 0; i <foreInFields.size(); i++) {
                    EField eField = ObjectUtil.clone(foreInFields.get(i));
                    eField.setId(fieldOldIdAndNewIdCache.get(foreInFields.get(i).getId()));
                    eFields.add(eField);
                }
                String newModelDef = JSONUtil.parse(eFields).toString();
                modelDefEntity.setInTypes(newModelDef);
            }
            //对输出类型id进行处理
            if (modelDef.getOutTypes().isEmpty()||modelDef.getOutTypes().equals("[]")) {
                modelDefEntity.setOutTypes(modelDef.getOutTypes());
            }else {
                JSONArray array = JSONUtil.parseArray(modelDef.getOutTypes());
                List<EField> foreFields = array.toList(EField.class);
                List<EField> eFields = new ArrayList<>();
                for (int i = 0; i <foreFields.size(); i++) {
                    EField eField = ObjectUtil.clone(foreFields.get(i));
                    eField.setId(fieldOldIdAndNewIdCache.get(foreFields.get(i).getId()));
                    eFields.add(eField);
                }
                String newModelDef = JSONUtil.parse(eFields).toString();
                modelDefEntity.setOutTypes(newModelDef);
            }
            modelDefEntity.setDes(modelDef.getDes());
            modelDefEntity.setTags(modelDef.getTags());

            entities.add(modelDefEntity);
            //处理响应的数据
            String paramStr = JSONUtil.parseArray(entities).toString();
            String response = HttpUtil.post(MyApi.insertModelDef.getValue()+"?token="+ UserInfo.token, paramStr);
            if (FileTools.judgeImportState(response)) {
                logger.error("name为" + modelDef.getName() + "的关系导入失败");
                continue;
            }
            //对新老的id进行处理
            JSONArray arrays = FileTools.formatData2JSONArray(response);

            modelDefNewIdAndOldId.put(modelDef.getId(), arrays.get(0, JSONObject.class).getLong("id"));
            logger.debug("id" + modelDef.getId() + "新id为" + arrays.get(0, JSONObject.class).getLong("id"));
        }

    }

    public static void main(String[] args) {
        modelDefImportHandle("E:\\test\\测试八个方面1223\\test.modelDef");
        modelImportHandle("E:\\test\\测试八个方面1223\\test.models");
    }
}
