package com.yzh.utilts;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.yzh.api.MyApi;
import com.yzh.userInfo.UserInfo;
import onegis.psde.attribute.Field;
import onegis.psde.form.Form;
import onegis.psde.psdm.SDomain;
import onegis.psde.psdm.SObject;
import onegis.psde.util.JsonUtils;

import java.util.*;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNull;

/**
 * @author Yzh
 * @create 2020-12-18 17:03
 * @details
 */
public class FormUtils {
    public static List<Form> objectFromsHandle(List<SObject> sObjects) throws Exception {
        List<Form> fromList = new ArrayList<>();
        if (isNull(sObjects)||isEmpty(sObjects)){
            return  fromList;
        }
        List<Form> forms = new ArrayList<>();
        for (SObject sObject : sObjects) {
            if (isEmpty(sObject.getForms())||isNull(sObject.getForms())){
                continue;
            }else {
                forms.addAll(sObject.getForms().getForms());
            }
        }
        if (isNull(forms)||isEmpty(forms)) {
            return fromList;
        }
        Set<Long> fids = new HashSet<>();
        for (Form form : forms) {
            fids.add(form.getFid());
        }
        //通过fId查询字段信息
        Map<String, Object> params = MapUtil.builder(new HashMap<String, Object>())
                .put("token", UserInfo.token)
                .put("orderType", "ID")
                .put("descOrAsc", true)
                .put("ids", fids.toArray())
                .build();
        String formJsonStr = HttpUtil.get(MyApi.getFieldByFid.getValue(), params);
        JSONObject formJsonObj = FileTools.formatData(formJsonStr);
        List<JSONObject> fieldJsonObjList = JSONArray.parseArray(formJsonObj.get("list").toString(), JSONObject.class);
        fromList.addAll( JsonUtils.jsonToList(formJsonObj.get("list").toString(), Form.class));

        return  fromList;
    }
}
