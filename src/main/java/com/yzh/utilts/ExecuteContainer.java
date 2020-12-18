package com.yzh.utilts;

import com.yzh.dao.EDObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author        :  yuyazhou
 * @ CreateDate    :  2020/12/18 10:08
 */
public class ExecuteContainer {
    /**
     * dobject容器
     */
    private List<EDObject> edObjects;

    public Map<Long,List<Long>> getEDObjectMap(){
        Map<Long,List<Long>> map =new HashMap<>(8);

        if (this.edObjects.size()>0){
            for (EDObject edObject : this.edObjects) {
                if (map.containsKey(edObject.getDataSource())){
                    map.get(edObject.getDataSource()).add(edObject.getId());
                }else {
                    map.put(edObject.getDataSource(),new ArrayList<Long>(){{add(edObject.getId());}});
                }
            }
        }
        return map;
    }

}
