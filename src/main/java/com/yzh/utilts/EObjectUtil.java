package com.yzh.utilts;

import com.yzh.dao.*;
import onegis.common.utils.GeneralUtils;
import onegis.psde.attribute.Attribute;
import onegis.psde.attribute.Attributes;
import onegis.psde.compose.Compose;
import onegis.psde.compose.ComposeElement;
import onegis.psde.form.GeoBox;
import onegis.psde.model.Model;
import onegis.psde.model.ModelDef;
import onegis.psde.model.Models;
import onegis.psde.psdm.OBase;
import onegis.psde.psdm.OType;
import onegis.psde.psdm.SObject;
import onegis.psde.psdm.Version;
import onegis.psde.reference.SpatialReferenceSystem;
import onegis.psde.reference.TimeReferenceSystem;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/17 11:11
 */
public class EObjectUtil {


    private static int operationID = 0;
    private static Set<Long> actionIds = new HashSet<>();
    private static Set<Long> modelIDs = new HashSet<>();

    /**
     * @desc 设置对象类
     * @param sObject
     * @param esObject
     */
    public static void setOType(SObject sObject, ESObject esObject){
        OType otype = sObject.getOtype();
        Map<String,Object> otypeMap=new HashMap<>(2);
        otypeMap.put("id",otype.getId());
        otypeMap.put("name",otype.getName());
        esObject.setOtype(otypeMap);
    }
    /**
     * 设置行为
     */
    public static void setModels(ESObject esObject, SObject sObject) {

        Models models = sObject.getModels();
        if (models == null) {
            return;
        }

        List<Model> modelList = models.getModels();
        if (!GeneralUtils.isNotEmpty(modelList)) {
            return;
        }
        List<EModel> eModels = new ArrayList<>();
        for (Model model : modelList) {

            EModel eModel = new EModel();
            eModel.setId(model.getId());
            eModel.setName(model.getName());
            eModel.setInitData(model.getInitData());

            EModelDef eModelDef = new EModelDef();
            ModelDef mdef = model.getMdef();
            eModelDef.setId(mdef.getId());
            eModelDef.setName(mdef.getName());

            eModel.setMdef(eModelDef);
            eModel.setpLanguage(model.getpLanguage().getName());
            eModel.setExecutor(model.getExecutor());
            eModels.add(eModel);
        }

        esObject.setModels(eModels);
    }
    /**
     * 设置属性
     */
    public static void setAttribute(ESObject esObject, SObject sObject) {

        Attributes attributes = sObject.getAttributes();
        if (attributes == null) {
            return;
        }

        List<Attribute> attributeList = attributes.getAttributeList();
        List<EAttribute> eAttributes = getEAttributes(attributeList);
        esObject.setAttributes(eAttributes);
    }
    /**
     * 获取 List<EAttribute>
     */
    public static List<EAttribute> getEAttributes(List<Attribute> attributeList) {
        List<EAttribute> eAttributes = new ArrayList<>();
        for (Attribute attribute : attributeList) {
            EAttribute eAttr = new EAttribute();
            eAttr.setFid(attribute.getFid());
            eAttr.setName(attribute.getName());
            eAttr.setValue(attribute.getValue());
            eAttributes.add(eAttr);
        }
        return eAttributes;
    }

    /**
     * 设置基本信息【id、name、时空参考、时空域等】
     * @param esObject
     * @param sObject
     * @param dobjectMap
     */
    public static void setBasicInfo(ESObject esObject,SObject sObject,Map<Long, List<Long>> dobjectMap){
        esObject.setId(sObject.getId());
        esObject.setName(sObject.getName());
        esObject.setCode(!GeneralUtils.isNotEmpty(sObject.getCode()) ? "" : sObject.getCode());

        //设置时间参照
        esObject.setTrs("onegis:1001");
        TimeReferenceSystem trs = sObject.getTrs();
        String trsId =trs.getId();
        String trsAuthName = trs.getAuthName();
        if (trs != null && trsId != null && trsAuthName != null && StringUtils.isNotBlank(trsId) && StringUtils.isNotBlank(trsAuthName)) {
            esObject.setTrs(String.format("%s%s", trsAuthName == null || StringUtils.isBlank(trsAuthName) ? "" : trsAuthName + ":", trsId));
        }

        //设置空间参照
        esObject.setSrs("ongis:4326");
        SpatialReferenceSystem srs = sObject.getSrs();
        String srsId = srs.getId();
        String srsAuthName = srs.getAuthName();
        if (srs != null && srsId != null && srsAuthName != null && StringUtils.isNotBlank(srsId) && StringUtils.isNotBlank(srsAuthName)) {
            esObject.setSrs(String.format("%s%s", srsAuthName == null || StringUtils.isBlank(srsAuthName) ? "" : srsAuthName + ":", srs.getId()));
        }

        esObject.setSdomain(sObject.getSdomain()==null?0L:sObject.getSdomain());
        //获取真实的时间
        Long realTime = sObject.getRealTime();
        if (realTime!=null){
            esObject.setRealTime(sObject.getRealTime());
        }
        //获取空间区域
        GeoBox geoBox = sObject.getGeoBox();
        esObject.addGeobox(geoBox.getMinx(),
                geoBox.getMiny(), geoBox.getMinz(),
                geoBox.getMaxx(), geoBox.getMaxy(), geoBox.getMaxz());

        List<OBase> parents = sObject.getParents();
        if (GeneralUtils.isNotEmpty(parents)){
            esObject.setParent(parents.get(0).getId());
        }

        esObject.setDataSource(sObject.getFrom());
        if (dobjectMap.containsKey(sObject.getId())){
            List<Long> datas = dobjectMap.get(sObject.getId());
            esObject.setDataGenerate(!GeneralUtils.isNotEmpty(datas) ? new ArrayList<>() : datas);
        }
    }

//    /**
//     * 设置关系
//     */
//    public static void setNetWork(ESObject esObject,SObject sObject){
//
//        esObject.setNetwork();
//    }
    /**
     * 设置组成结构
     */
    public static void  setCompose(ESObject esObject,SObject sObject){

        Compose compose = sObject.getCompose();
        if (compose==null){
            return;
        }

        List<ECompose> eComposes = new ArrayList<>();

        ECompose eCompose = new ECompose();
        eCompose.setId(compose.getId());
        eCompose.setName(compose.getName());
        eCompose.setDes(compose.getDes());
        eCompose.setStrong(compose.getStrong());

        List<ComposeElement> elements = compose.getElements();

        if (!GeneralUtils.isNotEmpty(elements)){
            return;
        }

        List<EComposeElement> eComposeElements = new ArrayList<>();
        for (ComposeElement element : elements) {
            EComposeElement eComposeElement =new EComposeElement();
            eComposeElement.setOid(element.getOid());
            eComposeElement.setName(element.getName());
            eComposeElement.setMatrix(element.getMatrix());
            eComposeElements.add(eComposeElement);
        }

        eCompose.setElements(eComposeElements);
        eComposes.add(eCompose);
        esObject.setCompose(eComposes);
    }

