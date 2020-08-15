package com.youthsdt.spiderfactory.util;

import java.util.UUID;

/**
 * @author xiangjing
 * @version 1.0
 * @date 2020/8/5 14:36
 */
public class UUIDUtils {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
