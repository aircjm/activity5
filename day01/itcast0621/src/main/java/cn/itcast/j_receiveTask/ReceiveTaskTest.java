package cn.itcast.j_receiveTask;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ReceiveTaskTest {
	
	//创建工作流的核心对象（流程引擎）
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**部署流程定义（inputStream）+启动流程实例*/
	@Test
	public void deployementProcessDefinitionAndStartProcessInstance(){
		/**部署流程定义*/
		InputStream inBpmn = this.getClass().getResourceAsStream("receiveTask.bpmn");
		InputStream inPng = this.getClass().getResourceAsStream("receiveTask.png");
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
					.createDeployment()//创建部署对象
					.name("接收活动（使流程处于等待状态）")//指定部署名称
					.addInputStream("receiveTask.bpmn", inBpmn)//使用inputStream格式的输入流完成部署
					.addInputStream("receiveTask.png", inPng)//使用inputStream格式的输入流完成部署
					.deploy();//完成部署
		System.out.println("部署ID："+deployment.getId());
		System.out.println("部署名称："+deployment.getName());
		/**启动流程实例*/
		//使用流程定义的key启动流程实例(对应Xxxxx.bpmn文件中的流程中的id属性)
		String processDefinitionKey = "receiveTask";
		ProcessInstance pi = processEngine.getRuntimeService()//管理执行对象和流程实例相关的Service（正在执行）
					.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，因为当使用流程定义key的时候，默认启动的是最新版本
		System.out.println("流程实例ID："+pi.getId());
		System.out.println("流程定义ID："+pi.getProcessDefinitionId());
		
		/**查询正在执行的执行对象表*/
		Execution execution = processEngine.getRuntimeService()//
					.createExecutionQuery()//使用执行对象查询
					.processInstanceId(pi.getId())//按照流程实例ID查询
					.activityId("receivetask1")//
					.singleResult();
		
		/**汇总当日销售额，使用流程变量汇总当日销售额*/
		processEngine.getRuntimeService()//
					.setVariable(execution.getId(), "当日销售额", 5000);
		
		/**使用执行对象ID，向后执行一步，让流程继续执行*/
		processEngine.getRuntimeService()//
					.signal(execution.getId());
		
		
		/**查询正在执行的执行对象表*/
		Execution execution1 = processEngine.getRuntimeService()//
						.createExecutionQuery()//使用执行对象查询
						.processInstanceId(pi.getId())//按照流程实例ID查询
						.activityId("receivetask2")//
						.singleResult();
		
		/**从流程变量中获取当日销售额的值*/
		Integer value = (Integer) processEngine.getRuntimeService().getVariable(execution1.getId(), "当日销售额");
		System.out.println("给老板发送短信：金额是："+value);
		
		/**使用执行对象ID，向后执行一步，让流程继续执行*/
		processEngine.getRuntimeService()//
					.signal(execution1.getId());
		
		
	}
	
	
}
