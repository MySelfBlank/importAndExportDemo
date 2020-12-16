package com.yzh.utilts;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.yzh.Index;
import com.yzh.api.MyApi;
import com.yzh.userInfo.UserInfo;
import onegis.psde.psdm.OType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.yzh.utilts.FileTools.exportFile;
import static com.yzh.utilts.FileTools.formatData;

/**
 * @author Yzh
 * @create 2020-12-16 17:08
 */
public class OtypeUtilts {
    private static final Logger logger = LoggerFactory.getLogger(OtypeUtilts.class);
    static Map<String,Object> params = new HashMap<>();
    public static void getOtype (){
        params.put("sdomains", UserInfo.domain);
        String objectJsonStr = HttpUtil.get(MyApi.getObject.getValue(), params);
        JSONObject data = formatData(objectJsonStr);

        String objectListStr = data.getStr("list");
        List<JSONObject> objectList = JSONArray.parseArray(objectListStr, JSONObject.class);
        //获取当前时空域下的所有类模板Id
        Set<String> otypeIds = new HashSet<>();

        for (JSONObject o : objectList) {
            JSONObject otype = (JSONObject) o.get("otype");
            otypeIds.add(otype.getStr("id"));
        }
        logger.debug("当前时空域下所有的类模板Id=" + otypeIds);

        params.clear();
        params.put("token", UserInfo.token);
        params.put("ids", otypeIds.toArray());
        String otypeInfoStr = HttpUtil.get(MyApi.getOtypesByIds.getValue(), params);
        JSONObject otypeInfoJson = formatData(otypeInfoStr);
        List<JSONObject> oTypesJsonList = JSONArray.parseArray(otypeInfoJson.getStr("list"), JSONObject.class);

        //类模板集合
        List<OType> oTypeList = new ArrayList<>();
        oTypesJsonList.forEach(v -> {
            //System.out.println(v.toBean(OType.class).getName());
            oTypeList.add(v.toBean(OType.class));
        });

        exportFile(new JSONObject(),"E:/"+Index.sDomain.getName()+"/test.classes");
    }
}
