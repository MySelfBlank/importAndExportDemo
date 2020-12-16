package com.yzh.utilts;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.yzh.Index;
import com.yzh.api.MyApi;
import com.yzh.userInfo.UserInfo;
import onegis.psde.psdm.SDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Yzh
 * @create 2020-12-16 10:53
 */
public class SDomainPagesTools {
    private static final Logger logger = LoggerFactory.getLogger(SDomainPagesTools.class);
    private static Map<String,Object> params = new HashMap<>();
    public static void getPages(int pageNum, int pageSize, String sDomainName, int pages, Scanner input, List<SDomain> sDomains){
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        params.put("names", sDomainName);
        params.put("token", UserInfo.token);
        String getRespond = HttpUtil.get(MyApi.getDomain1.getValue(), params);
        logger.debug("获取时空域完毕");
        JSONObject jsonData = Index.formatData(getRespond);
        pages = jsonData.getInt("pages");
        sDomains.clear();
        sDomains = JSONArray.parseArray(jsonData.getStr("list"), SDomain.class);
        for (int i = 0; i < sDomains.size(); i++) {
            System.out.println("[" + (i + 1) + "]" + sDomains.get(i).getName());
        }
        //是否选择下一页
        if(pages>pageNum){
            System.out.println("是否选择下一页？[y/n]");
            if(input.next().equals("y")){
                //重新做分页请求
                pageNum++;
                getPages(pageNum,pageSize,sDomainName,pages,input,sDomains);
            }
        }
        //是否选择上一页
        if(pageNum>1&&pageNum<=pages){
            System.out.println("是否选择上一页？[y/n]");
            if(input.next().equals("y")){
                //重新做分页请求
                pageNum--;
                getPages(pageNum,pageSize,sDomainName,pages,input,sDomains);
            }
        }
        System.out.println();
        System.out.println("请选择时空域前的序号");
        try {
            Integer i = input.nextInt() - 1;
            while (i > sDomains.size()) {
                System.out.println("输入无效请重新输入：");
                i = input.nextInt() - 1;
            }
            UserInfo.domain = sDomains.get(i).getId().toString();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
