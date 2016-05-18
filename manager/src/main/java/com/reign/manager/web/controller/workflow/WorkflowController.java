package com.reign.manager.web.controller.workflow;

import com.alibaba.fastjson.JSONObject;
import com.reign.manager.service.workflow.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Created by ji on 16-4-28.
 */
@Controller
@RequestMapping("/reign/wf/")
public class WorkflowController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowController.class);

    @Autowired
    private WorkflowService workflowService;

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("commitWf")
    @ResponseBody
    public JSONObject commitWf(MultipartFile file, HttpServletRequest request) {
        File ff = null;
        try {
            if (file != null) {
                String tmpPath = request.getSession().getServletContext().getRealPath("/") + "upload/wf/" + file.getOriginalFilename();
                ff = new File(tmpPath);
                file.transferTo(ff);
                //todo save workflow file
                //todo 解析workflow
                workflowService.parseWf(ff);
            }
        } catch (Exception e) {
            LOGGER.error("commitWf exception", e);
        } finally {
            if (ff != null && ff.exists()) {
                try {
                    ff.delete();
                } catch (Exception e) {
                    LOGGER.error("Try to delete temporary wf definition after uploading error.", e);
                }
            }
        }

        return null;
    }
}
