package com.yzh.api;

import static com.yzh.utilts.EnvironmentSelectTool.dev;

/**
 * @author Yzh
 * @create 2020-12-03 14:30
 */
public enum MyApi {
    //登录 api
    login("login", "http://bt1.geosts.ac.cn/api/uc"+dev+"/api/v2/account/login"),

    //userInfo
    getUserInfo("getUserInfo", "http://bt1.geosts.ac.cn/api/uc"+dev+"/api/v2/account/authorize"),

    //获取时空域 api
    getDomain("getDomain", "http://bt1.geosts.ac.cn/api/dae"+dev+"/datastore/rest/v0.1.0/datastore/sdomain/query?token=@token&names=@names"),

    getDomain1("getDomain", "http://bt1.geosts.ac.cn/api/dae"+dev+"/datastore/rest/v0.1.0/datastore/sdomain/query"),

    //获取类模板信息根据ID
    getOtypesByIds("getOtypesByIds", "http://bt1.geosts.ac.cn/api/dae"+dev+"/datastore/rest/v0.1.0/datastore/otype/query"),

    //获取时空域下的所有的空间对象
    getObject("getObject", "http://bt1.geosts.ac.cn/api/dae"+dev+"/datastore/rest/v0.1.0/datastore/object/query"),

    //获取时空域下的连接关系 参数信息传入时空域 sdomian
    getNetWork("", "http://bt1.geosts.ac.cn/api/dae"+dev+"/rest/v0.1.0/datastore/getRelationCatalog"),

    //获取时空域下的连接关系
    getRelationById("getRelation", "http://bt1.geosts.ac.cn/api/dae"+dev+"/datastore/rest/v0.1.0/datastore/orelation/query"),

    //获取字段
    getFieldByFid("getFieldByFid", "http://bt1.geosts.ac.cn/api/dae"+dev+"/datastore/rest/v0.1.0/datastore/ofield/query"),

    getStyleById("", "http://bt1.geosts.ac.cn/api/dae"+dev+"/datastore/rest/v0.1.0/datastore/oformstyle/query"),

    //获取行为属性传入id
    getModelById("getModelById", "http://bt1.geosts.ac.cn/api/dae"+dev+"/datastore/rest/v0.1.0/datastore/model/query"),

    //导入字段 Post (允许批量插入)
    insertField("insertField","http://bt1.geosts.ac.cn/api/dae"+dev+"/datastore/rest/v0.1.0/datastore/ofield/insert?token=@token"),

    //导入形态样式 Post (允许批量插入)
    insertFormStyle("insertFormStyle","http://bt1.geosts.ac.cn/api/dae"+dev+"/datastore/rest/v0.1.0/datastore/oformstyle/insert?token=@token"),

    //获取行为类别传入id
    getModelDefById("getModelDefById","http://bt1.geosts.ac.cn/api/dae"+dev+"/datastore/rest/v0.1.0/datastore/modeldef/query"),

    //行为脚本的下载
    getModelScript("getModelScript","http://bt1.geosts.ac.cn/api/dae"+dev+"/hdfs-service/hdfs/rest/v0.1.0/datastore/slave/hdfs/download"),
    //注销本次登录
    logout("logout", "http://bt1.geosts.ac.cn/api/uc"+dev+"/api/v2/account/logout");


    private final String name;

    private final String value;

    MyApi(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
