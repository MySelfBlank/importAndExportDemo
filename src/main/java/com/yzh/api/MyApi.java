package com.yzh.api;

/**
 * @author Yzh
 * @create 2020-12-03 14:30
 */
public enum MyApi {
    //登录 api
    login("login","http://bt1.geosts.ac.cn/api/uc-dev/api/v2/account/login"),

    //userInfo
    getUserInfo("getUserInfo","http://bt1.geosts.ac.cn/api/uc-dev/api/v2/account/authorize"),

    //获取时空域 api
    getDomain("getDomain","http://bt1.geosts.ac.cn/api/dae-dev/datastore/rest/v0.1.0/datastore/sdomain/query?token=@token&names=@names"),

    getDomain1("getDomain","http://bt1.geosts.ac.cn/api/dae-dev/datastore/rest/v0.1.0/datastore/sdomain/query"),

    //获取类模板信息根据ID
    getOtypesByIds("getOtypesByIds","http://bt1.geosts.ac.cn/api/dae-dev/datastore/rest/v0.1.0/datastore/otype/query"),

    //获取时空域下的所有的空间对象
    getObject("getObject","http://bt1.geosts.ac.cn/api/dae-dev/datastore/rest/v0.1.0/datastore/object/query"),

    //获取时空域下的连接关系 参数信息传入时空域 sdomian
    getNetWork("","http://bt1.geosts.ac.cn/api/dae/rest/v0.1.0/datastore/getRelationCatalog"),

    //获取字段
    getFieldByFid("getFieldByFid","http://bt1.geosts.ac.cn/api/dae-dev/datastore/rest/v0.1.0/datastore/ofield/query"),

    getStyleById("","http://bt1.geosts.ac.cn/api/dae-dev/datastore/rest/v0.1.0/datastore/oformstyle/query"),

    //注销本次登录
    logout("logout","http://bt1.geosts.ac.cn/api/uc-dev/api/v2/account/logout");

    private final String name ;

    private final String value ;

    MyApi(String name, String value) {
        this.name=name;
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {return name;}
}
