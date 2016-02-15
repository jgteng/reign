package com.reign.component.utils.Cache;

/**
 * Created by ji on 16-2-4.
 */
public interface ExpireCallBack<K, V> {

    public V handler(K key) throws Exception;

}
