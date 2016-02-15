package com.reign.component.utils.Cache;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by ji on 16-2-4.
 */
class DelayItem<T> implements Delayed {

    //Created time
    private static long MILLISECONDS_ORIGIN = System.currentTimeMillis();

    //data item
    private T item;
    //timeOut (unit:milliseconds)
    private long milliseconds;

    public DelayItem(T item, long milliseconds) {
        this.milliseconds = milliseconds;
        this.item = item;
    }

    private static final long now() {
        return System.currentTimeMillis() - MILLISECONDS_ORIGIN;
    }

    public void setMilliseconds(long milliseconds) {
        MILLISECONDS_ORIGIN = System.currentTimeMillis();
        this.milliseconds = milliseconds;
    }

    /**
     * 如果超时，或者Map缓存中已经没有该元素，都会导致失效
     *
     * @param unit
     * @return
     */
    public long getDelay(TimeUnit unit) {
        long d = unit.convert(milliseconds - now(), TimeUnit.MILLISECONDS);

        return d;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DelayItem) {
            return item.equals(((DelayItem) obj).getItem());
        } else {
            return false;
        }
    }

    public int compareTo(Delayed o) {
        if (o == this) {
            return 0;
        }
        if (o instanceof DelayItem) {
            DelayItem x = (DelayItem) o;
            long diff = milliseconds - x.milliseconds;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
            else
                return 1;
        }
        long d = (getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    public T getItem() {
        return item;
    }
}