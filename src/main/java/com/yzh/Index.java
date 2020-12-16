package com.yzh;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.yzh.api.MyApi;
import com.yzh.dao.OtypeInputModel;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.SDomainPagesTools;
import onegis.psde.attribute.Field;
import onegis.psde.psdm.OType;
import onegis.psde.psdm.SDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.yzh.utilts.FileTools.*;
import static com.yzh.utilts.SDomainPagesTools.getPages;

/**
 * @author Yzh
 * @create 2020-12-03 11:35
 */

public class Index {
    final static String JSONdata = "data";
    final static String LIST = "list";
    public static int pages;
    private static int pageNum=1;
    final static int pageSize = 10;
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
        System.out.println("请输入时空域名称");
        //获取输入
        String sDomainName = input.nextLine();

        //分页设置
        List<SDomain> sDomains = new ArrayList<>();

        getPages(pageNum,pageSize,sDomainName,input,sDomains);
        int page =pages;
        while (0<pageNum&&pageNum<page){
            //是否选择下一页
            if (page>pageNum){
                System.out.println("是否选择下一页？[y/n]");
                //重新做分页请求
                if(input.next().equals("y")){
                    pageNum++;
                    getPages(pageNum,pageSize,sDomainName,input,sDomains);
                }else {
                    break;
                }
            }

            //是否选择上一页
            if (pageNum>1&&pageNum<=page){
                System.out.println("是否选择上一页？[y/n]");
                //重新做分页请求
                if(input.next().equals("y")){
                    pageNum--;
                    getPages(pageNum,pageSize,sDomainName,input,sDomains);
                }else {
                    break;
                }
            }
        }

        System.out.println("请选择时空域前的序号");
        try {
            Integer i = input.nextInt() - 1;
            while (i > sDomains.size()) {
                System.out.println("输入无效请重新输入：");
                i = input.nextInt() - 1;
            }
            UserInfo.domain = sDomains.get(i).getId();

        } catch (Exception e) {
            e.getMessage();
        }

        logger.debug("选择的时空域Id为=" + UserInfo.domain);
        //导出时空域基本信息

        //导出类模板

        //导出对象

        //导出用到的关系模板

        //根据时空域Id查询时空域下的对象
        params.clear();
        params.put("sdomains", UserInfo.domain);
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
        //退出账号
        logout();
    }
}
/*
 * 类视图和时空域无直接联系
 * 可查询到时空域下的所有以类模板生成的对象
 * 再从对象中获取类模板信息
 * 最后根据类模板信息获取到相应的信息
 *
 * */