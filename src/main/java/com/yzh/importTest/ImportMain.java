package com.yzh.importTest;

import com.yzh.importTest.importUtils.FieldImpotrUtil;
import com.yzh.utilts.FileTools;
import onegis.psde.attribute.Field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yzh.utilts.FileTools.login;


/**
 * @author Yzh
 * @create 2020-12-22 14:28
 * @details
 */
public class ImportMain {
    public static void main(String[] args) throws Exception {
        //用户需要先登录
        System.out.println("请输入您的账号和密码");
        login("asiayu01@163.com", "yu1306730458");
        //字段导入
        FieldImpotrUtil.fieldImport();
        //形态样式导入

        //形态导入
    }


    /*
    类模板对字段，形态，样式属于引用关系
    需要先导入 字段，样式，形态信息
    如遇到Id重复需要重置Id
    重置前后的Id可用Map保存，方便修改Otype中的引用关系
    */

    /*不采用批量导入，一个一个导入  防止个边数据导入报错*/
}
