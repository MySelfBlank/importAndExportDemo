package com.yzh.dao;

import com.yzh.dao.exportModel.EDObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yzh
 * @date 2021-01-09 16:26
 * 数据容器
 */
public class ExecuteContainer {
    /**
     * 数据对象
     */
    public static List<EDObject> dObjectList = new ArrayList<>();

    public static void clear(){
        dObjectList.clear();
    }

    public static void addDObject(List<EDObject> edObjects) {
        if (edObjects != null && !edObjects.isEmpty()) {
            dObjectList.addAll(edObjects);
        }
    }
}
