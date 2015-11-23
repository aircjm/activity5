package com.itheima09.activiti5.pi;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * pi��process instance  ����ʵ��
 * @author zd
 *   1����������ʵ��
 *   2����ѯ����
 *   3���������
 */
public class PITest {
	@Test
	public void testDeploy(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addClasspathResource("com/itheima09/activiti5/pi/qingjia.bpmn")
		.deploy();
	}
	/**
	 * 1���漰���ı�
	 *      act_ru_execution
	 *         1��˵��
	 *              ��ʾ�������е�����ʵ��
	 *         2���ֶ�
	 *              proc_inst_id  ����ʵ��ID
	 *              proc_def_id   pdid
	 *              act_id        ��ǰ�ڵ��idֵ
	 *              is_active     ��ǰ�ڵ��Ƿ��ǻ�Ծ��
	 *      act_ru_task
	 *         1��˵��
	 *              ��ʾ�������е�����
	 *         2���ֶ�
	 *              id_:����   ����ID
	 *              execution_id:piid
	 *              name_:���������
	 *              assignee_:�����ִ����
	 *      act_hi_actinst   action instance
	 *         1��˵��
	 *              ��ʾ�������л����Ѿ�������ϵĽڵ�
	 *         2���ֶ�
	 *              ID_���ڵ�ID
	 *              proc_def_id:���̶���ID
	 *              proc_inst_id:����ʵ��ID
	 *              execution_id:��������ڲ���������£���piidһ��
	 *              act_id:��ǰ�Ľڵ�ID
	 *              act_name: ��ǰ�Ľڵ������
	 *      act_hi_procinst  process instance
	 *         1��˵��
	 *              ��ʾ�������л����Ѿ���ɵ�����ʵ����
	 *         2���ֶ�
	 *              end_act_id:
	 *                  �����ֵ��˵������ʵ��������
	 *                  ���Ϊnull,˵��������ʵ�����ڽ���
	 *      act_hi_taskinst
	 *         1��˵��
	 *              ��ʾ����ִ�еĻ����Ѿ���ɵ������
	 *         2���ֶ�
	 *              delete_reason
	 *                  ɾ��ԭ��ΪʲôҪɾ������ִ�е�����
	 *                      null  ��ʾ������û�н���
	 *                      completed ��ʾ�����������
	 */
	@Test
	public void testStartPI(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		ProcessInstance pi = processEngine.getRuntimeService()
		  //.startProcessInstanceByKey(processDefinitionKey)  ����pdkey��������ʵ��  Ĭ�ϸ�����߰汾������
		 .startProcessInstanceById("qingjia:1:704");
		System.out.println(pi.getId());
		System.out.println(pi.getProcessInstanceId());
		System.out.println(pi.getProcessDefinitionId());
	}
	
	/**
	 * ��ѯ����ִ�е�����
	 */
	@Test
	public void testQueryTask(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<Task> taskList = processEngine.getTaskService()
		.createTaskQuery()
		.list();
		for(Task task:taskList){
			System.out.print("taskid:"+task.getId()+",");
			System.out.println("taskname:"+task.getName());
		}
	}
	
	/**
	 * ���Ը��������ִ���˲�ѯ��ǰ����ִ�е�����
	 */
	@Test
	public void testQueryTaskByAssignee(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<Task> taskList = processEngine.getTaskService()
		.createTaskQuery()
		.taskAssignee("����")
		.list();
		for(Task task:taskList){
			System.out.print("taskid:"+task.getId()+",");
			System.out.println("taskname:"+task.getName());
		}
	}
	
	/*
	 * ���Ը���
	 *     executionid
	 *     piid
	 *     pdkey
	 *     pdid
	 *     �����Թ�������
	 *     ���Ǹ���assignee������������õ�
	 */
	
	/**
	 * �������
	 */
	@Test
	public void testFinishTask(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService()
		.complete("1002");
	}
	
	/**
	 * �жϵ�ǰ������ʵ���Ѿ�����
	 *    ����piid��ѯ����ʵ���������ѯ�����Ľ��Ϊnull������ʵ���Ѿ�������
	 *                          �����Ϊnull,����ʵ��û�н���
	 */
	@Test
	public void testIsEndPI(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		ProcessInstance pi = processEngine.getRuntimeService()
		.createProcessInstanceQuery()
		.processInstanceId("601")
		.singleResult();
		System.out.println(pi);
	}
}
