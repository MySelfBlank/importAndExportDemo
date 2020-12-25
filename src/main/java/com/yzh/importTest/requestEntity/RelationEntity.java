package com.yzh.importTest.requestEntity;

import onegis.psde.attribute.Fields;
import onegis.psde.dictionary.RelationEnum;
import onegis.psde.model.Model;
import onegis.psde.psdm.AObject;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/25 14:50
 */
public class RelationEntity extends AObject {
    private RelationEnum mappingType;
    private Fields fields;
    public Model model;
    private String code;
    public RelationEntity() {
        this.mappingType = RelationEnum.ONETOONE;
        this.fields = new Fields();
    }

    public RelationEnum getMappingType() {
        return mappingType;
    }

    public void setMappingType(RelationEnum mappingType) {
        this.mappingType = mappingType;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
