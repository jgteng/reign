package com.reign.runner;

import com.alibaba.fastjson.JSON;
import com.reign.domain.runtime.RunTimeBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ji on 16-5-17.
 */
public class ReignTaskRunner {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("参数不足");
            System.exit(-1);

        }
        int exitVal = -1;
        try {
            RunTimeBean runTimeBean = JSON.parseObject(args[0], RunTimeBean.class);

            List<String> command = new ArrayList<String>();
//            command.add("java");
//            command.add("-jar");
//            command.add("test.jar");
//            command.add(JSON.toJSONString(runTimeBean));


            command.add("/bin/sh");
            command.add("-c");
            command.add(" sh /tmp/test.sh");

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("/bin/sh -c sh /tmp/test.sh");
            exitVal = process.waitFor();

            Thread.sleep(5000L);
        } catch (Throwable t) {
            System.out.println("执行器执行异常");
            t.printStackTrace();
            System.exit(-1);
        }

        System.exit(exitVal);
    }
}
