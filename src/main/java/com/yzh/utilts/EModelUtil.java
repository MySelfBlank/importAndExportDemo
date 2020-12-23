package com.yzh.utilts;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.userInfo.UserInfo;
import onegis.psde.model.Model;
import onegis.psde.model.Models;
import onegis.psde.psdm.OType;

import java.util.*;

import static com.yzh.Index.sDomain;
import static com.yzh.utilts.FileTools.exportFile;
import static com.yzh.utilts.FileTools.formatData;

/**
 * 行为的导出
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/22 9:44
 */
public class EModelUtil {

    /**
     * 获取行为id
     * @param modelsList
     */
    public static Set<Long> getModel(List<OType> modelsList){
        if (modelsList==null||modelsList.size()==0){
            return new HashSet<>();
        }
        Set<Long> ids = new HashSet<>();
        for (OType models : modelsList) {
            if (models!=null&&modelsList.size()!=0) {
                Models myModels = models.getModels();
                List<Model> models2 = myModels.getModels();
                if (models2==null){
                    continue;
                }
                for (Model model : models2) {
                    Long id = model.getId();
                    ids.add(id);
                }
            }
        }
        return ids;
    }

    /**
     * 通过行为id获取行为数据
     * @param modelsList
     */
    public static void getModelsFile(List<OType> modelsList) throws Exception {
        Set<Long> ids = getModel(modelsList);
        Map<String, Object> param = new HashMap<>();
        param.put("token", UserInfo.token);
        param.put("ids", ids.toArray());
        param.put("DESCOrAsc","true");
        String relationStr = HttpUtil.get(MyApi.getModelById.getValue(), param);

        JSONObject list = formatData(relationStr);

        String path = "E:\\test\\" + sDomain.getName() + "\\test.models";
        exportFile(JSONUtil.parse(list), path);
    }
}
