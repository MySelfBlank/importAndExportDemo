package com.yzh.dao;

import com.yzh.dao.exportModel.AbstractObject;
import onegis.psde.attribute.Fields;
import onegis.psde.model.Model;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/22 15:58
 */
public class ERelation extends AbstractObject {
    /**
     * 描述关系的字段
     */
    private Fields fields;
    /**
     * 关系映射类型
     */
    private int mappingType;

    /**
     * 规则
     */
    public Model model;

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public Integer getMappingType() {
        return mappingType;
    }

    public void setMappingType(Integer mappingType) {
        this.mappingType = mappingType;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
