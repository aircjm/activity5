package cn.itcast.sh05.jbpm.variables;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.api.Configuration;
import org.jbpm.api.ProcessEngine;
import org.junit.Test;

/**
 * ���̱���
 * @author zd
 *
 */
public class VariableTest {
	/**
	 * ����������ʵ����ʱ���������̱���
	 *    
	 */
	@Test
	public void testStartPi(){
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("��������ʵ��", "����������ʵ����ʱ���������̱���");
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.startProcessInstanceById("qingjia-1", variables);
	}
	
	/**
	 * ����������ʱ���������̱���
	 */
	@Test
	public void testWhenTask(){
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("�������", "3��");
		variables.put("���ԭ��", "Լ��");
		ProcessEngine processEngine = Configuration.getProcessEngine();
		/**
		 * ��taskidΪ120003��ʱ���������̱���
		 */
		processEngine.getTaskService()
		.setVariables("120003", variables);
		processEngine.getTaskService()
		.completeTask("120003");
	}
	
	/**
	 * ����ʵ��ֻҪû�н������������������̱���
	 */
	@Test
	public void testSetVariablesWhenPi(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getExecutionService()
		.setVariable("qingjia.120001", "aaa", "aaa");
	}
	
	/**
	 * ��ȡ���̱���
	 */
	@Test
	public void testGetVariables(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		String s= processEngine.getExecutionService()
		/**
		 * ��һ������ΪexecutionId
		 * �ڶ�������Ϊkeyֵ
		 */
		.getVariable("qingjia.120001", "aaa").toString();
		System.out.println(s);
	}
	
	/**
	 * ���԰�һ��������뵽����ʵ����
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
	 * �Ѷ��������ʵ������ȡ����
	 */
	@Test
	public void testGetObj(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		Person person = (Person)processEngine.getExecutionService()
		.getVariable("qingjia.120001", "person");
		System.out.println(person.getName());
	}
}
