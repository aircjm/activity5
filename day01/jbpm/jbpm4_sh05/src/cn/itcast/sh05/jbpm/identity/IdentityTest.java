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
		 * ������һ�����ţ��ò��ŵ�����Ϊ��ѯ��
		 */
		processEngine.getIdentityService()
		.createGroup("��ѯ��");
		processEngine.getIdentityService()
		.createUser("����","����", "����");
		processEngine.getIdentityService()
		.createUser("��������","��������", "��������");
		processEngine.getIdentityService()
		.createUser("��������","��������", "��������");
		processEngine.getIdentityService()
		.createUser("��������","��������", "��������");
		processEngine.getIdentityService()
		.createMembership("����", "��ѯ��");
		processEngine.getIdentityService()
		.createMembership("��������", "��ѯ��");
		processEngine.getIdentityService()
		.createMembership("��������", "��ѯ��");
		processEngine.getIdentityService()
		.createMembership("��������", "��ѯ��");

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
	 * ��������ID��ѯ��ѡ��
	 */
	@Test
	public void testQueryParticipation(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
	}
}
