package com.reign.client.thread.executors;

import com.alibaba.fastjson.JSONArray;
import com.reign.client.cache.RunningTaskCache;
import com.reign.client.main.StartUp;
import com.reign.client.rpc.socket.NettyClient;
import com.reign.client.thread.template.ThreadTemplate;
import com.reign.component.constants.MessageTypeConstant;
import com.reign.domain.rpc.NTMessageProtocol;

import java.util.Set;

/**
 * Created by ji on 16-2-2.
 */
public class SendRunningTasksToNameNode extends ThreadTemplate {

    @Override
    public void run() {
        Set<Long> runningTasks = RunningTaskCache.getInstance().getTaskIds();
        NTMessageProtocol ntMessageProtocol = new NTMessageProtocol();
        ntMessageProtocol.setType(MessageTypeConstant.SEND_RUNNING_TASKS_TYPE);
        ntMessageProtocol.setSource(StartUp.nodeName);

        JSONArray dataArray= new JSONArray();
        dataArray.addAll(runningTasks);
        ntMessageProtocol.setArrayData(dataArray);

        NettyClient.getInstance().sendMessage(ntMessageProtocol.toString());
    }
}
