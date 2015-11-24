package com.itheima09.activiti5.helloworld;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

/*
 * 1、画流程图
 * 2、把该流程部署到activiti5的引擎中
 * 3、启动流程实例
 * 4、请假
 * 5、部门经理审批
 * 6、总经理审批
 */
public class HelloWorldTest {
	/**
	 * 把流程图部署到流程引擎中
	 */
	@Test
	public void testDeploy(){
		//获取流程引擎
		ProcessEngine processEngine =  ProcessEngines.getDefaultProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		//部署流程图
		.addClasspathResource("com/itheima09/activiti5/helloworld/helloworld.bpmn")
		.addClasspathResource("com/itheima09/activiti5/helloworld/helloworld.png")
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
	
	/**
	 * 完成请假申请的任务
	 */
	@Test
	public void testFinishTask_Applicator(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService()
		.complete("201");
	}
	
	/**
	 * 完成部门经理审批的任务
	 */
	@Test
	public void testFinshTask_ManagerApprove(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService()
		.complete("204");
	}
	
	/**
	 * 完成总经理审批的任务
	 */
	@Test
	public void testFinshTask_BossApprove(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService()
		.complete("302");
	}
}
