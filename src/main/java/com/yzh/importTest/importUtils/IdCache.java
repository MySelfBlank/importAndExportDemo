package com.yzh.importTest.importUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yzh
 * @create 2021-01-12 11:08
 * @details
 */
public class IdCache {
    //缓存全局可用
    public static Map<Long,Long> fieldOldIdAndNewIdCache = new HashMap<>();

    public static Map<Long,Long> formStylesOidAndNewId = new HashMap<>();

    public static Map<Long, Long> relationNewIdAndOldId = new HashMap<>();
}
