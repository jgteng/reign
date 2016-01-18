package com.reign.manager.web.controller.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.reign.domain.task.Task;
import com.reign.manager.exception.ReignBaseException;
import com.reign.manager.exception.ReignManagerException;
import com.reign.manager.exception.ReignParamException;
import com.reign.manager.service.task.TaskService;
import com.reign.manager.util.PageResult;
import com.reign.manager.util.RequestUtil;
import com.reign.manager.web.ResponseCodeConstants;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by ji on 15-10-15.
 */
@Controller
@RequestMapping("/reign/task/")
public class TaskController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @RequestMapping("index")
    public String toIndex() {
        return "task/taskList";
    }

    @ResponseBody
    @RequestMapping("listTask")
    public JSONObject listTask(String paramContent) {
        PageResult response = new PageResult();
        try {
            Task task = (Task) RequestUtil.toClassBean(paramContent, Task.class);
            List<Task> taskList = taskService.listTask();
            JSONArray array = new JSONArray();
            for (int i = 0; i < 10; i++) {
                JSONObject tmpObj = new JSONObject();
                array.add(tmpObj);
                tmpObj.put("id", i);
                tmpObj.put("taskName", "name" + i);
                tmpObj.put("mainScript", "mainScript" + i);
                tmpObj.put("created", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
            response.setTotalCount(11L);
            response.setList(taskList);
        } catch (ReignParamException pe) {
            response.setCode(pe.getCode());
            response.setMessage(pe.getMessage());
        }

        return response.toJson();
    }

    @RequestMapping("to_add")
    public String toAdd() {
        return "task/taskAdd";
    }

    @ResponseBody
    @RequestMapping("add")
    public JSONObject addTask(Task paramTask) {
        PageResult response = new PageResult();
        try {
            taskService.saveTask(paramTask);
            response.setCode(ResponseCodeConstants.SUCCESS_CODE);
            response.setObj(paramTask);
        } catch (ReignManagerException re) {
            LOGGER.error("[addTask] error.", re);
            response.setCode(re.getCode());
            response.setMessage(re.getMessage());
        } catch (Exception e) {
            LOGGER.error("[addTask] failed", e);
            response.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
        }
        return response.toJson();
    }


    @RequestMapping("get")
    @ResponseBody
    public JSONObject get(Task paramTask) {
        PageResult pageResult = new PageResult();
        try {
            if (paramTask.getId() == null) {
                throw new ReignParamException(ResponseCodeConstants.PARAM_ERROR_CODE, "Task Id is null.");
            }
            Task task = taskService.getById(paramTask.getId());
            if (task != null) {
                pageResult.setObj(task);
            } else {
                pageResult.setMessage("Result Is Null");
            }
            pageResult.setCode(ResponseCodeConstants.SUCCESS_CODE);

        } catch (ReignBaseException rb) {
            LOGGER.error(rb.getCode() + "," + rb.getMessage());
            pageResult.setCode(rb.getCode());
            pageResult.setMessage(rb.getMessage());
        } catch (Exception e) {
            LOGGER.error("【Task get】failed.", e);
            pageResult.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
        }
        return pageResult.toJson();
    }

    @RequestMapping("info")
    public String getInfo(Model model, Task paramTask) {
        try {
            if (paramTask.getId() == null) {
                throw new ReignParamException(ResponseCodeConstants.PARAM_ERROR_CODE, "Task Id is null.");
            }
            Task task = taskService.getById(paramTask.getId());
            model.addAttribute("task", task);
        } catch (ReignBaseException rb) {
            LOGGER.error(rb.getCode() + "," + rb.getMessage());
        } catch (Exception e) {
            LOGGER.error("【Task get】failed.", e);
        }
        return "task/taskInfo";
    }

    @RequestMapping("del")
    @ResponseBody
    public JSONObject delTask(Task paramTask) {
        PageResult result = new PageResult();
        try {
            if (paramTask.getId() == null) {
                throw new ReignParamException(ResponseCodeConstants.PARAM_ERROR_CODE, "Task Id is null.");
            }
            taskService.delTask(paramTask.getId());
            result.setCode(ResponseCodeConstants.SUCCESS_CODE);
        } catch (ReignBaseException rb) {
            LOGGER.error(rb.getCode() + "," + rb.getMessage());
            result.setCode(rb.getCode());
            result.setMessage(rb.getMessage());
        } catch (Exception e) {
            LOGGER.error("del task error. ", e);
            result.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
        }
        return result.toJson();
    }
}
