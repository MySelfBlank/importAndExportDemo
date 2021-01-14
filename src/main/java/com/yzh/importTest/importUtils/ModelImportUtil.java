package com.yzh.importTest.importUtils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.yzh.api.MyApi;
import com.yzh.dao.EField;
import com.yzh.dao.EModel;
import com.yzh.importTest.requestEntity.Model;
import com.yzh.importTest.requestEntity.ModelDefEntity;
import com.yzh.importTest.requestEntity.ModelEntity;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.FileTools;
import com.yzh.utilts.HttpClientUtils;
import onegis.common.utils.JsonUtils;
import onegis.psde.model.ModelDef;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import static com.yzh.importTest.importUtils.IdCache.*;
import static com.yzh.utilts.FileTools.login;

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
    public static void modelImportHandle(String url,String modelUrl) throws Exception{
        logger.debug("模型开始导入=====》读取文件");
        String modelStr = FileTools.readFile(url);
        List<EModel> models = JsonUtils.jsonToList(modelStr, EModel.class);

        List<ModelEntity> modelEntities = new ArrayList<>();
        for (EModel model : models) {
            ModelEntity modelEntity = new ModelEntity();
            modelEntity.setName(model.getName());
            JSONObject jsonObject = JSONUtil.parseObj(model.getMdef());
            ModelDefEntity modelDefEntity = jsonObject.toBean(ModelDefEntity.class);
            modelDefEntity.setId(modelDefNewIdAndOldId.get(modelDefEntity.getId()));
            ModelDef newModelDef = JSONUtil.parse(modelDefEntity).toBean(ModelDef.class);
            modelEntity.setMdef(newModelDef);
            modelEntity.setpLanguage(Integer.valueOf(model.getpLanguage()));
            if (model.getpLanguage().equals("1")||model.getpLanguage().equals("2")){
                if (!model.getMobj().getScript().isEmpty()){
                    uploadModles(modelUrl);
                    modelEntity.setMobj(model.getMobj());
                }
            }else {
                String[] mobj = new String[]{};
                modelEntity.setMobj(null);
            }
            modelEntities.add(modelEntity);

            //处理响应的数据
            String paramStr = JSONUtil.parseArray(modelEntities).toString();
            paramStr=paramStr.replaceAll("\\\\","");
            String response = HttpUtil.post(MyApi.insertModel.getValue()+"?token="+ UserInfo.token, paramStr);
            if (FileTools.judgeImportState(response)) {
                logger.error("name为" + model.getName() + "的关系导入失败");
                modelEntities.clear();
                continue;
            }
            //对新老的id进行处理
            JSONArray arrays = FileTools.formatData2JSONArray(response);

            modelNewIdAndOldId.put(model.getId(), arrays.get(0, JSONObject.class).getLong("id"));
            logger.debug("id" + model.getId() + "新id为" + arrays.get(0, JSONObject.class).getLong("id"));
        }

    }

    /**
     * 上传模型
     * @param directoryPath
     * @return
     * @throws Exception
     */
    private static Map<String, String> uploadModles(String directoryPath) throws Exception{
        Map<String, String> resultMap = new HashMap<>();
        // 读取该路径下所有文件
        List<File> fileList = new ArrayList<>();
        fileList=FileTools.getFiles(directoryPath);
        // 筛选出模型文件，包括后缀名.gltf .glb  .ive  .osgb
        String suffixArray[] = {"gltf", "glb", "ive", "osgb", "GLTF", "GLB", "IVE", "OSGB", "JSON", "json"};
        List<String> suffixList = Arrays.asList(suffixArray);
        // 遍历文件，并上传
        for(int i=0; i<fileList.size(); i++){
            File file = fileList.get(i);
            String fileName = file.getName();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            // 只上传模型文件
            if (suffixList.contains(suffix)) {
                Model model = uploadModel(file);
                if (model!=null){
                    resultMap.put(model.getFname(), model.getFid());
                }
            }

        }
        return resultMap;
    }

    /**
     * 通过接口上传模型
     * @param file
     * @return
     * @throws Exception
     */
    public static Model uploadModel(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        Map<String,String> params = new HashMap<>();
        params.put("token",UserInfo.token);
        params.put("name",file.getName().substring(0, file.getName().lastIndexOf(".")));
        Map<String, MultipartFile> multipartFileMap = new HashMap<>();
        multipartFileMap.put("file", multipartFile);
        String result = HttpClientUtils.doPostByte(MyApi.uploadModel.getValue(), params, multipartFileMap);
        onegis.result.response.ResponseResult responseResult = JsonUtils.jsonToPojo(result, onegis.result.response.ResponseResult.class);
        if (responseResult.getStatus() == 200) {
            Model model = JSON.parseObject(JSON.toJSONString(responseResult.getData()), Model.class);
            System.out.println("模型" +  model.getFname() +"保存成功，fid：" + model.getFid());
            return model;
        }else {
            System.out.println("模型" +  file.getName() +"保存失败" );
        }
        return null;
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
            logger.info("id" + modelDef.getId() + "新id为" + arrays.get(0, JSONObject.class).getLong("id"));
            entities.clear();
        }

    }

    public static void main(String[] args) throws Exception {
        login("asiayu01@163.com", "yu1306730458");
        modelImportHandle("E:\\test\\测试八个方面1223\\test.models","E:\\test\\测试八个方面1223\\ModelFile");
        cn.hutool.json.JSON parse = JSONUtil.parse(modelNewIdAndOldId);
        cn.hutool.json.JSON parseDef = JSONUtil.parse(modelDefNewIdAndOldId);
        FileTools.exportFile(parse,"E:\\test\\中原工_yzh","modelId.text");
        FileTools.exportFile(parseDef,"E:\\test\\中原工_yzh","modelDefId.text");
    }
}
