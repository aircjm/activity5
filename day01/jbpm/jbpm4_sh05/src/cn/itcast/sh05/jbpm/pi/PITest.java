package cn.itcast.sh05.jbpm.pi;

import java.util.List;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.api.task.Task;
import org.junit.Test;

/**
 * pi=process instance  流程实例
 * @author zd
 *
 */
public class PITest {
	/**
	 * 启动流程实例
	 *    根据pdid
	 */
	@Test
	public void testStartPIByPDID(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		ProcessInstance pi = processEngine.getExecutionService()
		.startProcessInstanceById("qingjia-2");
		System.out.println(pi.getId());
	}
	
	/**
	 * 根据pdkey启动流程实例，最高版本的流程实例
	 */
	@Test
	public void testStartPIBypdkey(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceByKey("qingjia");
	}
	
	/**
	 * 查询所有的正在执行的任务查询
	 */
	@Test
	public void testQueryAllTask(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		List<Task> taskList = processEngine.getTaskService()
		.createTaskQuery()
		.list();
		for(Task task:taskList){
			System.out.println(task.getId());
			System.out.println(task.getAssignee());
			System.out.println(task.getCreateTime());
			System.out.println(task.getName());
		}
	}
	
	/**
	 * 查询登录人正在执行的任务
	 */
	@Test
	public void testQueryTaskByAssignee(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		List<Task> taskList = processEngine.getTaskService()
		.createTaskQuery()
		.assignee("张")
		.list();
		for(Task task:taskList){
			System.out.println(task.getId());
			System.out.println(task.getAssignee());
			System.out.println(task.getCreateTime());
			System.out.println(task.getName());
		}
	}
	
	/**
	 * 完成任务
	 *    当一个任务完成以后，该任务在jbpm4_task表中会删除，在相应的历史表中的state值为"completed"
	 *    当完成了最后一个任务以后，流程实例结束了，正在执行的流程实例在jbpm4_execution表中删除了
	 *    在jbpm4_hist_proceinst表中的state的值为"ended"
	 */
	@Test
	public void testFinishTask(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getTaskService()
		.completeTask("100002");
	}
	
	/**
	 * 查询已经完成的所有的任务
	 */
	@Test
	public void testQueryHistTask(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		List<HistoryTask> historyTasks = processEngine.getHistoryService()
		.createHistoryTaskQuery()
		.state("completed")
		.list();
		for(HistoryTask historyTask:historyTasks){
			System.out.println(historyTask.getAssignee());
		}
	}
	
	/**
	 * 直接结束流程实例
	 */
	@Test
	public void testEndPI(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.endProcessInstance("qingjia.120001", "failed");
	}
	
	/**
	 * 一个流程变量是可以通过该方式保存起来的
	 */
	@Test
	public void testSaveVariables(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.createVariable("qingjia.60001", "aaa", "aaa", true);
	}
}
