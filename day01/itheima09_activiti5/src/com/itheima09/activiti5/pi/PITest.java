package com.itheima09.activiti5.pi;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * pi是process instance  流程实例
 * @author zd
 *   1、启动流程实例
 *   2、查询任务
 *   3、完成任务
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
	 * 1、涉及到的表
	 *      act_ru_execution
	 *         1、说明
	 *              表示正在运行的流程实例
	 *         2、字段
	 *              proc_inst_id  流程实例ID
	 *              proc_def_id   pdid
	 *              act_id        当前节点的id值
	 *              is_active     当前节点是否是活跃的
	 *      act_ru_task
	 *         1、说明
	 *              表示正在运行的任务
	 *         2、字段
	 *              id_:主键   任务ID
	 *              execution_id:piid
	 *              name_:任务的名称
	 *              assignee_:任务的执行人
	 *      act_hi_actinst   action instance
	 *         1、说明
	 *              表示正在运行或者已经运行完毕的节点
	 *         2、字段
	 *              ID_：节点ID
	 *              proc_def_id:流程定义ID
	 *              proc_inst_id:流程实例ID
	 *              execution_id:如果不存在并发的情况下，和piid一样
	 *              act_id:当前的节点ID
	 *              act_name: 当前的节点的名称
	 *      act_hi_procinst  process instance
	 *         1、说明
	 *              表示正在运行或者已经完成的流程实例表
	 *         2、字段
	 *              end_act_id:
	 *                  如果有值，说明流程实例结束了
	 *                  如果为null,说明该流程实例正在进行
	 *      act_hi_taskinst
	 *         1、说明
	 *              表示正在执行的或者已经完成的任务表
	 *         2、字段
	 *              delete_reason
	 *                  删除原因：为什么要删除正在执行的任务
	 *                      null  表示该任务没有结束
	 *                      completed 表示该任务结束了
	 */
	@Test
	public void testStartPI(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		ProcessInstance pi = processEngine.getRuntimeService()
		  //.startProcessInstanceByKey(processDefinitionKey)  根据pdkey启动流程实例  默认根据最高版本的启动
		 .startProcessInstanceById("qingjia:1:704");
		System.out.println(pi.getId());
		System.out.println(pi.getProcessInstanceId());
		System.out.println(pi.getProcessDefinitionId());
	}
	
	/**
	 * 查询正在执行的任务
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
	 * 可以根据任务的执行人查询当前正在执行的任务
	 */
	@Test
	public void testQueryTaskByAssignee(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<Task> taskList = processEngine.getTaskService()
		.createTaskQuery()
		.taskAssignee("张三")
		.list();
		for(Task task:taskList){
			System.out.print("taskid:"+task.getId()+",");
			System.out.println("taskname:"+task.getName());
		}
	}
	
	/*
	 * 可以根据
	 *     executionid
	 *     piid
	 *     pdkey
	 *     pdid
	 *     都可以过滤任务
	 *     但是根据assignee过滤任务是最常用的
	 */
	
	/**
	 * 完成任务
	 */
	@Test
	public void testFinishTask(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getTaskService()
		.complete("1002");
	}
	
	/**
	 * 判断当前的流程实例已经结束
	 *    根据piid查询流程实例，如果查询出来的结果为null，流程实例已经结束了
	 *                          如果不为null,流程实例没有结束
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
