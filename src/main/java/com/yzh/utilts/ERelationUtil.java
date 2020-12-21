package com.yzh.utilts;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.userInfo.UserInfo;
import onegis.psde.psdm.SObject;
import onegis.psde.relation.Network;
import onegis.psde.relation.REdge;
import onegis.psde.relation.RNode;
import onegis.psde.relation.Relation;

import java.util.*;

import static com.yzh.Index.sDomain;
import static com.yzh.utilts.FileTools.exportFile;
import static com.yzh.utilts.FileTools.forJsonList;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/18 14:29
 */
public class ERelationUtil {
    /**
     * 获取连接网格
     *
     * @param sObjects
     */
    public static void getNetWork(List<SObject> sObjects) throws Exception {

        Set<Long> ids = new HashSet<>();
        if (sObjects == null || sObjects.size() == 0) {
            return;
        }
        //从对象中取出NetWork获取关系id
        for (SObject sObject : sObjects) {
            Network network = sObject.getNetwork();
            if (network == null) {
                continue;
            }
            List<RNode> nodes = network.getNodes();
            if (nodes == null || nodes.size() == 0) {
                continue;
            }
            for (RNode node : nodes) {
                REdge edge = node.getEdge();
                if (edge == null) {
                    continue;
                }
                Relation relation = edge.getRelation();
                if (relation == null) {
                    continue;
                }
                ids.add(relation.getId());
            }
        }
        getRelationFile(ids);
    }

    /**
     * 根据id打印关系数据到本地
     *
     * @param ids
     * @throws Exception
     */
    public static void getRelationFile(Set<Long> ids) throws Exception {
        //根据关系id打印出关系数据到本地
        Map<String, Object> param = new HashMap<>();
        param.put("token", UserInfo.token);
        param.put("ids", ids.toArray());

        String relationStr = HttpUtil.get(MyApi.getRelationById.getValue(), param);

        List<Relation> list = forJsonList(relationStr);

        String path = "E:\\test\\" + sDomain.getName() + "\\test.relation";
        exportFile(JSONUtil.parse(list), path);
    }


}
