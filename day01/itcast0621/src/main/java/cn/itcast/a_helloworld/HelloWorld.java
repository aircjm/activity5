package cn.itcast.a_helloworld;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class HelloWorld {
	
	//创建工作流的核心对象（流程引擎）
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**部署流程定义（classpath）*/
	@Test
	public void deployementProcessDefinition(){
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
					.createDeployment()//创建部署对象
					.name("请假流程")//指定部署名称
					.addClasspathResource("diagrams/helloworld.bpmn")//从类路径加载资源文件，一次只能加载一个文件
					.addClasspathResource("diagrams/helloworld.png")//从类路径加载资源文件，一次只能加载一个文件
					.deploy();//完成部署
		System.out.println("部署ID："+deployment.getId());
		System.out.println("部署名称："+deployment.getName());
	}
	
	/**启动流程实例*/
	@Test
	public void startProcessInstance(){
		//使用流程定义的key启动流程实例(对应helloworld.bpmn文件中的流程中的id属性)
		String processDefinitionKey = "helloworld";
		ProcessInstance pi = processEngine.getRuntimeService()//管理执行对象和流程实例相关的Service（正在执行）
					.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，因为当使用流程定义key的时候，默认启动的是最新版本
		System.out.println("流程实例ID："+pi.getId());//101
		System.out.println("流程定义ID："+pi.getProcessDefinitionId());//helloworld:1:4
		
	}
	
	/**查询我的个人任务*/
	@Test
	public void findMyPersonalTask(){
		//任务办理人
		String assignee = "王五";
		List<Task> list = processEngine.getTaskService()//与任务相关的Service（正在执行）
						.createTaskQuery()//创建任务的查询对象
						.taskAssignee(assignee)//指定个人任务的办理人
						.list();
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("任务ID："+task.getId());
				System.out.println("任务名称："+task.getName());
				System.out.println("任务的创建时间："+task.getCreateTime());
				System.out.println("任务的办理人："+task.getAssignee());
				System.out.println("流程实例ID："+task.getProcessInstanceId());//一个流程启动后。流程实例只有1个 //101
				System.out.println("执行对象ID："+task.getExecutionId());//101
				System.out.println("流程定义ID："+task.getProcessDefinitionId());//helloworld:1:4
			}
		}				
	}
	
	/**完成任务*/
	@Test
	public void completeTask(){
		//任务ID
		String taskId = "302";
		processEngine.getTaskService()//与任务相关的Service（正在执行）
						.complete(taskId);
		System.out.println("完成任务：任务ID："+taskId);
	}
}
