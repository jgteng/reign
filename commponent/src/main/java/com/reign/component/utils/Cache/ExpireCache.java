package com.reign.component.utils.Cache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by ji on 16-2-4.
 */
public class ExpireCache<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpireCache.class);

    //Data Cache
    private ConcurrentHashMap<K, V> _CACHE_MAP = new ConcurrentHashMap<K, V>();

    //DelayQueue for expire
    private DelayQueue<DelayItem<K>> _Q = new DelayQueue<DelayItem<K>>();

    private Thread expireCheckThread;

    //首次超时时间
    private long firstExpireTime;

    //超时之后是否删除
    private boolean continueCheckAfterExpire;

    //第一次超时之后，后续超时时间
    private long subsequentExpireTime;

    //超时单位
    private TimeUnit unit;

    private ExpireCallBack expireCallBack;

    private ExpireCache(long firstExpireTime, TimeUnit unit) {
        this.firstExpireTime = firstExpireTime;
        this.unit = unit;
    }

    public static ExpireCache setExpireTime(long firstExpireTime, long subsequentExpireTime, TimeUnit unit, boolean continueCheckAfterExpire) {
        ExpireCache expireCache = new ExpireCache(firstExpireTime, unit);
        if (continueCheckAfterExpire == true) {
            expireCache.continueCheckAfterExpire = continueCheckAfterExpire;
            if (subsequentExpireTime <= 0) {
                expireCache.subsequentExpireTime = firstExpireTime;
            } else {
                expireCache.subsequentExpireTime = subsequentExpireTime;
            }
        }
        return expireCache;
    }

    /**
     * 采用这种方式，而不是在构造方法中创建线程是为了防止构造方法中启动线程，且线程中会有实例引用导致的this逸出
     * <p>下面带参数的build方法原因也是如此
     *
     * @return
     */
    public ExpireCache build() {
        Runnable daemonTask = new Runnable() {
            public void run() {
                expireCheck();
            }
        };
        expireCheckThread = new Thread(daemonTask);
        expireCheckThread.setDaemon(true);
        expireCheckThread.setName("ExpireCacheCheckThread");
        expireCheckThread.start();
        return this;
    }

    /**
     * 带有失效回调函数的build方法
     *
     * @param callBack
     * @return
     */
    public ExpireCache build(ExpireCallBack<K, V> callBack) {
        this.expireCallBack = callBack;
        Runnable daemonTask = new Runnable() {
            public void run() {
                expireCheck();
            }
        };
        expireCheckThread = new Thread(daemonTask);
        expireCheckThread.setDaemon(true);
        expireCheckThread.setName("ExpireCacheCheckThread");
        expireCheckThread.start();
        return this;
    }

    /**
     * 真正的失效检测
     */
    private void expireCheck() {
        for (; ; ) {
            try {
                DelayItem<K> delayItem = _Q.take();
                if (delayItem != null) {
                    LOGGER.warn("[expireCache] timeout");
                    if (expireCallBack != null) {
                        try {
                            expireCallBack.handler(delayItem.getItem());
                        } catch (Exception e) {
                            LOGGER.error("[ExpireCache expireCheck] callback error", e);
                        }
                    }

                    //如果超时后还继续检测，则item设置新的超时时间;
                    //否则从Cache中删除数据
                    if (continueCheckAfterExpire) {
                        long milliseconds = TimeUnit.MILLISECONDS.convert(subsequentExpireTime, unit);
                        delayItem.setMilliseconds(milliseconds);
                        _Q.put(delayItem);
                    } else {
                        _CACHE_MAP.remove(delayItem.getItem());
                    }
                }
            } catch (InterruptedException e) {
                LOGGER.error("[Expire Cache] do expireCheckError", e);

                //失败后停100ms
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void put(K key, V value) {
        V oldValue = _CACHE_MAP.put(key, value);
        if (oldValue != null) {
            //todo 这个地方性能比较差，DelayQueue删除元素慢
            _Q.remove(new DelayItem<K>(key, 0L));
        }

        long milliseconds = TimeUnit.MILLISECONDS.convert(firstExpireTime, unit);
        DelayItem delayItem = new DelayItem(key, milliseconds);
        _Q.put(delayItem);
    }

    public V get(K key) {
        return _CACHE_MAP.get(key);
    }

    public V remove(K key) {
        V value = _CACHE_MAP.remove(key);
        if (value != null) {
            _Q.remove(new DelayItem<K>(key, 0l));
        }
        return value;
    }


    public static void main(String[] args) throws InterruptedException {
        ExpireCache cache = ExpireCache.setExpireTime(10, 5, TimeUnit.SECONDS, true).build();
        cache.put(1L, 2L);
        cache.put(1L, 2L);
        cache.put(1L, 2L);
        System.out.println(1231);

        ExpireCache cache1 = ExpireCache.setExpireTime(10, 3, TimeUnit.SECONDS, true).build(new ExpireCallBack() {
            @Override
            public Object handler(Object key) {
                System.out.println("what is matter" + key);
                return null;
            }
        });
        long start = System.currentTimeMillis();
        for (long i = 0; i < 10000; i++) {
            cache1.put(i, "sdjfklsjlfkjsdlkfjlsdjf" + i);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        long star1 = System.currentTimeMillis();
        for (long i = 0; i < 10000; i++) {
            cache1.put(i, "sdfsdfsd" + i);
        }
        long end2 = System.currentTimeMillis();
        System.out.println(end2 - star1);
        System.out.println("ok");
        Thread.sleep(10000000L);

    }

}
