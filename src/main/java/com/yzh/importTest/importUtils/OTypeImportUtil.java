package com.yzh.importTest.importUtils;

import com.yzh.dao.EClassesOutPutModel;
import com.yzh.utilts.FileTools;
import onegis.psde.psdm.OType;
import onegis.psde.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Yzh
 * @create 2021-01-14 9:21
 * @details
 */
public class OTypeImportUtil {
    //日志工厂
    private static final Logger logger = LoggerFactory.getLogger(OTypeImportUtil.class);

    public static void importOTpye () throws Exception{
        logger.debug("类模板开始导入===========》读取文件");
        String oTypesStr = FileTools.readFile("E:\\test\\测试八个方面1223\\test.otype");
        List<OType> eClasses = JsonUtils.jsonToList(oTypesStr, OType.class);
    }

    public static void main(String[] args) throws Exception{
        importOTpye();
    }
}
