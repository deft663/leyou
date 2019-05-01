package com.leyou.goods.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhang
 * @date 2019年04月22日 17:37
 */
public class ThreadUtils {

    private static final ExecutorService es = Executors.newFixedThreadPool(10);

    public static void execute(Runnable runnable) {
        es.submit(runnable);
    }
}
