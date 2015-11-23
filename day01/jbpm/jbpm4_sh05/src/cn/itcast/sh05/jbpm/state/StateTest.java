package cn.itcast.sh05.jbpm.state;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.junit.Test;

public class StateTest {
	@Test
	public void testDeploy(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addResourceFromClasspath("cn/itcast/sh05/jbpm/state/state.jpdl.xml")
		.deploy();
	}
	
	@Test
	public void testStartpi(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceById("state-1");
	}
	
	@Test
	public void testNextNode(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.signalExecutionById("state.380001");
	}
}
