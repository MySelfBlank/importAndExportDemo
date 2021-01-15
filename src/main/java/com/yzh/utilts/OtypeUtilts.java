package com.yzh.utilts;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.yzh.api.MyApi;
import com.yzh.dao.EClassesOutPutModel;
import com.yzh.dao.EField;
import com.yzh.dao.EModel;
import com.yzh.dao.EModelDef;
import com.yzh.dao.exportModel.*;
import com.yzh.userInfo.UserInfo;
import onegis.psde.attribute.Field;
import onegis.psde.attribute.Fields;
import onegis.psde.form.FormStyle;
import onegis.psde.form.FormStyles;
import onegis.psde.model.Model;
import onegis.psde.model.ModelDef;
import onegis.psde.model.Models;
import onegis.psde.psdm.DObject;
import onegis.psde.psdm.OType;
import onegis.psde.psdm.SObject;
import onegis.psde.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static cn.hutool.core.util.ObjectUtil.*;
import static com.yzh.Index.*;
import static com.yzh.utilts.ConnectorUtils.dsConnectors2EConnectors;
import static com.yzh.utilts.EDObjectUTil.getDObjectList;
import static com.yzh.utilts.FieldUtils.dsField2Field;
import static com.yzh.utilts.FileTools.exportFile;
import static com.yzh.utilts.FileTools.formatData;
import static com.yzh.utilts.FormStyleUtils.forms2EForms;

/**
 * @author Yzh
 * @create 2020-12-16 17:08
 */
public class OtypeUtilts {
    private static Set<Long> classIDs = new HashSet<>();
    private static final Logger logger = LoggerFactory.getLogger(OtypeUtilts.class);
    static Map<String, Object> params = new HashMap<>();
    static List<JSONObject> jsonObjects = new ArrayList<>();

    public static void getOtype() throws Exception {
        params.put("sdomains", UserInfo.domain);
        params.put("loadForm", true);
        params.put("loadDynamicData", false);
        params.put("loadModel", true);
        params.put("loadNetwork", true);
        params.put("loadObjType", true);
        params.put("loadDes",true);
        params.put("loadAction", true);
        params.put("loadChildren", true);
        params.put("loadVersion",true);
        params.put("orderType","VID");
        params.put("descOrAsc", false);
        String objectJsonStr = HttpUtil.get(MyApi.getObject.getValue(), params);
        JSONObject data = formatData(objectJsonStr);

        List<JSONObject> objectList =null;
        String objectListStr = data.getStr("list");
        List<SObject> sObjects = JsonUtils.jsonToList(objectListStr, SObject.class);
        sObjectsList.addAll(sObjects);
        objectList = JSONArray.parseArray(objectListStr, JSONObject.class);

        //获取数据对象
        getDObjectList(sObjects);
        //处理SObject

        //获取当前时空域下的所有类模板Id

        //获取当前时空域下的所有类模板Id
        for (JSONObject o : objectList) {
            JSONObject otype = (JSONObject) o.get("otype");
            classIDs.add(otype.getLong("id"));
        }
        logger.debug("当前时空域下所有的类模板Id=" + classIDs);
        EConnectorUtils.EConnectorHandel(classIDs);
        params.clear();
        params.put("token", UserInfo.token);
        params.put("ids", classIDs.toArray());
        String otypeInfoStr = HttpUtil.get(MyApi.getOtypesByIds.getValue(), params);
        JSONObject otypeInfoJson = formatData(otypeInfoStr);
        //传递过去对象集合对行为类进行处理
        jsonObjects.addAll(JsonUtils.jsonToList(otypeInfoJson.getStr("list"),JSONObject.class));
//        EModelUtil.getEModel(jsonObjects);

        oTypeList.addAll(JsonUtils.jsonToList(otypeInfoJson.getStr("list"),OType.class));
        //处理类模板(暂不处理，直接导出)
        List<EOType> eoTypes = handleOType2EOType(oTypeList);

        //打印类模板
        JSON parse = JSONUtil.parse(eoTypes);
        exportFile(parse, "E:\\test\\" + sDomain.getName() + "\\test.otype","Otype");
    }

    public static void filterOtype(List<OType> oTypeList) throws Exception {
        List<EClassesOutPutModel> eClassesOutPutModelList = new ArrayList<>();
        for (OType oType : oTypeList) {
            eClassesOutPutModelList.add(otype2Class(oType));
        }
    }

