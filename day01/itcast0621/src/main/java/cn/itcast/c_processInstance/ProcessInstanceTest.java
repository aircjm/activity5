package cn.itcast.c_processInstance;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ProcessInstanceTest {
	
	//创建工作流的核心对象（流程引擎）
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**部署流程定义（zip）*/
	@Test
	public void deployementProcessDefinition_zip(){
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
					.createDeployment()//创建部署对象
					.name("请假流程")//指定部署名称
					.addZipInputStream(zipInputStream)//使用zip格式的输入流完成部署
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
		System.out.println("流程实例ID："+pi.getId());
		System.out.println("流程定义ID："+pi.getProcessDefinitionId());
		
	}
	
	/**查询我的个人任务*/
	@Test
	public void findMyPersonalTask(){
		//任务办理人
		String assignee = "王五";
		List<Task> list = processEngine.getTaskService()//与任务相关的Service（正在执行）
						.createTaskQuery()//创建任务的查询对象
						/**where条件*/
						.taskAssignee(assignee)//指定个人任务的办理人
//						.taskCandidateUser(candidateUser)//组任务的办理人查询
//						.processInstanceId(processInstanceId)//按照流程实例ID查询
						/**排序*/
						.orderByTaskCreateTime().asc()//按照任务的创建时间升序排列
						/**返回的结果*/
//						.count()//返回结果集数量
//						.listPage(firstResult, maxResults)//分页查询
//						.singleResult()//惟一结果集
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
		String taskId = "1302";
		processEngine.getTaskService()//与任务相关的Service（正在执行）
						.complete(taskId);
		System.out.println("完成任务：任务ID："+taskId);
	}
	
	/**查询流程状态（判断流程正在执行，还是结束）*/
	@Test
	public void processInstanceIsEnd(){
		//流程实例ID
		String processInstanceId = "1101";
		//查询正在执行的执行对象表
		ProcessInstance pi = processEngine.getRuntimeService()//管理执行对象和流程实例相关的Service（正在执行）
						.createProcessInstanceQuery()//创建流程实例查询
						.processInstanceId(processInstanceId)//按照流程实例ID查询
						.singleResult();
		if(pi==null){
			System.out.println("流程结束了！");
		}
		else{
			System.out.println("流程没有结束！");
		}
		
	}
	
	/**历史任务*/
	@Test
	public void findHistoryTask(){
		//办理人
		String taskAssignee = "张三";
		List<HistoricTaskInstance> list = processEngine.getHistoryService()//
					.createHistoricTaskInstanceQuery()//创建历史任务实例，操作历史任务表
					.taskAssignee(taskAssignee)//指定任务的办理人
					.list();
		if(list!=null && list.size()>0){
			for(HistoricTaskInstance hti:list){
				System.out.println("任务ID："+hti.getId());
				System.out.println("任务的办理人："+hti.getAssignee());
				System.out.println("流程实例ID："+hti.getProcessInstanceId());
				System.out.println(hti.getStartTime()+"      "+hti.getEndTime()+"       "+hti.getDurationInMillis());
				System.out.println("##########################3");
			}
		}
		
	}
}
