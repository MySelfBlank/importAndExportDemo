package com.yzh.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/17 9:15
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EVersion {
    private Long vtime;

    private EBase base;

    /**
     * 行为集合
     */
    private List<EAction> actions = new ArrayList<>();

    /**
     * 增量 -- 属性集合
     */
    private List<EAttribute> attributes = new ArrayList<>();

    /**
     * 增量 -- 组成结构集合
     */
    private List<ECompose> composes = new ArrayList<>();

    /**
     * 增量 -- 形态集合
     */
    private List<EForm> forms = new ArrayList<>();

    /**
     * 增量 -- 行为集合
     */
    private List<EModel> models = new ArrayList<>();

    /**
     * 增量 -- 关系集合
     */
    private ENetWork network;

    public void addAction(EAction eAction){
        this.actions.add(eAction);
    }

    public void addForm(EForm eForm){
        this.forms.add(eForm);
    }

    public void addAttr(EAttribute eAttribute){
        this.attributes.add(eAttribute);
    }

    public void addModel(EModel eModel){
        this.models.add(eModel);
    }

    public void addCompose(ECompose eCompose){
        this.composes.add(eCompose);
    }
}
