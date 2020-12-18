package com.yzh.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/18 10:05
 */
public class EDObject extends AbstractObject{

    /**
     * 产生数据对应ot
     */
    @JsonProperty("dType")
    private Long dType;

    /**
     * 产生数据源
     */
    private Long dataSource;

    /**
     * 写出文件名
     */
    private String data;

    public Long getdType() {
        return dType;
    }

    public void setdType(Long dType) {
        this.dType = dType;
    }

    public Long getDataSource() {
        return dataSource;
    }

    public void setDataSource(Long dataSource) {
        this.dataSource = dataSource;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
