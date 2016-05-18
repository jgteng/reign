package com.reign.client.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.reign.client.cache.RunningTaskCache;
import com.reign.client.dao.SqliteDaoFactory;
import com.reign.client.rpc.socket.NettyClient;
import com.reign.component.constants.CoreConstant;
import com.reign.component.constants.MessageTypeConstant;
import com.reign.domain.rpc.NTMessageProtocol;
import com.reign.domain.rpc.TaskRunStatusRpcData;
import com.reign.domain.runtime.RunTimeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by ji on 16-1-15.
 */
public class TaskRunner implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRunner.class);

    private RunTimeBean runTimeBean;

    public TaskRunner(RunTimeBean runTimeBean) {
        this.runTimeBean = runTimeBean;
    }

    public void run() {
        int exitVal = this.startProcess();

        try {
            SqliteDaoFactory.getInstance().updateStatus(runTimeBean.getLogId(), exitVal == 0 ? CoreConstant.TASK_STATUS_WAIT : CoreConstant.TASK_STATUS_FAIL);
        } catch (Exception e) {
            LOGGER.error("write task end status into sqlLite error,[taskId:{},logId:{},result:{}]", new Object[]{runTimeBean.getTaskId(), runTimeBean.getLogId(), exitVal}, e);
        }

        NTMessageProtocol ntMessageProtocol = new NTMessageProtocol();
        ntMessageProtocol.setType(MessageTypeConstant.TASK_STATUS_TYPE);

        TaskRunStatusRpcData runStatusRpcData = new TaskRunStatusRpcData();
        runStatusRpcData.setLogId(Long.parseLong(runTimeBean.getLogId()));
        runStatusRpcData.setTaskId(runTimeBean.getTaskId());
        runStatusRpcData.setStatus(exitVal == 0 ? CoreConstant.TASK_STATUS_WAIT : CoreConstant.TASK_STATUS_FAIL);
        runStatusRpcData.setTime(new Date());

        JSONArray dataArray = new JSONArray();
        dataArray.add(JSON.parseObject(JSON.toJSONString(runStatusRpcData)));
        ntMessageProtocol.setArrayData(dataArray);

        NettyClient.getInstance().sendMessage(ntMessageProtocol.toString());

    }


    private int startProcess() {
        int exitVal = -1;
        try {
            List<String> command = new ArrayList<String>();
            command.add("java");
            command.add("-jar");
            command.add(" /tmp/taskRunner.jar");
            command.add(JSON.toJSONString(runTimeBean));


//            command.add("/bin/sh");
//            command.add("-c");
//            command.add(" sh /tmp/test.sh");

            Process process = null;

            //build the process. The runTimeBean information is passed into the process in param.
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            Map<String, String> processEnv = processBuilder.environment();

            //set the environment
            Map<String, String> env = runTimeBean.getEnv();
            if (env == null) {
                env = new HashMap<String, String>();
            }
            //进程参数增加REIGN_T_ID变量，方便查找进程
            env.put("REIGN_T_ID", runTimeBean.getLogId());

            processEnv.putAll(env);

            process = processBuilder.start();

            //add task to running cache when started
            RunningTaskCache.getInstance().addTask(runTimeBean);

            int pid = this.getProcessId(process);
            runTimeBean.setPid(pid);

            try {
                SqliteDaoFactory.getInstance().updateProcessId(runTimeBean.getLogId(), pid);
            } catch (Exception e) {
                LOGGER.error("[TaskRunner] Try to write process id into sqlLite error [logId:{}], [processId:{}]", new Object[]{runTimeBean.getLogId(), pid}, e);
            }

            ProcessStream processStream = new ProcessStream(process, runTimeBean);
            //start read output and error stream.
            processStream.startReadOutputStream();

            exitVal = process.waitFor();

            //remove task from running cache when task ended
            RunningTaskCache.getInstance().removeTask(runTimeBean.getTaskId());

            Thread.sleep(5000L);
        } catch (Throwable t) {
            LOGGER.error("[TaskRunner => startProcess] Start Task Runner Process Exception", t);
            exitVal = 1;
        }
        return exitVal;
    }

    /**
     * Get Process ID
     *
     * @param process
     * @return
     */
    private int getProcessId(Process process) {
        try {
            Field field = process.getClass().getDeclaredField("pid");
            field.setAccessible(true);
            int pid = (Integer) field.get(process);
            return pid;
        } catch (Exception ex) {
            LOGGER.error("[TaskRunner => getProcessId] Try to get Process Id Error", ex);
        }
        return 0;
    }

    public static void main(String[] args) {
        TaskRunner taskRunne = new TaskRunner(new RunTimeBean());
        taskRunne.run();
    }
}
