package com.yzh.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yzh
 * @create 2020-12-16 18:04
 */
public class EClassesOutPutModel extends AbstractObject {
    /**
     * 描述
     */
    private String desc;

    /**
     * 空间参考
     */
    private String srs;

    /**
     * 时间参考
     */
    private String trs;

    /**
     * 字段
     */
    private List<EField> fields = new ArrayList<>();

    /**
     * 形态
     */
    private List<EForm> forms = new ArrayList<>();

    /**
     * 关系
     */
    private List<EConnector> connectors = new ArrayList<>();

    /**
     * 行为
     */
    private List<EModel> models = new ArrayList<>();
}
