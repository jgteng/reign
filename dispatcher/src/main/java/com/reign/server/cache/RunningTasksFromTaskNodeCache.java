package com.reign.server.cache;

import com.alibaba.fastjson.JSONObject;
import com.reign.component.constants.MessageTypeConstant;
import com.reign.component.utils.Cache.ExpireCache;
import com.reign.component.utils.Cache.ExpireCallBack;
import com.reign.domain.rpc.NTMessageProtocol;
import com.reign.domain.task.Task;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by ji on 16-2-3.
 */
public class RunningTasksFromTaskNodeCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunningTasksFromTaskNodeCache.class);


    private static final RunningTasksFromTaskNodeCache _INSTANCE = new RunningTasksFromTaskNodeCache();

    private static final ExpireCache<Long, String> _TASK_CACHE = ExpireCache
            .setExpireTime(60, 60, TimeUnit.SECONDS, true)
            .build(new ExpireCallBack() {
                public Object handler(Object key) {
                    try {
                        Long taskId = (Long) key;
                        String nodeName = _TASK_CACHE.get(taskId);
                        LOGGER.warn("任务状态长时间没有返回，主动向TaskNode查询，任务Id:{},taskNode:{}", taskId, nodeName);
                        Channel channel = TaskNodeConnectionCache.getInstance().getChannel(nodeName);
                        if (channel != null && channel.isActive() && channel.isWritable()) {
                            Task task = TaskCache.getInstance().getTask(taskId);
                            if (task != null && task.getRunLogId() != null) {
                                NTMessageProtocol message = new NTMessageProtocol();
                                message.setType(MessageTypeConstant.GET_TASK_STATUS_TYPE);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("logId", task.getRunLogId());
                                jsonObject.put("taskId", taskId);
                                message.setData(jsonObject);
                                LOGGER.debug("[dispatcher write:{}]", message);
                                channel.writeAndFlush(message.toString());
                            } else {
                                LOGGER.error("任务的logId为空，不再获取任务状态，从超时检测列表中移除，taskId:{}", taskId);
                                _TASK_CACHE.remove(taskId);
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error("向TaskNode查询任务状态失败", e);
                    }
                    return null;
                }
            });

    public RunningTasksFromTaskNodeCache() {
    }

    public static RunningTasksFromTaskNodeCache getInstance() {
        return _INSTANCE;
    }

    public void add(Long taskId, String nodeName) {
        _TASK_CACHE.put(taskId, nodeName);
    }

    public void remove(Long taskId) {
        _TASK_CACHE.remove(taskId);
    }

    public String get(Long taskId) {
        return _TASK_CACHE.get(taskId);
    }

    public boolean containsKey(Long taskId) {
        return _TASK_CACHE.containsKey(taskId);
    }

    public static void main(String[] args) throws InterruptedException {
        RunningTasksFromTaskNodeCache.getInstance().add(1L, "test");
        for (long i = 3; i < 1000; i++) {
            LOGGER.error("put:" + i);
            RunningTasksFromTaskNodeCache.getInstance().add(i, "test" + i);
        }
    }
}
