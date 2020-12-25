package com.yzh.utilts;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yzh.api.MyApi;
import com.yzh.dao.ERelation;
import com.yzh.dao.ERelationEnum;
import com.yzh.userInfo.UserInfo;
import onegis.psde.psdm.SObject;
import onegis.psde.relation.Network;
import onegis.psde.relation.REdge;
import onegis.psde.relation.RNode;
import onegis.psde.relation.Relation;
import onegis.psde.util.JsonUtils;

import java.util.*;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNull;
import static com.yzh.Index.sDomain;
import static com.yzh.utilts.FileTools.exportFile;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/18 14:29
 */
public class ERelationUtil {
    /**
     * 获取连接网格
     * @param sObjects
     */
    public static void getRelation(List<SObject> sObjects) throws Exception {

        Set<Long> RelationIds = new HashSet<>();
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
            RelationIds.addAll(getRelationId(nodes));
        }
        getRelationFile(RelationIds);
    }

    /**
     * 获取关系id
     * @param nodes
     * @return
     */
    public static Set<Long> getRelationId(List<RNode> nodes){
        Set<Long> RelationIds = new HashSet<>();
        if (isNull(nodes)||isEmpty(nodes)){
            return new HashSet<>();
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
            RelationIds.add(relation.getId());
        }
        return RelationIds;
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
        JSONObject jsonObject = FileTools.formatData(relationStr);

        List<Relation> list = JsonUtils.jsonToList(jsonObject.getStr("list"), Relation.class);

        String path = "E:\\test\\" + sDomain.getName() + "\\test.relation";
        exportFile(JSONUtil.parse(list), path,"relation");
    }

    /**
     * 对连接关系进行处理
     * @param relation
     * @return
     * @throws Exception
     */
    public static ERelation dsRelations2ERelation(Relation relation) throws Exception {
        if (relation == null){
            return null;
        }
        ERelation eRelation = new ERelation();
        eRelation.setId(relation.getId());
        eRelation.setName(relation.getName());
        eRelation.setFields(FieldUtils.dsField2Field(relation.getFields()));
        eRelation.setMappingType(ERelationEnum.getEnum(relation.getMappingType().getValue()).getName());
        eRelation.setRules(new ArrayList());
        return eRelation;
    }


}
