package cn.itcast.i_start;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class StartTest {
	
	//创建工作流的核心对象（流程引擎）
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**部署流程定义（inputStream）+启动流程实例*/
	@Test
	public void deployementProcessDefinitionAndStartProcessInstance(){
		/**部署流程定义*/
		InputStream inBpmn = this.getClass().getResourceAsStream("start.bpmn");
		InputStream inPng = this.getClass().getResourceAsStream("start.png");
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
					.createDeployment()//创建部署对象
					.name("开始活动")//指定部署名称
					.addInputStream("start.bpmn", inBpmn)//使用inputStream格式的输入流完成部署
					.addInputStream("start.png", inPng)//使用inputStream格式的输入流完成部署
					.deploy();//完成部署
		System.out.println("部署ID："+deployment.getId());
		System.out.println("部署名称："+deployment.getName());
		/**启动流程实例*/
		//使用流程定义的key启动流程实例(对应Xxxxx.bpmn文件中的流程中的id属性)
		String processDefinitionKey = "start";
		ProcessInstance pi = processEngine.getRuntimeService()//管理执行对象和流程实例相关的Service（正在执行）
					.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，因为当使用流程定义key的时候，默认启动的是最新版本
		System.out.println("流程实例ID："+pi.getId());
		System.out.println("流程定义ID："+pi.getProcessDefinitionId());
		
		//判断流程是否结束
		ProcessInstance ifPi = processEngine.getRuntimeService()//查询正在执行的
								.createProcessInstanceQuery()//按照流程实例进行查询
								.processInstanceId(pi.getId())//按照流程实例ID查询
								.singleResult();
		//流程结束
		if(ifPi==null){
			System.out.println("流程结束！");
			//查询历史表，获取流程实例的对象
			HistoricProcessInstance hpi = processEngine.getHistoryService()//
							.createHistoricProcessInstanceQuery()//
							.processInstanceId(pi.getId())//使用流程实例ID查询
							.singleResult();
			System.out.println(hpi.getId()+"      "+hpi.getStartTime()+"       "+hpi.getEndTime()+"      "+hpi.getDurationInMillis());
		}
	}
	
	
}
