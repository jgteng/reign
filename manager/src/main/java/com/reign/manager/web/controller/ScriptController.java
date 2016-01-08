package com.reign.manager.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.reign.manager.service.script.ScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;

/**
 * Created by ji on 15-12-10.
 */
@RequestMapping("/reign/script/")
public class ScriptController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptController.class);

    @Autowired
    private ScriptService scriptService;

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
