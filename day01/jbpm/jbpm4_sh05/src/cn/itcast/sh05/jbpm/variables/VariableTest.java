package cn.itcast.sh05.jbpm.variables;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.junit.Test;

/**
 * 流程变量
 * @author zd
 *
 */
public class VariableTest {
	/**
	 * 在启动流程实例的时候，设置流程变量
	 *    
	 */
	@Test
	public void testStartPi(){
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("启动流程实例", "在启动流程实例的时候设置流程变量");
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceById("qingjia-1", variables);
	}
	
	/**
	 * 在完成任务的时候设置流程变量
	 */
	@Test
	public void testWhenTask(){
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("请假天数", "3天");
		variables.put("请假原因", "约会");
		ProcessEngine processEngine = Configuration.getProcessEngine();
		/**
		 * 在taskid为120003的时候，设置流程变量
		 */
		processEngine.getTaskService()
		.setVariables("120003", variables);
		processEngine.getTaskService()
		.completeTask("120003");
	}
	
	/**
	 * 流程实例只要没有结束，都可以设置流程变量
	 */
	@Test
	public void testSetVariablesWhenPi(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.setVariable("qingjia.120001", "aaa", "aaa");
	}
	
	/**
	 * 获取流程变量
	 */
	@Test
	public void testGetVariables(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		String s= processEngine.getExecutionService()
		/**
		 * 第一个参数为executionId
		 * 第二个参数为key值
		 */
		.getVariable("qingjia.120001", "aaa").toString();
		System.out.println(s);
	}
	
	/**
	 * 可以把一个对象放入到流程实例中
	 */
	@Test
	public void testSetObjToPi(){
		Person person = new Person();
		person.setPid(1L);
		person.setName("aaa");
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.setVariable("qingjia.120001", "person", person);
	}
	
	/**
	 * 把对象从流程实例中提取出来
	 */
	@Test
	public void testGetObj(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		Person person = (Person)processEngine.getExecutionService()
		.getVariable("qingjia.120001", "person");
		System.out.println(person.getName());
	}
}
