package cn.itcast.sh05.jbpm.swimlane;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.junit.Test;

public class SwimlaneTest {
	@Test
	public void testDeploy(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addResourceFromClasspath("cn/itcast/sh05/jbpm/swimlane/swimlane.jpdl.xml")
		.deploy();
	}
	
	@Test
	public void testStartPI(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceById("swimlane-1");
	}
	
	@Test
	public void testFinishTask(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getTaskService()
		.completeTask("330002");
	}
}
