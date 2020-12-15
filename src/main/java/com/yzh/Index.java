package com.yzh;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.yzh.api.MyApi;
import com.yzh.dao.OtypeInputModel;
import com.yzh.userInfo.UserInfo;
import onegis.psde.attribute.Field;
import onegis.psde.psdm.OType;
import onegis.psde.psdm.SDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Yzh
 * @create 2020-12-03 11:35
 */

public class Index {
    final static String JSONdata = "data";
    final static String LIST = "list";
    private static final Logger logger = LoggerFactory.getLogger(Index.class);
    static Map<String, Object> params = new HashMap<>();
    public static void main(String[] args) {
        logger.debug("开始运行");
        //用户Token
        Scanner input = new Scanner(System.in);
        System.out.println("请输入您的账号和密码");
        login("yzhyaoxiaozhi@foxmail.com", "yzh1997");
//        login(input.nextLine().trim(), input.nextLine().trim());

        //get请求 并返回请求结果（时空域信息）
        logger.debug("开始获取时空域");
        if (UserInfo.token == null) {
            logger.error("用户登录失败，程序结束");
            return;
        }
        params.clear();
        params.put("token",UserInfo.token);
        params.put("uids",UserInfo.userId);
        //String getRespond = HttpUtil.get(MyApi.getDomain.getValue().replace("@token", UserInfo.token).replace("@names", ""));
        String getRespond = HttpUtil.get(MyApi.getDomain1.getValue(),params);
        logger.debug("获取时空域完毕");
        JSONObject jsonData = formatData(getRespond);
        logger.debug("获取当前用户创建的时空域");
        List<SDomain> sDomains = JSONArray.parseArray(jsonData.getStr(LIST), SDomain.class);
        //sDomains.stream().filter(v->UserInfo.userId.equals(v.getUser().getUid())).collect(Collectors.toList());

        //获取该用户创建的时空域
        List<SDomain> userDomain = new ArrayList<>();
        sDomains.forEach(v -> {
            if (v.getUser().getUid().equals(UserInfo.userId)) {
                userDomain.add(v);
            }
        });
        System.out.println("您账户下的时空域有：");
        for (int i = 0; i < userDomain.size(); i++) {
            System.out.println("[" + (i + 1) + "]" + userDomain.get(i).getName());
        }
        System.out.println("请输入要选择的时空域：");
        //获取时空域Id
        String domainId = "";
        try {
            Integer i =input.nextInt() - 1;
            while (i>userDomain.size()){
                System.out.println("输入无效请重新输入：");
                i=input.nextInt()-1;
            }
            domainId = userDomain.get(i).getId().toString();
        } catch (Exception e) {
            e.getMessage();
        }

        logger.debug("选择的时空域Id为=" + domainId);

        //根据时空域Id查询时空域下的对象
        params.clear();
        params.put("sdomains", domainId);
        String objectJsonStr = HttpUtil.get(MyApi.getObject.getValue(), params);
        JSONObject data = formatData(objectJsonStr);

        /*阿里的fastjson格式化方案*/
        //com.alibaba.fastjson.JSONObject parse = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parse(objectJsonStr);
        //com.alibaba.fastjson.JSONObject data = (com.alibaba.fastjson.JSONObject) parse.get("data");

        String objectListStr = data.getStr(LIST);
        List<JSONObject> objectList = JSONArray.parseArray(objectListStr, JSONObject.class);

        //获取当前时空域下的所有类模板Id
        Set<String> otypeIds = new HashSet<>();

        for (JSONObject o : objectList) {
            JSONObject otype = (JSONObject) o.get("otype");
            otypeIds.add(otype.getStr("id"));
        }
        logger.debug("当前时空域下所有的类模板Id=" + otypeIds);

        //通过类模板Id 查询该类模板的其他相关信息
        params.clear();
        params.put("token", UserInfo.token);
        params.put("ids", otypeIds.toArray());
        String otypeInfoStr = HttpUtil.get(MyApi.getOtypesByIds.getValue(), params);
        JSONObject otypeInfoJson = formatData(otypeInfoStr);
        List<JSONObject> oTypesJsonList = JSONArray.parseArray(otypeInfoJson.getStr(LIST), JSONObject.class);

        //类模板集合
        List<OType> oTypeList = new ArrayList<>();
        oTypesJsonList.forEach(v -> {
            //System.out.println(v.toBean(OType.class).getName());
            oTypeList.add(v.toBean(OType.class));
        });

        //对数据进行整合
        List<OtypeInputModel> output = new ArrayList<>();
        oTypeList.forEach(v -> {
            OtypeInputModel otypeInputModel = new OtypeInputModel();
            List<String> forms = new ArrayList<>();
            List<Field> fields = new ArrayList<>();
            otypeInputModel.setName(v.getName());
            v.getFormStyles().getStyles().forEach(x -> {
                forms.add(x.getType().getName());
            });
            v.getFields().getFields().forEach(y -> {
                fields.add(y);
            });
            otypeInputModel.setForms(forms);
            otypeInputModel.setFides(fields);
            output.add(otypeInputModel);
        });
        System.out.println(output);

        //System.out.println(oTypeList.toArray());
        //把字符串转换为Json
//        JSONObject jsonObject = JSONUtil.parseObj(getRespond);
//        //创建一个文件路径
//        File file = new File("D:/时空域.domain");
//        //判断文件是否存在
//        if (!file.exists()) {
//            try {
//                //创建该文件
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        //将查询内容写到文件当中
//        FileWriter writer = new FileWriter(file, "utf-8");
//        writer.write(jsonObject.toString());

        //退出账号
        logout();
    }

    /**
     * @param username
     * @param password
     */
    public static void login(String username, String password) {
        HashMap<String, Object> login = new HashMap<>();
        //设置登录参数
        login.put("username", username);
        login.put("password", password);
        //post请求
        String post = HttpUtil.post(MyApi.login.getValue(), login);
        //登录失败处理
        JSONObject object = JSONUtil.parseObj(post);
        if (object.getStr("status").equals("453")) {
            logger.error(object.getStr("message"));
            return;
        }
        //数据处理
        JSONObject data = formatData(post);

        logger.debug("存储用户信息");
        UserInfo.token = data.get("token").toString();
        logger.debug("用户Token信息=" + data.get("token").toString());

        Map<String, Object> param = new HashMap<>();
        param.put("token", UserInfo.token);
        String result = HttpUtil.get(MyApi.getUserInfo.getValue(), param);
        JSONObject userData = formatData(result);
        UserInfo.userId = userData.get("id").toString();
        UserInfo.email = userData.getStr("email");
        logger.debug("用户的ID=" + UserInfo.userId + " 邮箱是=" + UserInfo.email);
    }

    public static void logout() {
        HttpUtil.get(MyApi.logout.getValue());
        logger.debug("运行结束");
    }

    //格式化请求结果返回的data

    /**
     * @param object
     * @return
     */
    public static JSONObject formatData(String object) {
        JSONObject sourceData = JSONUtil.parseObj(object);
        return (JSONObject) JSONUtil.parse(sourceData.get(JSONdata));
    }
}
/*
 * 类视图和时空域无直接联系
 * 可查询到时空域下的所有以类模板生成的对象
 * 再从对象中获取类模板信息
 * 最后根据类模板信息获取到相应的信息
 *
 * */