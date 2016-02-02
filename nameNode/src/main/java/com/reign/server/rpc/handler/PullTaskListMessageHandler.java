package com.reign.server.rpc.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.reign.component.constants.CoreConstant;
import com.reign.component.constants.MessageTypeConstant;
import com.reign.domain.rpc.NTMessageProtocol;
import com.reign.domain.task.Task;
import com.reign.server.cache.PipeLineCache;
import com.reign.server.cache.TaskCache;
import com.reign.server.dao.DaoFactory;
import com.reign.server.dao.TaskDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ji on 16-1-15.
 */
public class PullTaskListMessageHandler implements MessageHandlerInf{
    private static final Logger LOGGER = LoggerFactory.getLogger(PullTaskListMessageHandler.class);

    private static TaskDao taskDao;

    static {
        taskDao = (TaskDao) DaoFactory.getDao(TaskDao.class);
    }

    @Override
    public String handleMessage(NTMessageProtocol messageProtocol) {
        NTMessageProtocol result = new NTMessageProtocol();

        JSONArray taskList = null;

        JSONObject paramPbj = messageProtocol.getData();
        String nodeId = paramPbj.getString("nodeId");

        List<Long> taskIdList = PipeLineCache.getInstance().getTasksByNodeName(nodeId);
        if (taskIdList != null && taskIdList.size() > 0) {
            taskList = new JSONArray();
            for (Long tmpId : taskIdList) {
                Task task = TaskCache.getInstance().getTask(tmpId);
                if (task != null && (task.getStatus() == CoreConstant.TASK_STATUS_QUEUE)) {

                    //Task status change to 'taken' when TaskNode pull task
                    task.setStatus(CoreConstant.TASK_STATUS_TAKEN);
                    int successCount = taskDao.changeTaskStatus(task);
                    if (successCount <= 0) {
                        continue;
                    }

                    taskList.add(task);
                } else {
                    LOGGER.error("[PullTaskListMessageHandler] can not get task info from TaskCache [taskId:{},taskStatus:{}]", tmpId, task.getStatus());
                }
            }
        }
        result.setType(MessageTypeConstant.TASK_PULL_RESULT_TYPE);

        result.setArrayData(taskList);

        return result.toString();
    }

    /**
     * Task status change to 'taken' when TaskNode pull task
     */
}
