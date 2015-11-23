package cn.itcast.sh05.jbpm.decision;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.junit.Test;

public class DecisionTest {
	@Test
	public void testDeploy(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addResourceFromClasspath("cn/itcast/sh05/jbpm/decision/decision.jpdl.xml")
		.deploy();
	}
	
	@Test
	public void testStartPi(){
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("outin", "b");
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceById("decision-1",variables);
	}
	
	@Test
	public void testFinishTask(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getTaskService()
		.completeTask("400003");
	}
}
