package cn.itcast.sh05.jbpm.event;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.junit.Test;

public class EventTest {
	@Test
	public void testDeploy(){
		ProcessEngine processEngine =  Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addResourceFromClasspath("cn/itcast/sh05/jbpm/event/event.jpdl.xml")
		.deploy();
	}
	
	@Test
	public void testStartPI(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceById("event-1");
	}
}
