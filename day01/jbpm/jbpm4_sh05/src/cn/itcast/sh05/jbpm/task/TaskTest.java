package cn.itcast.sh05.jbpm.task;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.junit.Test;

public class TaskTest {
	@Test
	public void testDeploy(){
		ProcessEngine processEngine =  Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addResourceFromClasspath("cn/itcast/sh05/jbpm/task/task.jpdl.xml")
		.deploy();
	}
	
	@Test
	public void testStartPI(){
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("applicator", "aaa");
		ProcessEngine processEngine =  Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceById("task-1",variables);
	}
	
	/**
	 * ������������������ʱ�򣬱�������ž���������ֵ�����ִ����
	 */
	@Test
	public void testFinishTask(){
		 Map<String, String> variables = new HashMap<String, String>();
		 variables.put("manager", "asfd");
		 ProcessEngine processEngine = Configuration.getProcessEngine();
		 processEngine.getTaskService()
		 .setVariables("190003", variables);
		 processEngine.getTaskService()
		 .completeTask("190003");
	}
}
