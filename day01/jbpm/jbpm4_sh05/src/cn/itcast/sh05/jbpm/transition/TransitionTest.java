package cn.itcast.sh05.jbpm.transition;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.junit.Test;

public class TransitionTest {
	@Test
	public void testDeploy(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addResourceFromClasspath("cn/itcast/sh05/jbpm/transition/transition.jpdl.xml")
		.deploy();
	}
	
	@Test
	public void testStartPI(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceById("transition-1");
	}
	
	@Test
	public void testFinishTask(){
		/**
		 * 该参数表示选择的去处
		 */
		String outcome = "to end1";
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getTaskService()
		.completeTask("360002",outcome);
	}
}
