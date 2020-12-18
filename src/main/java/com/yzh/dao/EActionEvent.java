package com.yzh.dao;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/17 9:27
 */
public class EActionEvent {
    /**
     * 操作方式【增删改】
     */
    private String actionOperationType;

    /**
     * 对象属性标记【八个方面】
     */
    private String objectOperationType;

    public EActionEvent() {

    }

    public EActionEvent(EActionEnum operationType, EActionEnum objectType) {
        this.actionOperationType = operationType.getName();
        this.objectOperationType = objectType.getName();
    }

    public void createActionOperationType(Integer value){
        this.actionOperationType = EActionEnum.getEnum(value).getName();
    }

    public void createObjectOperationType(Integer value){
        this.objectOperationType = EActionEnum.getEnum(value).getName();
    }
}
