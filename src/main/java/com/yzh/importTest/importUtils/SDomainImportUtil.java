package com.yzh.importTest.importUtils;

import cn.hutool.json.JSONUtil;
import com.yzh.utilts.FileTools;
import onegis.psde.attribute.Field;
import onegis.psde.psdm.SObject;
import onegis.psde.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Yzh
 * @create 2021-01-12 11:25
 * @details
 */
public class SDomainImportUtil {
    //日志工厂
    private static final Logger logger = LoggerFactory.getLogger(FieldImportUtil.class);

    public static void importSDomain(){
        //读取文件
        logger.debug("时空域开始导入===========》读取字段文件");
        String fieldsStr = FileTools.readFile("E:\\test\\中原工_yzh\\test.sdomain");
        SObject sObject = JsonUtils.parserBean(fieldsStr, SObject.class);

    }

    public static void main(String[] args) {
        importSDomain();
    }
}
