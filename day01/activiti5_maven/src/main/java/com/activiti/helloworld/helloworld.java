package com.activiti.helloworld;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

/**
 * Created by jiaming.chen1 on 2015/11/24.
 */
public class helloworld {


    //将流程图部署到项目中
    @Test
    public void testDeploy(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRepositoryService().createDeployment()
                //在Maven项目中这些文件是在resources文件夹中
                .addClasspathResource("helloworld.bpmn")
                .addClasspathResource("helloworld.png")
                .deploy();
    }

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcessInstance(){
        //pdkey是流程定义的名称 对应的是act_re_procdef表中的KEY_
        String pdkey = "itheima09";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRuntimeService()
                .startProcessInstanceByKey(pdkey);
    }

@Test
    public void testfirstProcesInstance(){
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    processEngine.getTaskService().complete("7502");
}



}
