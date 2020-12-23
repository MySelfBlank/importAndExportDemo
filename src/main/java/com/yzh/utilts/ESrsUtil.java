package com.yzh.utilts;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import onegis.psde.psdm.OType;
import onegis.psde.reference.SpatialReferenceSystem;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNull;
import static com.yzh.Index.sDomain;


/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/23 11:12
 */
public class ESrsUtil {

    public static void getSrs(List<OType> oTypeList) throws ConcurrentModificationException {
        if (oTypeList == null || oTypeList.size() == 0) {
            return;
        }
        List<SpatialReferenceSystem> srsList = new ArrayList<>();
        for (OType oType : oTypeList) {
            if (!isNull(oType) && !isEmpty(oType)) {
                srsList.add(oType.getSrs());
            }
        }
        List<SpatialReferenceSystem> collect = new ArrayList<>();
        for (SpatialReferenceSystem spatialReferenceSystem : srsList) {
            if (collect.size() == 0) {
                collect.add(spatialReferenceSystem);
                continue;
            }
            for (int i = 0; i < collect.size(); i++) {
                if (!spatialReferenceSystem.getId().equals(collect.get(i).getId())) {
                    collect.add(spatialReferenceSystem);
                }
            }
        }
        JSON json = JSONUtil.parse(collect);
        String path = "E:\\test\\" + sDomain.getName() + "\\test.srs";
        FileTools.exportFile(json, path);
    }
}
