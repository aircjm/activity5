package cn.itcast.sh05.jbpm.pd;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.transaction.xa.XAException;

import org.jbpm.api.Configuration;
import org.jbpm.api.Deployment;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessEngine;
import org.junit.Test;

/**
 * ���̶������
 *    1�������̶�����в���
 *    2�������̶������ɾ��
 *    3��������ͼ���в鿴
 *    4�������̶�����в�ѯ
 * @author zd
 *
 */
public class PDManager {
	/**
	 * �����̶�����з���
	 */
	@Test
	public void testDeployFromClasspath(){
		/**
		 * ���jbpm����������
		 */
		ProcessEngine processEngine = Configuration.getProcessEngine();
		/**
		 * ��һ��api�ĵ���������һ��api�ķ���ֵ�����Կ�������ʽ���
		 */
		processEngine.getRepositoryService()
		.createDeployment()
		.addResourceFromClasspath("qingjia.jpdl.xml")
		.addResourceFromClasspath("qingjia.png")
		.deploy();
	}
	
	@Test
	public void testDeployFromInputStream(){
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("qingjia.jpdl.xml");
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		/**
		 * ��һ���������ļ���·��
		 * �ڶ���������inputStream
		 */
		.addResourceFromInputStream("qingjia.jpdl.xml", inputStream)
		.deploy();
	}
	
	@Test
	public void testDeployFromZipInputStream(){
		//�õ�zipinputStream
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("qingjia.zip");
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		//��ɲ���
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addResourcesFromZipInputStream(zipInputStream)
		.deploy();
	}
	
	/**
	 * ����deploymentId�鿴���̶���ͼƬ
	 */
	@Test
	public void testShowImage() throws Exception{
		ProcessEngine processEngine = Configuration.getProcessEngine();
		InputStream inputStream = processEngine.getRepositoryService()
		.getResourceAsStream("1","qingjia.png");
		
		OutputStream outputStream = new FileOutputStream("d:/processimg.png");
		for(int b=-1;(b=inputStream.read())!=-1;){
			outputStream.write(b);
		}
		inputStream.close();
		outputStream.close();
	}
	
	/**
	 * ��ѯ���е����̲���
	 */
	@Test
	public void testQueryAllDeployment(){
		ProcessEngine processEngine = Configuration.getProcessEngine();
		List<Deployment> dList = processEngine.getRepositoryService()
		.createDeploymentQuery()
		.list();
		for(Deployment deployment:dList){
			System.out.println(deployment.getId());
		}
	}
	
	/**
	 * ����deploymentId��ѯ���̲���
	 */
	@Test
	public void testQueryDeployment(){
		ProcessEngine processEngine =  Configuration.getProcessEngine();
		Deployment deployment = processEngine.getRepositoryService()
		.createDeploymentQuery()
		.deploymentId("20001")
		.uniqueResult();
		System.out.println(deployment.getId());
	}
	
	/**
	 * ��ѯ���е����̶���
	 */
	@Test
	public void testQueryPD(){
		ProcessEngine processEngine =  Configuration.getProcessEngine();
		List<ProcessDefinition> pdList = processEngine.getRepositoryService()
		.createProcessDefinitionQuery()
		.list();
		for(ProcessDefinition pd:pdList){
			System.out.print("deploymentId: "+pd.getDeploymentId()+",");
			System.out.print("image: "+pd.getImageResourceName()+",");
			System.out.print("pdkey: "+pd.getKey()+",");
			System.out.print("version: "+pd.getVersion()+",");
			System.out.println("pdid: "+pd.getId());
		}
	}
	
	/**
	 * ����һ��deploymentID,ɾ�������̲���
	 */
	@Test
	public void testDelete(){
		ProcessEngine processEngine =  Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.deleteDeploymentCascade("30001");
	}
	
	/**
	 * ����һ��pdkey,ɾ���������̶���
	 *    1������pdkey��ѯ�����е����̶���
	 *    2��������pdkey�µ����е����̶��壬��ÿһ�����̶����а�deploymentID��ȡ����
	 *    3������deploymentIDɾ�����̶���
	 */
	@Test
	public void testDeletePD(){
		ProcessEngine processEngine =  Configuration.getProcessEngine();
		/**
		 * ����pdkey��ȡ���е����̶���
		 */
		List<ProcessDefinition> pdList = processEngine.getRepositoryService()
		.createProcessDefinitionQuery()
		.processDefinitionKey("qingjia")
		.list();
		
		for(ProcessDefinition pd:pdList){
			processEngine.getRepositoryService()
			.deleteDeploymentCascade(pd.getDeploymentId());
		}
	}
}
