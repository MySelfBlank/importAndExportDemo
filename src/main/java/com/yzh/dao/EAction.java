package com.yzh.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/17 9:25
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EAction {
    private Long id;
    private EActionEvent operation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EActionEvent getOperation() {
        return operation;
    }

    public void setOperation(EActionEvent operation) {
        this.operation = operation;
    }
}
