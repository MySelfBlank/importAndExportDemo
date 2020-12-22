package com.yzh.importTest;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.utilts.FileTools;
import onegis.psde.attribute.Field;

import java.lang.reflect.Type;
import java.util.List;


/**
 * @author Yzh
 * @create 2020-12-22 14:28
 * @details
 */
public class ImportMain {
    public static void main(String[] args) throws Exception {
        //读取导出的字段信息
        String fieldsStr = FileTools.readFile("E:\\test\\中原工_yzh\\test.fields");
        List<Field> fieldList = FileTools.jsonArray2List(fieldsStr,Field.class);
        System.out.println(fieldsStr);
    }


    /*
    类模板对字段，形态，样式属于引用关系
    需要先导入 字段，样式，形态信息
    如遇到Id重复需要重置Id
    重置前后的Id可用Map保存，方便修改Otype中的引用关系
    */
}
