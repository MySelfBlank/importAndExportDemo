package com.yzh.importTest.importUtils;

import cn.hutool.core.util.ObjectUtil;
import com.yzh.utilts.FileTools;
import onegis.psde.attribute.Field;
import onegis.psde.attribute.Fields;
import onegis.psde.form.FormStyle;
import onegis.psde.form.FormStyles;
import onegis.psde.model.Model;
import onegis.psde.model.ModelDef;
import onegis.psde.model.Models;
import onegis.psde.psdm.OType;
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
        List<OType> eClasses = JsonUtils.jsonToList(oTypesStr, OType.class);
        for (OType eClass : eClasses) {
            if (!eClass.getFields().getFields().equals("[]")&&eClass.getFields().getFields()!=null){
                Fields fields = eClass.getFields();
                List<Field> fieldList = fields.getFields();
                List<Field> newFieldList = handleFieldId(fieldList);
                fields.setFields(newFieldList);
                eClass.setFields(fields);
            }
            if (!eClass.getFormStyles().getStyles().equals("[]")&&eClass.getFormStyles().getStyles()!=null){
                FormStyles formStyles=eClass.getFormStyles();
                List<FormStyle> formStyleList=eClass.getFormStyles().getStyles();
                List<FormStyle> newFormStyles = handleFormStylesId(formStyleList);
                formStyles.setStyles(newFormStyles);
                eClass.setFormStyles(formStyles);
            }
            //连接关系处理为空
            if (!eClass.getModels().getModels().equals("[]")&&eClass.getModels().getModels()!=null){
                Models models =eClass.getModels();
                List<Model> modelList=eClass.getModels().getModels();
                List<Model> newModels = handleModelId(modelList);
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
    public static List<Field> handleFieldId(List<Field> fieldList){
        List<Field> newFieldList=new ArrayList<>();
        for (Field field : fieldList) {
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
    public static List<FormStyle> handleFormStylesId(List<FormStyle> styles){
        List<FormStyle> newFormStyles = new ArrayList<>();
        for (FormStyle style : styles) {
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
    public static List<Model> handleModelId(List<Model> models){
        List<Model> newModels = new ArrayList<>();
        for (Model model : models) {
            ModelDef newModelDef = ObjectUtil.clone(model.getMdef());
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
    public static void main(String[] args) throws Exception{
        importOTpye();
    }
}
