package cn.itcast.sh05.jbpm.identity;

import java.util.List;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.identity.Group;
import org.jbpm.api.task.Participation;
import org.junit.Test;

public class IdentityTest {
	@Test
	public void testIdentity(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		/**
		 * 创建了一个部门，该部门的名称为咨询部
		 */
		processEngine.getIdentityService()
		.createGroup("咨询部");
		processEngine.getIdentityService()
		.createUser("张三","张三", "张三");
		processEngine.getIdentityService()
		.createUser("王二麻子","王二麻子", "王二麻子");
		processEngine.getIdentityService()
		.createUser("王二麦子","王二麦子", "王二麦子");
		processEngine.getIdentityService()
		.createUser("王二妹子","王二妹子", "王二妹子");
		processEngine.getIdentityService()
		.createMembership("张三", "咨询部");
		processEngine.getIdentityService()
		.createMembership("王二麻子", "咨询部");
		processEngine.getIdentityService()
		.createMembership("王二麦子", "咨询部");
		processEngine.getIdentityService()
		.createMembership("王二妹子", "咨询部");

	}
	
	@Test
	public void testDeploy(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addResourceFromClasspath("cn/itcast/sh05/jbpm/identity/identity.jpdl.xml")
		.deploy();
	}
	
	@Test
	public void testStartPi(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceById("identity-1");
	}
	
	/**
	 * 根据任务ID查询候选人
	 */
	@Test
	public void testQueryParticipation(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
	}
}
