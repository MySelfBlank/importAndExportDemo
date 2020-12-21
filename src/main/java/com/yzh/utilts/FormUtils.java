package com.yzh.utilts;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.yzh.api.MyApi;
import com.yzh.dao.EForm;
import com.yzh.dao.EFormRef;
import com.yzh.userInfo.UserInfo;
import onegis.psde.dictionary.FormEnum;
import onegis.psde.form.AForm;
import onegis.psde.form.Form;
import onegis.psde.form.FormStyle;
import onegis.psde.form.ModelBlock;
import onegis.psde.psdm.SObject;
import onegis.psde.util.JsonUtils;
import org.springframework.util.StringUtils;

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
        if (isNull(sObjects) || isEmpty(sObjects)) {
            return fromList;
        }
        List<Form> forms = new ArrayList<>();
        for (SObject sObject : sObjects) {
            if (isEmpty(sObject.getForms()) || isNull(sObject.getForms())) {
                continue;
            } else {
                forms.addAll(sObject.getForms().getForms());
            }
        }
        if (isNull(forms) || isEmpty(forms)) {
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
        fromList.addAll(JsonUtils.jsonToList(formJsonObj.get("list").toString(), Form.class));

        return fromList;
    }

    public static List<FormStyle> objectFromsHandle2(List<Form> fromList) throws Exception {
        if (isNull(fromList) || isEmpty(fromList)) {
            return new ArrayList<>();
        }
        Set<String> formList = new HashSet<>();
        StringBuffer buffer = new StringBuffer();
        for (Form form : fromList) {
            //形态不是模型的时候取样式id
            if(!form.getType().getName().equalsIgnoreCase("model")){
                //取形态中的样式Id
                JSONArray jsonArray = JSONArray.parseArray(form.getStyle());
                for (Object o : jsonArray) {
                    buffer.append("," + o);
                }
            }
        }
        //去除第一位多余的，
        buffer.deleteCharAt(0);
        System.out.println(buffer);
        String[] split = buffer.toString().split(",");
        formList.addAll(Arrays.asList(split));
        System.out.println(formList);
        //请求样式数据
        Map<String, Object> params = MapUtil.builder(new HashMap<String, Object>())
                .put("token", UserInfo.token)
                .put("orderType", "ID")
                .put("descOrAsc", true)
                .put("ids", formList.toArray())
                .build();
        String styleJsonStr = HttpUtil.get(MyApi.getStyleById.getValue(), params);
        JSONObject stylejsonObj = FileTools.formatData(styleJsonStr);
        String styleListStr = stylejsonObj.getStr("list");
        if(StringUtils.isEmpty(styleListStr)){
            return new ArrayList<>();
        }
        List<FormStyle> formStyles = JsonUtils.jsonToList(styleListStr, FormStyle.class);
        return formStyles;
    }

    public static List<EForm> dsForms2EForm(List<Form> forms) throws Exception{
        if (isEmpty(forms)||isNull(forms)){
            return new ArrayList<>();
        }
        List<EForm> eForms = new ArrayList<>();
        for (Form form : forms) {
            EForm eForm = dsForm2EForm(form);
            eForms.add(eForm);
        }
        return eForms;
    }

    public static EForm dsForm2EForm(Form form) {
        EForm eForm = new EForm();
        eForm.setId(form.getId());
        eForm.setDim(form.getDim());
        eForm.setType(FormEnum.getEnum(form.getType().getValue()).getName());
        eForm.setMaxGrain(form.getMaxGrain());
        eForm.setMinGrain(form.getMinGrain());
        eForm.setFormRef(dsFormRef2FormRef(form.getFormref()));
        eForm.setStyle(form.getStyle());
        return eForm;
    }

    private static EFormRef dsFormRef2FormRef(AForm aForm){
        if (aForm == null){
            return null;
        }
        EFormRef eFormRef = new EFormRef();
        if (aForm instanceof ModelBlock){
            ModelBlock block = (ModelBlock) aForm;
            eFormRef.setName(block.getName());
            eFormRef.setDesc(block.getDes());
            eFormRef.setExtension(block.getExtension());
            eFormRef.setFname(block.getFname());
        }
        return eFormRef;
    }
}
