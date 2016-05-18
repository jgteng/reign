package com.reign.manager.service.workflow;

import com.reign.component.constants.WorkflowConstant;
import com.reign.component.exception.WorkflowDefinitionException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by ji on 16-4-29.
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {
    @Override
    public void parseWf(File ff) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = builder.parse(ff);
        org.w3c.dom.Element root = document.getDocumentElement();
        String wfTag = root.getTagName();

        if (!StringUtils.equals(wfTag, WorkflowConstant.WORKFLOW_TAG)) {
            throw new WorkflowDefinitionException("Workflow definition error,[start tag:" + wfTag + "]");
        }

        String wfName = root.getAttribute("name");
        if (StringUtils.isEmpty(wfName)) {
            throw new WorkflowDefinitionException("Workflow name can not be null");
        }

        NodeList startNodeList = root.getElementsByTagName("start");
        if (startNodeList == null && startNodeList.getLength() != 1) {
            throw new WorkflowDefinitionException("workflow must contain one and only one start node");
        }
    }
}
