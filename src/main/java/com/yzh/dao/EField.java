package com.yzh.dao;

import java.util.Map;

/**
 * @author Yzh
 * @create 2020-12-16 18:10
 */
public class EField extends AbstractObject {
    /**
     * 字段中文名
     */
    private String caption;

    /**
     * 字段描述
     */
    private String desc;

    /**
     * 字段数据类型
     */
    private String type;

    /**
     * 字段值域
     */

    private Map<String, Object> domain;

    /**
     * 字段值域
     */
//    private String domain;

    /**
     * 字段默认值
     */
    private String defaultValue;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getDomainMap() {
        return domain;
    }

    public void setDomainMap(Map<String, Object> domainMap) {
        this.domain = domainMap;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
