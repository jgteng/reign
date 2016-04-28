package com.reign.client.core;

import com.reign.domain.runtime.RunTimeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by ji on 16-1-15.
 */
public class ProcessStream {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessStream.class);

    private Process process;

    private RunTimeBean runTimeBean;

    public ProcessStream(Process process, RunTimeBean runTimeBean) {
        this.process = process;
        this.runTimeBean = runTimeBean;
    }


    public void startReadOutputStream() {
        StreamWatcher outputWatcher = new StreamWatcher(process.getInputStream(),"OUTPUT",this);
        StreamWatcher errorWatcher = new StreamWatcher(process.getErrorStream(), "ERROR", this);
        new Thread(outputWatcher, "process output watcher").start();
        new Thread(errorWatcher, "process error watcher").start();
    }

    public RunTimeBean getRunTimeBean() {
        return runTimeBean;
    }
}

class StreamWatcher implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamWatcher.class);

    private InputStream inputStream;
    private String type;
    private BufferedReader bufferedReader = null;
    private ProcessStream processStream;


    public StreamWatcher(InputStream inputStream, String type,ProcessStream processStream) {
        this.inputStream = inputStream;
        this.type = type;
        this.processStream = processStream;
    }

    public void run() {
        try {

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                LOGGER.debug("[" + processStream.getRunTimeBean().getTaskId() + "]" + line);
//                ps.createStreamFile(line);
            }
        } catch (Exception e) {
            LOGGER.error("[StreamWatcher] read process outputStream (" + type + ") error", e);
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e) {
                LOGGER.error("[StreamWatcher] close reader(" + type + ") error", e);
            }
        }
    }
}
