package com.yzh.importTest.importUtils;

import cn.hutool.core.util.ObjectUtil;
import com.yzh.dao.EField;
import com.yzh.dao.EModel;
import com.yzh.dao.EModelDef;
import com.yzh.dao.exportModel.*;
import com.yzh.utilts.FileTools;
import onegis.psde.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
        List<EOType> eClasses = JsonUtils.jsonToList(oTypesStr, EOType.class);
        for (EOType eClass : eClasses) {
            if (!eClass.getFields().getFields().equals("[]")&&eClass.getFields().getFields()!=null){
                EFields eField=eClass.getFields();
                List<EField> fieldList = eField.getFields();
                List<EField> newFieldList = handleFieldId(fieldList);
                eField.setFields(newFieldList);
                eClass.setFields(eField);
            }
            if (!eClass.getFormStyles().getStyles().equals("[]")&&eClass.getFormStyles().getStyles()!=null){
                EFormStyless formStyles=eClass.getFormStyles();
                List<EFormStyles> formStyleList=eClass.getFormStyles().getStyles();
                List<EFormStyles> newFormStyles = handleFormStylesId(formStyleList);
                formStyles.setStyles(newFormStyles);
                eClass.setFormStyles(formStyles);
            }
            //连接关系处理为空
            if (!eClass.getModels().getModels().equals("[]")&&eClass.getModels().getModels()!=null){
                EModels models =eClass.getModels();
                List<EModel> modelList=eClass.getModels().getModels();
                List<EModel> newModels = handleModelId(modelList);
                models.setModels(newModels);
                eClass.setModels(models);
            }

        }
    }

    /**
     * 将字段的老id替换为新id
     * @param fieldList
     * @return
     */
    public static List<EField> handleFieldId(List<EField> fieldList){
        List<EField> newFieldList=new ArrayList<>();
        for (EField field : fieldList) {
            Long newFiledId = IdCache.fieldOldIdAndNewIdCache.get(field.getId());
            field.setId(newFiledId);
            newFieldList.add(field);
        }
        return newFieldList;
    }

    /**
     * 对样式的id进行替换
     * @param styles
     * @return
     */
    public static List<EFormStyles> handleFormStylesId(List<EFormStyles> styles){
        List<EFormStyles> newFormStyles = new ArrayList<>();
        for (EFormStyles style : styles) {
            Long newStyleId = IdCache.formStylesOidAndNewId.get(style.getId());
            style.setId(newStyleId);
            newFormStyles.add(style);
        }
        return newFormStyles;
    }

    /**
     * 对model和modelDef的id进行替换
     * @param models
     * @return
     */
    public static List<EModel> handleModelId(List<EModel> models){
        List<EModel> newModels = new ArrayList<>();
        for (EModel model : models) {
            EModelDef newModelDef = ObjectUtil.clone(model.getMdef());
            if (model.getMdef()!=null&&!model.getMdef().equals("")){
                Long newModelDefId = IdCache.modelDefNewIdAndOldId.get(model.getMdef().getId());
                newModelDef.setId(newModelDefId);
            }
            model.setMdef(newModelDef);
            Long newModelId = IdCache.modelNewIdAndOldId.get(model.getId());
            model.setId(newModelId);
            newModels.add(model);
        }
        return newModels;
    }
    public static void main(String[] args) throws Exception {
        importOTpye();
    }
}
