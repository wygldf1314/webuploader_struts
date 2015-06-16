/* 
 * @(#)fileLock.java    Created on 2015-6-16
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package wanpeng.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Administrator
 * @version $Revision: 1.0 $, $Date: 2015-6-16 下午4:19:34 $
 */
public class fileLock {
    private static Map<String, Lock> LOCKS = new HashMap<String, Lock>();

    public static synchronized Lock getLock(String key) {
        if (LOCKS.containsKey(key)) {
            return LOCKS.get(key);
        }
        else {
            Lock one = new ReentrantLock();
            LOCKS.put(key, one);
            return one;
        }
    }

    public static synchronized void removeLock(String key) {
        LOCKS.remove(key);
    }
}
