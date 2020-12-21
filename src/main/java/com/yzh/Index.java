package com.yzh;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.dao.SDomainOutPutModel;
import com.yzh.userInfo.UserInfo;
import com.yzh.utilts.FieldUtils;
import com.yzh.utilts.FileTools;
import com.yzh.utilts.OtypeUtilts;
import onegis.psde.attribute.Field;
import onegis.psde.psdm.SDomain;
import onegis.psde.psdm.SObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.yzh.utilts.FileTools.*;
import static com.yzh.utilts.SDomainPagesTools.getPages;
import static com.yzh.utilts.SDomainUtil.getSDomain;

/**
 * @author Yzh
 * @create 2020-12-03 11:35
 */

public class Index {

    public static int pages;
    private static int pageNum = 1;
    private final static int pageSize = 10;
    public static SDomain sDomain;
    public static List<SObject> sObjectsList = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(Index.class);

    public static void main(String[] args) throws Exception {
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

        getPages(pageNum, pageSize, sDomainName, input, sDomains);
        int page = pages;
        String stop="no";
        while (stop.equals("no")) {
            System.out.println("是否选择当前页的时空域[yes/no]");
            if (input.next().equals("yes")){
                break;
            }
            //采用Switch Catch 控制上下页
            System.out.println("上一页 ：b 下一页 ：n");
            switch (input.next()) {
                case "n":
                    //是否选择下一页
                    if (page > pageNum) {
                        //重新做分页请求
                            pageNum++;
                            getPages(pageNum, pageSize, sDomainName, input, sDomains);
                    }
                    break;
                case "b":
                    //是否选择上一页
                    if (pageNum > 1 && pageNum <= page) {
                        //重新做分页请求
                            pageNum--;
                            getPages(pageNum, pageSize, sDomainName, input, sDomains);
                    }else {
                        System.out.println();
                    }
                    break;
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
            sDomain = sDomains.get(i);
            SDomainOutPutModel sDomainOutPutModel=new SDomainOutPutModel();
            SDomainOutPutModel sDomain = getSDomain(sDomainOutPutModel, Index.sDomain);
            JSONObject jsonObject=(JSONObject)JSONUtil.parse(sDomain);
            String path = "E:/test/"+sDomain.getName()+"/test.sdomain";
            exportFile(jsonObject,path);
        } catch (Exception e) {
            e.getMessage();
        }

        logger.debug("选择的时空域Id为=" + UserInfo.domain);
        //导出时空域基本信息

        //导出时空域下类模板
        OtypeUtilts.getOtype();
        //导出时空域下所有使用的属性
        List<Field> fieldList = FieldUtils.objectFieldsHandle(sObjectsList);
        FileTools.exportFile(JSONUtil.parse(fieldList),"E:/test/"+sDomain.getName()+"/test.fields");
        //导出时空域下所有使用的样式
//        FormUtils.objectFromsHandle(sObjectsList);
        //导出时空域下所有使用的形态

        //退出账号
        logout();
    }
}

/*阿里的fastjson格式化方案*/
//com.alibaba.fastjson.JSONObject parse = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parse(objectJsonStr);
//com.alibaba.fastjson.JSONObject data = (com.alibaba.fastjson.JSONObject) parse.get("data");