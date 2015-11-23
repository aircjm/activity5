package cn.itcast.l_groupTask03;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class TaskTest {
	
	//创建工作流的核心对象（流程引擎）
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**部署流程定义（inputStream）+启动流程实例*/
	@Test
	public void deployementProcessDefinitionAndStartProcessInstance(){
		/**部署流程定义*/
		InputStream inBpmn = this.getClass().getResourceAsStream("task.bpmn");
		InputStream inPng = this.getClass().getResourceAsStream("task.png");
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
					.createDeployment()//创建部署对象
					.name("任务")//指定部署名称
					.addInputStream("task.bpmn", inBpmn)//使用inputStream格式的输入流完成部署
					.addInputStream("task.png", inPng)//使用inputStream格式的输入流完成部署
					.deploy();//完成部署
		System.out.println("部署ID："+deployment.getId());
		System.out.println("部署名称："+deployment.getName());
		
		/**设置流程管理的组织机构（用户、角色组）begin*/
		IdentityService identityService = processEngine.getIdentityService();
		identityService.saveGroup(new GroupEntity("部门经理"));//创建角色组（创建组织机构）
		identityService.saveGroup(new GroupEntity("总经理"));//创建角色组（创建组织机构）
		
		identityService.saveUser(new UserEntity("张三"));//创建用户
		identityService.saveUser(new UserEntity("李四"));//创建用户
		identityService.saveUser(new UserEntity("王五"));//创建用户
		
		identityService.createMembership("张三", "部门经理");//创建用户和角色的关联关系
		identityService.createMembership("李四", "部门经理");//创建用户和角色的关联关系
		identityService.createMembership("王五", "总经理");//创建用户和角色的关联关系
		/**设置流程管理的组织机构（用户、角色组）end*/
		
		/**启动流程实例*/
		String processDefinitionKey = "task";
		ProcessInstance pi = processEngine.getRuntimeService()//管理执行对象和流程实例相关的Service（正在执行）
					.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，因为当使用流程定义key的时候，默认启动的是最新版本
		System.out.println("流程实例ID："+pi.getId());
		System.out.println("流程定义ID："+pi.getProcessDefinitionId());
	}
	
	/**查询我的个人任务*/
	@Test
	public void findMyPersonalTask(){
		//任务办理人
		String assignee = "小C";
		List<Task> list = processEngine.getTaskService()//与任务相关的Service（正在执行）
						.createTaskQuery()//创建任务的查询对象
						.taskAssignee(assignee)//指定个人任务的办理人
						.orderByTaskCreateTime().asc()//按照任务的创建时间升序排列
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
	
	/**查询我的组任务*/
	@Test
	public void findGroupTask(){
		//任务办理人
		String assignee = "李四";
		List<Task> list = processEngine.getTaskService()//与任务相关的Service（正在执行）
						.createTaskQuery()//创建任务的查询对象
						.taskCandidateUser(assignee)//指定组任务的办理人
						.orderByTaskCreateTime().asc()//按照任务的创建时间升序排列
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
		String taskId = "6309";
		
		processEngine.getTaskService()//与任务相关的Service（正在执行）
						.complete(taskId);
		System.out.println("完成任务：任务ID："+taskId);
	}
	
	/**查询任务的成员（正在执行）*/
	@Test
	public void findGroupUser(){
		//任务ID
		String taskId = "5508";
		List<IdentityLink> list = processEngine.getTaskService()//
					.getIdentityLinksForTask(taskId);
		if(list!=null && list.size()>0){
			for(IdentityLink identityLink:list){
				System.out.println(identityLink.getUserId()+"    "+identityLink.getType()+"    "+identityLink.getTaskId());
			}
		}
	}
	
	/**查询历史任务的成员（正在执行）*/
	@Test
	public void findHistoryGroupUser(){
		//流程实例
		String processInstanceId = "2605";
		List<HistoricIdentityLink> list = processEngine.getHistoryService()//
						.getHistoricIdentityLinksForProcessInstance(processInstanceId);
					
		if(list!=null && list.size()>0){
			for(HistoricIdentityLink identityLink:list){
				System.out.println(identityLink.getUserId()+"    "+identityLink.getType()+"    "+identityLink.getTaskId()+"    "+identityLink.getProcessInstanceId());
			}
		}
	}
	
	/**如果是组任务，实现拾取任务，将组任务分配给某个人办理，将组任务指定个人任务*/
	@Test
	public void claim(){
		//任务ID
		String taskId = "5508";
		//指定某个人（在组任务成员之内，在组任务成员之外）办理组任务
		String userId = "大F";
		processEngine.getTaskService()//
					.claim(taskId, userId);
	}
	
	/**将个人任务回退到组任务（前提：之前组任务）*/
	@Test
	public void assignee(){
		//任务ID
		String taskId = "5508";
		processEngine.getTaskService()//
					.setAssignee(taskId, null);
	}
	
	/**向组任务中添加成员*/
	@Test
	public void addCadidateUser(){
		//任务ID
		String taskId = "5508";
		//添加的成员
		String userId = "小E";
		processEngine.getTaskService()//
					.addCandidateUser(taskId, userId);
	}
	
	/**从组任务中删除成员*/
	@Test
	public void deleteCadidateUser(){
		//任务ID
		String taskId = "5508";
		//添加的成员
		String userId = "小D";
		processEngine.getTaskService()//
					.deleteCandidateUser(taskId, userId);
	}
}
