package com.itheima09.activiti5.helloworld;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

/*
 * 1��������ͼ
 * 2���Ѹ����̲���activiti5��������
 * 3����������ʵ��
 * 4�����
 * 5�����ž�������
 * 6���ܾ�������
 */
public class HelloWorldTest {
	/**
	 * ������ͼ��������������
	 */
	@Test
	public void testDeploy(){
		//��ȡ��������
		ProcessEngine processEngine =  ProcessEngines.getDefaultProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addClasspathResource("com/itheima09/activiti5/helloworld/helloworld.bpmn")
		.addClasspathResource("com/itheima09/activiti5/helloworld/helloworld.png")
		.deploy();
	}
	
	/**
	 * ��������ʵ��
	 */
	@Test
	public void testStartProcessInstance(){
		//pdkey�����̶��������
		String pdkey = "myProcess";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRuntimeService()
		.startProcessInstanceByKey(pdkey);
	}
	
	/**
	 * ���������������
	 */
	@Test
	public void testFinishTask_Applicator(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService()
		.complete("104");
	}
	
	/**
	 * ��ɲ��ž�������������
	 */
	@Test
	public void testFinshTask_ManagerApprove(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService()
		.complete("202");
	}
	
	/**
	 * ����ܾ�������������
	 */
	@Test
	public void testFinshTask_BossApprove(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService()
		.complete("302");
	}
}
