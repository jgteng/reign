package com.reign.manager.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.reign.domain.script.Script;
import com.reign.domain.task.Task;
import com.reign.manager.exception.ReignParamException;
import com.reign.manager.service.script.ScriptService;
import com.reign.manager.util.PageResult;
import com.reign.manager.util.RequestUtil;
import com.reign.manager.web.ResponseCodeConstants;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by ji on 15-12-10.
 */
@Controller
@RequestMapping("/reign/script/")
public class ScriptController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptController.class);

    @Autowired
    private ScriptService scriptService;

    @RequestMapping("index")
    public String toIndex() {
        return "script/scriptList";
    }

    @ResponseBody
    @RequestMapping("listScript")
    public JSONObject listTask(String paramContent) {
        PageResult response = new PageResult();
        try {
            Script script = (Script) RequestUtil.toClassBean(paramContent, Script.class);
            List<Script> scriptList = scriptService.listScript(script);
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
            response.setList(scriptList);
        } catch (ReignParamException pe) {
            response.setCode(pe.getCode());
            response.setMessage(pe.getMessage());
        } catch (Exception e) {
            LOGGER.error("查询脚本列表异常", e);
            response.setCode(ResponseCodeConstants.SYS_ERROR_CODE);
            response.setMessage(e.getMessage());
        }

        return response.toJson();
    }

    @RequestMapping("upload")
    @ResponseBody
    public JSONObject uploadScriptFile(MultipartFile file, HttpServletRequest request) {
        File ff = null;
        try {
            if (file != null) {
                String tmpPath = request.getSession().getServletContext().getRealPath("/") + "upload/" + file.getOriginalFilename();
                ff = new File(tmpPath);
                file.transferTo(ff);
                scriptService.saveFile(ff);
            }
        } catch (Exception e) {
            LOGGER.error("upload script file exception", e);
        } finally {
            if (ff != null && ff.exists()) {
                try {
                    ff.delete();
                } catch (Exception e) {
                    LOGGER.error("Try to delete temporary script file after uploading error.", e);
                }
            }
        }

        return null;
    }

    @RequestMapping("download")
    public void download(String fileName, HttpServletResponse response) {
        try {
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/x-msdownload");
            byte[] bytes = scriptService.getFile();
            while (bytes != null) {
                toClient.write(bytes);
                bytes = scriptService.getFile();
            }
        } catch (Exception e) {
            LOGGER.error("Download script file exception. ", e);
        }
    }

}