    /**
     * 构建空间对象交换格式对象ESObject
     */
    public static ESObject buildESObject(SObject startSObject,Map<Long, List<Long>> dobjectMap){
        if (startSObject==null){
            return null;
        }
        //获取初始版本中已有的属性
        List<Attribute> attributeList = startSObject.getAttributes().getAttributeList();
        attributeList.forEach(a -> actionIds.add(a.getFid()));

        operationID=0;
        ESObject esObject = new ESObject();
        setBasicInfo(esObject,startSObject,dobjectMap);
        setOType(startSObject,esObject);
        setAttribute(esObject,startSObject);
        setModels(esObject,startSObject);
        setCompose(esObject,startSObject);

        return esObject;
    }

    public static ESObject buildESObject(List<SObject> resList, Map<Long, List<Long>> dobjectMap){
        if (!GeneralUtils.isNotEmpty(resList)){
            return null;
        }
        // 找出对象版本最小的那个，也就是当前列表中对象的起始版本
        SObject startSObject = getOriginal(resList);

        if (startSObject==null){
            return null;
        }

        SpatialReferenceSystem spatialReferenceSystem = startSObject.getSrs();
        TimeReferenceSystem timeReferenceSystem = startSObject.getTrs();
//        if (spatialReferenceSystem != null) {
//            Export.addSrsSystem(startSObject.getSrs());
//            Export.addSrsSystemId(spatialReferenceSystem.getId());
//        }
//
//        if (timeReferenceSystem != null) {
//            Export.addTrsSystem(startSObject.getTrs());
//            Export.addTrsSystemId(timeReferenceSystem.getId());
//        }

        OType oType = startSObject.getOtype();
        if (oType!=null&&oType.getId()!=null){

        }
        return buildESObject(startSObject,dobjectMap);
    }

    /**
     * 拿到对象列表中，初始状态的对象
     * @param sObjects
     * @return
     */
    public static SObject getOriginal(List<SObject> sObjects){
        if (!GeneralUtils.isNotEmpty(sObjects)){
            return null;
        }

        SObject sObject = new SObject();
        Long minVid = Long.MAX_VALUE;
        for (SObject object : sObjects) {

            Version version = object.getVersion();
            if (version==null||version.getVid()==null){
                continue;
            }

            Long vid = version.getVid();
            if (vid<minVid){
                minVid=vid;
                sObject =object;
            }
        }
        return sObject;
    }

    /**
     * 空间对象列表分组
     * @param sObjects
     * @return
     */
    public static HashMap<Long,List<SObject>> groupSObjects(List<SObject> sObjects){
        //对象分组
        HashMap<Long,List<SObject>> sobjectGroups = new HashMap<>();

        if (!GeneralUtils.isNotEmpty(sObjects)){
            return sobjectGroups;
        }

        for (SObject sObject : sObjects) {
            Long oId = sObject.getId();
            addGroup(sobjectGroups,oId,sObject);
        }
        return sobjectGroups;
    }
    /**
     * 分组添加SObject
     * @param groups  对象列表分组
     * @param oId     对象id
     * @param sObject 空间对象
     */
    public static void addGroup(HashMap<Long, List<SObject>> groups, Long oId, SObject sObject){
        if (groups==null){
            return;
        }

        List<SObject> sObjects;
        if (groups.containsKey(oId)){
            sObjects=groups.get(oId);
            sObjects.add(sObject);
        }else {
            sObjects=new ArrayList<>();
            sObjects.add(sObject);
        }

        groups.put(oId,sObjects);
    }

}