    public static EClassesOutPutModel otype2Class(OType oType) throws Exception {
        EClassesOutPutModel model = new EClassesOutPutModel();
        model.setId(oType.getId());
        model.setName(oType.getName());
        //处理关系
        model.setConnectors(dsConnectors2EConnectors(oType.getConnectors(), classIDs));
        //处理字段
        model.setFields(dsField2Field(oType.getFields()));
        //处理形态
        model.setForms(forms2EForms(oType.getFormStyles()));
        //处理行为

        model.setDesc(oType.getDes());
        model.setSrs("epsg:4326");
        model.setTrs("onegis:1001");
        return model;
    }

    public static List<ESObject> sobject2ESObject (List<SObject> sObjectList){
        List<ESObject> esobject = new ArrayList<>();
        if(isEmpty(sObjectList)||isNull(sObjectList)){
            return esobject;
        }
        return esobject;
    }
    public static List<EOType> handleOType2EOType (List<OType> oTypes) {
        List<EOType> eoTypes= new ArrayList<>();
        for (OType oType : oTypes) {
            EOType eoType = new EOType();
            eoType.setId(oType.getId());
            eoType.setName(oType.getName());
            eoType.setDes(oType.getDes());
            eoType.setCode(oType.getCode());
            eoType.setTags(oType.getTags());
            eoType.setIcon(oType.getIcon());
            //处理字段
            eoType.setFields(handleFields(oType.getFields()));
            eoType.setSrs(oType.getSrs());
            eoType.setTrs(oType.getTrs());
            //处理形态样式
            List<EFormStyles> eFormStyles = handleEFormStyle(oType.getFormStyles());
            EFormStyless eFormStyless = new EFormStyless();
            eFormStyless.setStyles(eFormStyles);
            eoType.setFormStyles(eFormStyless);
            //处理行为
            List<EModel> eModels = handleEModel(oType.getModels());
            EModels models = new EModels();
            models.setModels(eModels);
            eoType.setModels(models);
            eoTypes.add(eoType);
        }
        return eoTypes;
    }
    private static List<EFormStyles> handleEFormStyle(FormStyles formStyles){
        List<EFormStyles> eFormStyles = new ArrayList<>();
        if(isNull(formStyles)||isEmpty(formStyles)){
            return eFormStyles;
        }
        List<FormStyle> styles = formStyles.getStyles();
        for (FormStyle style : styles) {
            EFormStyles eFormStyle = new EFormStyles();
            eFormStyle.setId(style.getId());
            eFormStyle.setName(style.getName());
            eFormStyle.setData(style.getData());
            eFormStyle.setDes(style.getDes());
            eFormStyle.setDim(style.getDim());
            eFormStyle.setMaxGrain(style.getMaxGrain());
            eFormStyle.setMinGrain(style.getMinGrain());
            eFormStyle.setStyle(Long.parseLong(String.valueOf(style.getStyle().getValue())));
            eFormStyle.setType(Long.parseLong(String.valueOf(style.getType().getValue())));
            eFormStyles.add(eFormStyle);
        }
        return eFormStyles;
    }
    private static List<EModel> handleEModel (Models models){
        List<EModel> eModels = new ArrayList<>();
        if(isEmpty(models)||isNull(models)){
            return eModels;
        }
        List<Model> modelS = models.getModels();
        for (Model model : modelS) {
            EModel eModel = new EModel();
            eModel.setId(model.getId());
            eModel.setName(model.getName());
            //处理mdef
            eModel.setMdef(handleModel(model.getMdef()));
//            eModel.setpLanguage(model.getpLanguage().getName());
            eModel.setExecutor(model.getExecutor());
            eModels.add(eModel);
        }
        return eModels;
    }
    public static EModelDef handleModel(ModelDef modelDef){
        EModelDef eModelDef = new EModelDef();
        eModelDef.setId(modelDef.getId());
        eModelDef.setName(modelDef.getName());
        eModelDef.setActions(modelDef.getActions());
        eModelDef.setMtime(modelDef.getMtime());
        eModelDef.setInTypes(modelDef.getInTypes());
        eModelDef.setOutTypes(modelDef.getOutTypes());
        eModelDef.setIcon(modelDef.getIcon());
        eModelDef.setDes(modelDef.getDes());
        eModelDef.setType(modelDef.getType().getValue());
        return eModelDef;
    }
    public static EFields handleFields(Fields fields) {
        EFields efields = new EFields();
        if (isNull(fields)||isEmpty(fields)){
            return efields;
        }
        List<EField> efieldList = new ArrayList<>();
        List<Field> fieldList = fields.getFields();
        for (Field field : fieldList) {
            EField efield = new EField();
            efield.setId(field.getId());
            efield.setName(field.getName());
            efieldList.add(efield);
        }
        efields.setFields(efieldList);
        return efields;
    }
}
