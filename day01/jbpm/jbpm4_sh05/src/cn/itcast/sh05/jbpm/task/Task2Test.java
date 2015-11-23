package cn.itcast.sh05.jbpm.task;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.junit.Test;

public class Task2Test {
	@Test
	public void testDeploy(){
		ProcessEngine processEngine =  Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addResourceFromClasspath("cn/itcast/sh05/jbpm/task/task2.jpdl.xml")
		.deploy();
	}
	
	@Test
	public void testStartPI(){
		ProcessEngine processEngine =  Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceById("task2-1");
	}
	
	/**
	 * 通过程序，给当前正在执行的任务赋值执行人
	 */
	@Test
	public void testAssigneeToTask(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getTaskService()
		.assignTask("230002", "王二麻子");
	}
}
