package com.yzh.importTest.importUtils;

import com.yzh.utilts.FileTools;
import onegis.psde.form.FormStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yzh
 * @create 2020-12-25 9:23
 * @details
 */
public class FormImportUtil {
    //日志工厂
    private static final Logger logger = LoggerFactory.getLogger(FormImportUtil.class);
    public static Map<Long,Long> formStylesOidAndNewId = new HashMap<>();
    public static void formStyleImportHandle(){
        //读取文件
        logger.debug("形态样式开始导入===========》读取形态样式文件");
        String formStylesStr = FileTools.readFile("E:\\test\\中原工_yzh\\test.formStyles");
        List<FormStyle> formStyles = FileTools.jsonArray2List(formStylesStr, FormStyle.class);
        //过滤掉使用的默认样式collect
        List<FormStyle> formStylesRemoveDef = formStyles.stream().filter(v -> !v.getName().contains("default_")).collect(Collectors.toList());
        for (FormStyle formStyle : formStylesRemoveDef) {

        }
        System.out.println(formStylesStr);
    }
}
