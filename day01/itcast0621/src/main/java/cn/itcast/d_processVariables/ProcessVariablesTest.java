package cn.itcast.d_processVariables;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ProcessVariablesTest {
	
	//创建工作流的核心对象（流程引擎）
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**部署流程定义（zip）*/
	@Test
	public void deployementProcessDefinition_inputStream(){
		InputStream inbpmn = this.getClass().getResourceAsStream("/diagrams/processVariables.bpmn");
		InputStream inpng = this.getClass().getResourceAsStream("/diagrams/processVariables.png");
		
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
					.createDeployment()//创建部署对象
					.name("请假流程")//指定部署名称
					.addInputStream("processVariables.bpmn", inbpmn)//使用InputStream加载资源文件，注意：resourceName一定和文件的名称要一致
					.addInputStream("processVariables.png", inpng)//
					.deploy();//完成部署
		System.out.println("部署ID："+deployment.getId());
		System.out.println("部署名称："+deployment.getName());
	}
	
	/**启动流程实例*/
	@Test
	public void startProcessInstance(){
		//使用流程定义的key启动流程实例(对应helloworld.bpmn文件中的流程中的id属性)
		String processDefinitionKey = "processVariables";
		ProcessInstance pi = processEngine.getRuntimeService()//管理执行对象和流程实例相关的Service（正在执行）
					.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，因为当使用流程定义key的时候，默认启动的是最新版本
		System.out.println("流程实例ID："+pi.getId());
		System.out.println("流程定义ID："+pi.getProcessDefinitionId());
		
	}
	
	/**设置流程变量*/
	@Test
	public void setVariables(){
		//使用执行的Service操作流程变量
		TaskService taskService = processEngine.getTaskService();
		//指定任务ID
		String taskId = "2104";
		/**基本类型的设置*/
//		taskService.setVariable(taskId, "请假日期", new Date());
//		taskService.setVariableLocal(taskId, "请假天数", 3);
//		taskService.setVariable(taskId, "请假原因", "回家探亲");
		/**使用javabean对象*/
		/***注意：javabean对象一定要实现 implements java.io.Serializable*/
		Person p = new Person();
		p.setId(10);
		p.setName("张三丰");
		taskService.setVariable(taskId, "人员信息(添加versionID固定)", p);
	}
	
	/**获取流程变量*/
	@Test
	public void getVariables(){
		//使用执行的Service操作流程变量
		TaskService taskService = processEngine.getTaskService();
		//指定任务ID
		String taskId = "2104";
		/**基本类型的设置*/
//		Date nowdate = (Date) taskService.getVariable(taskId, "请假日期");
//		Integer daycount = (Integer) taskService.getVariable(taskId, "请假天数");
//		String reason = (String) taskService.getVariable(taskId, "请假原因");
//		System.out.println(nowdate + "        "+daycount+"         "+reason);
		/**使用javabean类型*/
		/**
		 * 注意：在javabean对象中指定：private static final long serialVersionUID = 6757393795687480331L;
		 *   如果不指定，此时改变javabean中属性的时候，版本号发生变化，此时获取流程变量的时候，就会抛出异常
		 */
		Person p = (Person) taskService.getVariable(taskId, "人员信息(添加versionID固定)");
		System.out.println(p.getId()+"       "+p.getName());
	}
	
	
	/**模拟操作流程变量的场景*/
	public void setAndGetProcessVariables(){
		TaskService taskService = processEngine.getTaskService();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		/**设置流程变量*/
//		taskService.setVariable(taskId, variableName, value);//使用任务ID，流程变量的名称，流程变量的值进行设置，一次只能设置一个值
//		taskService.setVariables(taskId, variables);//使用任务ID，和Map<String,Object>集合设置流程变量，map集合的key表示流程变量的名称，map集合的value表示流程变量的值，一次可以设置多个值
		
//		runtimeService.setVariable(executionId, variableName, value);//使用执行ID，流程变量的名称，流程变量的值进行设置，一次只能设置一个值
//		runtimeService.setVariables(executionId, variables);//使用执行对象ID，和Map<String,Object>集合设置流程变量，map集合的key表示流程变量的名称，map集合的value表示流程变量的值，一次可以设置多个值
		
//		runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);//启动流程实例的同时设置流程变量
//		taskService.complete(taskId, variables);//完成任务的同时，设置流程变量
		
		/**获取流程变量*/
//		taskService.getVariable(taskId, variableName);//使用任务ID，流程变量的名称，获取流程变量的值，一次只能获取一个值
//		taskService.getVariables(taskId);//使用任务ID，获取所有的流程变量,获取流程变量，存放到Map<String,Object>
//		taskService.getVariables(taskId, variableNames);//使用任务ID，指定流程变量的名称（存放到集合中）,获取流程变量，存放到Map<String,Object>
		
//		runtimeService.getVariable(executionId, variableName)//使用执行对象ID，流程变量的名称，获取流程变量的值，一次只能获取一个值
//		runtimeService.getVariables(executionId);//使用执行对象ID，获取所有的流程变量,获取流程变量，存放到Map<String,Object>
//		runtimeService.getVariables(executionId, variableNames);//使用执行对象ID，指定流程变量的名称（存放到集合中）,获取流程变量，存放到Map<String,Object>
	}
	
	/**完成任务*/
	@Test
	public void completeTask(){
		//任务ID
		String taskId = "2402";
		processEngine.getTaskService()//与任务相关的Service（正在执行）
						.complete(taskId);
		System.out.println("完成任务：任务ID："+taskId);
	}
	
	/**查询历史的流程变量*/
	@Test
	public void findHistoryVariables(){
		List<HistoricVariableInstance> list = processEngine.getHistoryService()//
						.createHistoricVariableInstanceQuery()//
						.variableName("请假天数")//
						.list();
		if(list!=null && list.size()>0){
			for(HistoricVariableInstance hvi:list){
				System.out.println(hvi.getId()+"    "+hvi.getProcessInstanceId()+"   "+hvi.getVariableName()+"   "+hvi.getValue()+"    "+hvi.getVariableTypeName());
			}
		}
	}
	
}
