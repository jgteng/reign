package com.reign.client.core;

import com.alibaba.fastjson.JSON;
import com.reign.domain.runtime.RunTimeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    }


    private int startProcess() {
        int exitVal = -1;
        try {
            List<String> command = new ArrayList<String>();
            command.add("java");
            command.add("-jar");
            command.add("test.jar");
            command.add(JSON.toJSONString(runTimeBean));

            Process process = null;

            ProcessStream processStream = new ProcessStream(process, runTimeBean);

            //build the process. The runTimeBean information is passed into the process in param.
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            Map<String, String> processEnv = processBuilder.environment();

            //set the environment
            if (runTimeBean.getEnv() != null && runTimeBean.getEnv().size() > 0) {
                processEnv.putAll(runTimeBean.getEnv());
            }

            process = processBuilder.start();

            int pid = this.getProcessId(process);
            runTimeBean.setPid(pid);

            //start read output and error stream.
            processStream.startReadOutputStream();

            exitVal = process.waitFor();

        } catch (Throwable t) {
            LOGGER.error("[TaskRunner => startProcess] Start Task Runner Process Exception", t);
            exitVal = 1;
        }
        return exitVal;
    }

    /**
     * Get Process ID
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
}
