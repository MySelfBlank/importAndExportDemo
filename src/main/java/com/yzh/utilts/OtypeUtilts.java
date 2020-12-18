package com.yzh.utilts;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.yzh.api.MyApi;
import com.yzh.dao.EClassesOutPutModel;
import com.yzh.userInfo.UserInfo;
import onegis.psde.psdm.OType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import static com.yzh.utilts.ConnectorUtils.dsConnectors2EConnectors;
import static com.yzh.utilts.FieldUtils.dsField2Field;
import static com.yzh.utilts.FileTools.formatData;

/**
 * @author Yzh
 * @create 2020-12-16 17:08
 */
public class OtypeUtilts {
    private static Set<Long> classIDs = new HashSet<>();
    private static final Logger logger = LoggerFactory.getLogger(OtypeUtilts.class);
    static Map<String,Object> params = new HashMap<>();
    public static void getOtype () throws Exception {
        params.put("sdomains", UserInfo.domain);
        String objectJsonStr = HttpUtil.get(MyApi.getObject.getValue(), params);
        JSONObject data = formatData(objectJsonStr);

        String objectListStr = data.getStr("list");
        List<JSONObject> objectList = JSONArray.parseArray(objectListStr, JSONObject.class);
        for (JSONObject object : objectList) {
            System.out.println(object);
        }
        //获取当前时空域下的所有类模板Id

        for (JSONObject o : objectList) {
            JSONObject otype = (JSONObject) o.get("otype");
            classIDs.add(otype.getLong("id"));
        }
        logger.debug("当前时空域下所有的类模板Id=" + classIDs);

        params.clear();
        params.put("token", UserInfo.token);
        params.put("ids", classIDs.toArray());
        String otypeInfoStr = HttpUtil.get(MyApi.getOtypesByIds.getValue(), params);
        JSONObject otypeInfoJson = formatData(otypeInfoStr);
        List<JSONObject> oTypesJsonList = JSONArray.parseArray(otypeInfoJson.getStr("list"), JSONObject.class);

        //类模板集合
        List<OType> oTypeList = new ArrayList<>();
        oTypesJsonList.forEach(v -> {
            //System.out.println(v.toBean(OType.class).getName());
            oTypeList.add(v.toBean(OType.class));
        });
        filterOtype(oTypeList);
    }
    public static void filterOtype(List <OType> oTypeList) throws Exception {
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
        model.setConnectors(dsConnectors2EConnectors(oType.getConnectors(),classIDs));
        //处理字段
        model.setFields(dsField2Field(oType.getFields()));
        //处理形态

        //处理行为

        model.setDesc(oType.getDes());
        model.setSrs("epsg:4326");
        model.setTrs("onegis:1001");
        return model;
    }
}
