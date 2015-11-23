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
	 * 当进入任务节点以后，候选人被保存在了jbpm4_participation表中
	 */
	@Test
	public void testStartPI(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceById("grouptask-1");
	}
	
	/**
	 * 根据候选人查看组任务
	 */
	@Test
	public void testQueryGroupTask(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		List<Task> tasks = processEngine.getTaskService()
		.findGroupTasks("工程师1");
		for(Task task:tasks){
			System.out.println(task.getName());
		}
	}
	
	/**
	 * 根据任务查询候选人
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
	 * 某一个人接收该任务
	 */
	@Test
	public void testTakeTask(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getTaskService()
		.takeTask("270002", "工程师1");
	}
}
