package com.yzh.importTest.importUtils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.dao.EField;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.FileTools;
import onegis.psde.attribute.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.hutool.core.util.ObjectUtil.*;

/**
 * @author Yzh
 * @create 2020-12-24 14:16
 * @details
 */
public class FieldImportUtil {
    //日志工厂
    private static final Logger logger = LoggerFactory.getLogger(FieldImportUtil.class);
    //缓存全局可用
    public static Map<Long,Long> fieldOldIdAndNewIdCache = new HashMap<>();
    public static void fieldImport() throws IOException {
        logger.debug("字段开始导入===========》读取字段文件");
        String fieldsStr = FileTools.readFile("E:\\test\\中原工_yzh\\test.fields");
        List<Field> fieldList = FileTools.jsonArray2List(fieldsStr,Field.class);
        //map用于记录新老Id关系

        List<EField> param = new ArrayList<>();
        //遍历字段，一个一个完成导入上传操作上传
        for (Field field : fieldList) {
            EField eField = new EField();
            //清空请求参数
            param.clear();
            eField.setName(field.getName()+" impotr");
            eField.setDomain(field.getDomain());
            eField.setCaption(field.getCaption());
            eField.setType(String.valueOf(field.getType().getValue()));
            if (isNotEmpty(field.getDefaultValue())&&isNotNull(field.getDefaultValue())){
                eField.setDefaultValue(field.getDefaultValue().toString());
            }
            eField.setDesc(field.getDes());

            param.add(eField);
            //重置请求参数
            String response = HttpUtil.post(MyApi.insertField.getValue().replace("@token", UserInfo.token),JSONUtil.parseArray(param).toString());
            //错误判断
            if (FileTools.judgeImportState(response)){
                logger.error("id:"+field.getId()+"的字段导入失败");
                continue;
            }
            //处理 response
            JSONArray array = FileTools.formatData2JSONArray(response);
            //上传完成将新老Id记录到Map当中
            fieldOldIdAndNewIdCache.put(field.getId(), array.get(0,JSONObject.class).getLong("id"));
            logger.debug("id" +field.getId() + "导入完毕新Id为："+array.get(0,JSONObject.class).getLong("id"));
        }

        System.out.println(fieldsStr);
    }
}
