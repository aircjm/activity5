package com.itheima09.activiti5.pd;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

/**
 * pdManager=process definition manager  ���̶������
 * @author zd
 *   ���̶��壺
 *      1��*.bpmn��*.png�������̶����ĵ�
 *         png��Ϊ����ʾͼƬ��
 *         bpmn��Ϊ�˽ṹ�����������̶���
 *      2����key,version,pdid��ȷ�����̶����
 *   ������
 *      1������
 *         classpath
 *         inputstream
 *         zipinputstream
 *      2����ѯ
 *      	��ѯ���̲���
 *             ��ѯ���е����̲���
 *             ����deploymentID��ѯ���̲���
 *          ��ѯ���̶���
 *             ��ѯ���е����̶���
 *             ����pdid��ѯ���̶���
 *             ����pdkey��ѯ���̶���
 *             ���� deploymentID��ѯ���̶���
 *      3��ɾ��
 *      4���鿴����ͼ
 *         ���Ը���pdid��ѯ����ͼ
 *         ���Ը���deploymentID,resourceName��ѯ����ͼ
 */
public class PDManager {
	/**
	 * 1���漰���ı�
	 *    act_re_deployment  �����
	 *       1��˵��
	 *           ��������һ�ζ���,ÿ����һ�Σ��ڸñ�������һ�м�¼
	 *       2���ֶ�
	 *           ID_: ����ID
	 *    act_re_procdef     ���̶����
	 *       1��˵��
	 *           �������������̶����
	 *           ���keyֵ����nameֵ�����仯���൱��һ��ȫ�µ����̶��壬���ʱ���ٴβ��𣬰汾�Ŵ�1��ʼ����
	 *       2���ֶ�
	 *           name_:���̶�������
	 *           key_:���̶���key
	 *           version_:�汾
	 *           deployment_id_:����ID
	 *           pdid:���̶���ID  {processKey}:{processVersion}:�����
	 *    act_ge_bytearry    ͨ�����ݱ�  �ñ�����hellworld.bpmn��helloworld.png����
	 *       1��˵��
	 *             �ڲ����ʱ�򣬲����bpmn��png������ڸñ���
	 *       2���ֶ�
	 *             deploymentID:����ID  �������Ը��ݲ���ID��ѯͼƬ
	 */
	/**
	 * ����classpath���в���
	 */
	@Test
	public void testDeploy_Classpath(){
		//�õ�һ��Ĭ�ϵ���������
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService()
		.createDeployment()
		.addClasspathResource("com/itheima09/activiti5/helloworld/helloworld.bpmn")
		.addClasspathResource("com/itheima09/activiti5/helloworld/helloworld.png")
		.deploy();
		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
	}
	
	/**
	 * ����inputStream���в���
	 */
	@Test
	public void testDeploy_Inputstream(){
		InputStream inputStream = this.getClass().
					getClassLoader().
					getResourceAsStream("com/itheima09/activiti5/helloworld/helloworld.bpmn");
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addInputStream("com/itheima09/activiti5/helloworld/helloworld.bpmn", inputStream)
		.deploy();
	}
	
	/**
	 * ����zipinputstream���в���
	 */
	@Test
	public void testDeploy_ZipinputStream(){
		InputStream inputStream = this.getClass().
				getClassLoader().
				getResourceAsStream("com/itheima09/activiti5/helloworld/helloworld.zip");
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addZipInputStream(zipInputStream)
		.deploy();
	}
	
	
	/**
	 * ��ѯ���е����̲���
	 */
	@Test
	public void testQueryDeployment(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<Deployment> dList = processEngine.getRepositoryService()
		.createDeploymentQuery()//����һ�������ѯ
		.list();
		for(Deployment deployment:dList){
			System.out.println(deployment.getId());
		}
	}
	
	/**
	 * ���ݲ���ID��ѯ����
	 */
	@Test
	public void testQueryDeploymentByID(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService()
		.createDeploymentQuery()
		.deploymentId("1")   //��ѯdeploymentidΪ1�Ĳ���
		.singleResult();
		System.out.println(deployment.getId());
	}
	
	/**
	 * ��ѯ���е����̶���
	 */
	@Test
	public void testQueryPD(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<ProcessDefinition> pdList = processEngine.getRepositoryService()
		.createProcessDefinitionQuery()
		.list();
		for(ProcessDefinition pd:pdList){
			System.out.print("key:"+pd.getKey()+",");
			System.out.print("version:"+pd.getVersion()+",");
			System.out.println("pdid:"+pd.getId());
		}
	}
	
	/**
	 * ����pdid��ѯ���̶���
	 */
	@Test
	public void testQueryPDByPDID(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		ProcessDefinition pd = processEngine.getRepositoryService()
		.createProcessDefinitionQuery()
		.processDefinitionId("itheima09:1:204")
		.singleResult();
		System.out.print("key:"+pd.getKey()+",");
		System.out.print("version:"+pd.getVersion()+",");
		System.out.println("pdid:"+pd.getId());
	}
	
	/**
	 * ����pdkey��ѯ���̶���
	 */
	@Test
	public void testQueryPDByPDKEY(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<ProcessDefinition> pdList = processEngine.getRepositoryService()
		.createProcessDefinitionQuery()
		.processDefinitionKey("myProcess")
		.list();
		for(ProcessDefinition pd:pdList){
			System.out.print("key:"+pd.getKey()+",");
			System.out.print("version:"+pd.getVersion()+",");
			System.out.println("pdid:"+pd.getId());
		}
	}
	
	/**
	 * ����deploymentID��ѯ���̶���   deploymentID��pdid�����Ӧ��
	 */
	
	/**
	 * ɾ��
	 *    ֻ�ܸ���deploymentID����ɾ��
	 */
	@Test
	public void testDelete(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//		processEngine.getRepositoryService()
//		.deleteDeployment("1");//�ø�APIֻ��ɾ�����̶�������ݺͲ��������
		processEngine.getRepositoryService()
		.deleteDeployment("1", true);//ɾ���˹���deploymentIDΪ1�����е����ݣ����������̶��塢���̲����������Ϣ
	}
	
	/**
	 * ��ѯ����ͼ
	 */
	@Test
	public void showImage() throws Exception{
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		InputStream inputStream = processEngine.getRepositoryService()
		.getProcessDiagram("itheima09:1:204");
		OutputStream outputStream = new FileOutputStream("e:/processimg1.png");
		for(int b=-1;(b=inputStream.read())!=-1;){
			outputStream.write(b);
		}
		inputStream.close();
		outputStream.close();
		InputStream inputStream2 = processEngine.getRepositoryService()
		.getResourceAsStream("101", "com/itheima09/activiti5/helloworld/helloworld.png");
		OutputStream outputStream2 = new FileOutputStream("e:/processimg2.png");
		for(int b=-1;(b=inputStream2.read())!=-1;){
			outputStream2.write(b);
		}
		inputStream2.close();
		outputStream2.close();
	}
}
