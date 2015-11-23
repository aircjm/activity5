package cn.itcast.sh05.jbpm.forkjoin;

import java.util.List;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.task.Task;
import org.junit.Test;

public class ForkJoinTest {
	@Test
	public void testDeploy(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getRepositoryService().createDeployment()
		.addResourceFromClasspath("cn/itcast/sh05/jbpm/forkjoin/forkjoin.jpdl.xml")
		.deploy();
	}
	
	@Test
	public void testStartPI(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceById("forkjoin-1");
	}
	
	@Test
	public void testQueryTaskByExecutionId(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		Task task = processEngine.getTaskService()
		.createTaskQuery()
		.executionId("forkjoin.430001.to task2.430003")
		.uniqueResult();
		System.out.println(task.getName());
	}
	
	@Test
	public void testQueryTaskByPIID(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		List<Task> tasks = processEngine.getTaskService()
		.createTaskQuery()
		.processInstanceId("forkjoin.430001")
		.list();
		System.out.println(tasks.size());
	}
}
