package com.yzh.dao;

import java.util.List;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/22 15:58
 */
public class ERelation extends AbstractObject{
    /**
     * 描述关系的字段
     */
    private List<EField> fields;
    /**
     * 关系映射类型
     */
    private String mappingType;

    /**
     * 规则
     */
    private List rules;

    public List<EField> getFields() {
        return fields;
    }

    public void setFields(List<EField> fields) {
        this.fields = fields;
    }

    public String getMappingType() {
        return mappingType;
    }

    public void setMappingType(String mappingType) {
        this.mappingType = mappingType;
    }

    public List getRules() {
        return rules;
    }

    public void setRules(List rules) {
        this.rules = rules;
    }
}
