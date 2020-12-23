package com.yzh.utilts;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import onegis.psde.psdm.OType;
import onegis.psde.reference.SpatialReferenceSystem;

import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNull;
import static com.yzh.Index.sDomain;


/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/23 11:12
 */
public class ESrsUtil {

    public static void getSrs(List<OType> oTypeList){
        if (oTypeList==null||oTypeList.size()==0){
            return ;
        }
        List<SpatialReferenceSystem> srsList =new ArrayList<>();
        for (OType oType : oTypeList) {
            if (!isNull(oType)&&!isEmpty(oType)){
                 srsList.add(oType.getSrs());
            }
        }
        List<SpatialReferenceSystem> collect = new ArrayList<>();
        for (SpatialReferenceSystem spatialReferenceSystem : srsList) {
            if(collect.size()==0){
                collect.add(spatialReferenceSystem);
                continue;
            }
            for (SpatialReferenceSystem referenceSystem : collect) {
                if(!spatialReferenceSystem.getId().equals(referenceSystem.getId())){
                    collect.add(spatialReferenceSystem);
                }
            }
        }
//        srsList.forEach(value->{
//            if(collect.size()==0){
//                collect.add(value);
//            }
//            collect.forEach(collectValue->{
//                if(value.getId()!=collectValue.getId()){
//                    collect.add(value);
//                }
//            });
//        });


        JSON json = JSONUtil.parse(collect);
        String path ="E:\\test\\" + sDomain.getName() + "\\test.srs";
        FileTools.exportFile(json,path);
    }
}
