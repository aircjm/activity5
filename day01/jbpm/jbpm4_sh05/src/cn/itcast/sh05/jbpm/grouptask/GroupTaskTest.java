package cn.itcast.sh05.jbpm.grouptask;

import java.util.List;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.task.Participation;
import org.jbpm.api.task.Task;
import org.junit.Test;

public class GroupTaskTest {
	@Test
	public void testDeploy(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addResourceFromClasspath("cn/itcast/sh05/jbpm/grouptask/grouptask.jpdl.xml")
		.deploy();
	}
	
	/**
	 * ����������ڵ��Ժ󣬺�ѡ�˱���������jbpm4_participation����
	 */
	@Test
	public void testStartPI(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceById("grouptask-1");
	}
	
	/**
	 * ���ݺ�ѡ�˲鿴������
	 */
	@Test
	public void testQueryGroupTask(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		List<Task> tasks = processEngine.getTaskService()
		.findGroupTasks("����ʦ1");
		for(Task task:tasks){
			System.out.println(task.getName());
		}
	}
	
	/**
	 * ���������ѯ��ѡ��
	 */
	@Test
	public void testQueryCandidate(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		List<Participation> pList = processEngine.getTaskService()
		.getTaskParticipations("270002");
		for(Participation p:pList){
			System.out.println(p.getUserId());
		}
	}
	
	/**
	 * ĳһ���˽��ո�����
	 */
	@Test
	public void testTakeTask(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getTaskService()
		.takeTask("270002", "����ʦ1");
	}
}
