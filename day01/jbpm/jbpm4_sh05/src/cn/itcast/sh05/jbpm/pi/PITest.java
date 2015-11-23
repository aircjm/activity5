package cn.itcast.sh05.jbpm.pi;

import java.util.List;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.history.HistoryTask;
import org.jbpm.api.task.Task;
import org.junit.Test;

/**
 * pi=process instance  ����ʵ��
 * @author zd
 *
 */
public class PITest {
	/**
	 * ��������ʵ��
	 *    ����pdid
	 */
	@Test
	public void testStartPIByPDID(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		ProcessInstance pi = processEngine.getExecutionService()
		.startProcessInstanceById("qingjia-2");
		System.out.println(pi.getId());
	}
	
	/**
	 * ����pdkey��������ʵ������߰汾������ʵ��
	 */
	@Test
	public void testStartPIBypdkey(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceByKey("qingjia");
	}
	
	/**
	 * ��ѯ���е�����ִ�е������ѯ
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
	 * ��ѯ��¼������ִ�е�����
	 */
	@Test
	public void testQueryTaskByAssignee(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		List<Task> taskList = processEngine.getTaskService()
		.createTaskQuery()
		.assignee("��")
		.list();
		for(Task task:taskList){
			System.out.println(task.getId());
			System.out.println(task.getAssignee());
			System.out.println(task.getCreateTime());
			System.out.println(task.getName());
		}
	}
	
	/**
	 * �������
	 *    ��һ����������Ժ󣬸�������jbpm4_task���л�ɾ��������Ӧ����ʷ���е�stateֵΪ"completed"
	 *    ����������һ�������Ժ�����ʵ�������ˣ�����ִ�е�����ʵ����jbpm4_execution����ɾ����
	 *    ��jbpm4_hist_proceinst���е�state��ֵΪ"ended"
	 */
	@Test
	public void testFinishTask(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getTaskService()
		.completeTask("100002");
	}
	
	/**
	 * ��ѯ�Ѿ���ɵ����е�����
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
	 * ֱ�ӽ�������ʵ��
	 */
	@Test
	public void testEndPI(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.endProcessInstance("qingjia.120001", "failed");
	}
	
	/**
	 * һ�����̱����ǿ���ͨ���÷�ʽ����������
	 */
	@Test
	public void testSaveVariables(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.createVariable("qingjia.60001", "aaa", "aaa", true);
	}
}
