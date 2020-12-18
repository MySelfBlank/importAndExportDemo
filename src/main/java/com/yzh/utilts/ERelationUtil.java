package com.yzh.utilts;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.yzh.api.MyApi;
import onegis.psde.psdm.SObject;
import onegis.psde.relation.Network;
import onegis.psde.relation.REdge;
import onegis.psde.relation.RNode;

import java.util.*;

import static com.yzh.Index.sDomain;
import static com.yzh.utilts.FileTools.*;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/18 14:29
 */
public class ERelationUtil {
    /**
     * 获取连接网格
     * @param sObjects
     */
    public static void getNetWork(List<SObject> sObjects) throws Exception {

        //从对象中取出NetWork
        List<Network> netWorks  = new ArrayList<>();
        for (SObject sObject : sObjects) {
            if (sObject.getNetwork()!=null){
                 netWorks.add(sObject.getNetwork());
            }
        }
        List<RNode> nodes =new ArrayList<>();
        for (Network netWork : netWorks) {
            if (!netWork.getNodes().isEmpty()&&netWork.getNodes()!=null){
                nodes.addAll(netWork.getNodes());
            }
        }
        List<REdge> edges =new ArrayList<>();
        for (RNode rNode : nodes) {
            if (rNode.getEdge()!=null){
                edges.add(rNode.getEdge());
            }
        }

        Set<Long> ids = new HashSet<>();
        for (REdge edge : edges) {
            if (edge.getRelation().getId()!=null){
                ids.add(edge.getRelation().getId());
            }
        }
        Map<String,Object> param = new HashMap<>();
        param.put("ids",ids.toArray());

        String relationStr = HttpUtil.get(MyApi.getRelation.getValue(),param);

        JSONObject jsonObject = forJsonList(relationStr);
        String path = "E:/"+sDomain.getName()+"/test.relation";
        exportFile(jsonObject,path);
    }



}
