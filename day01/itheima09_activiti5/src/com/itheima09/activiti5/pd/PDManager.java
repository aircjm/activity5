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
 * pdManager=process definition manager  流程定义管理
 * @author zd
 *   流程定义：
 *      1、*.bpmn和*.png叫做流程定义文档
 *         png是为了显示图片的
 *         bpmn是为了结构化的描述流程定义
 *      2、有key,version,pdid来确定流程定义的
 *   操作：
 *      1、部署
 *         classpath
 *         inputstream
 *         zipinputstream
 *      2、查询
 *      	查询流程部署
 *             查询所有的流程部署
 *             根据deploymentID查询流程部署
 *          查询流程定义
 *             查询所有的流程定义
 *             根据pdid查询流程定义
 *             根据pdkey查询流程定义
 *             根据 deploymentID查询流程定义
 *      3、删除
 *      4、查看流程图
 *         可以根据pdid查询流程图
 *         可以根据deploymentID,resourceName查询流程图
 */
public class PDManager {
	/**
	 * 1、涉及到的表
	 *    act_re_deployment  部署表
	 *       1、说明
	 *           描述的是一次动作,每部署一次，在该表中增加一行记录
	 *       2、字段
	 *           ID_: 部署ID
	 *    act_re_procdef     流程定义表
	 *       1、说明
	 *           是用来描述流程定义的
	 *           如果key值或者name值发生变化，相当于一个全新的流程定义，这个时候再次部署，版本号从1开始计算
	 *       2、字段
	 *           name_:流程定义名称
	 *           key_:流程定义key
	 *           version_:版本
	 *           deployment_id_:部署ID
	 *           pdid:流程定义ID  {processKey}:{processVersion}:随机数
	 *    act_ge_bytearry    通用数据表  该表存放了hellworld.bpmn和helloworld.png数据
	 *       1、说明
	 *             在部署的时候，部署的bpmn和png都存放在该表中
	 *       2、字段
	 *             deploymentID:部署ID  将来可以根据部署ID查询图片
	 */
	/**
	 * 根据classpath进行部署
	 */
	@Test
	public void testDeploy_Classpath(){
		//得到一个默认的流程引擎
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
	 * 根据inputStream进行部署
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
	 * 根据zipinputstream进行部署
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
	 * 查询所有的流程部署
	 */
	@Test
	public void testQueryDeployment(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<Deployment> dList = processEngine.getRepositoryService()
		.createDeploymentQuery()//创建一个部署查询
		.list();
		for(Deployment deployment:dList){
			System.out.println(deployment.getId());
		}
	}
	
	/**
	 * 根据部署ID查询部署
	 */
	@Test
	public void testQueryDeploymentByID(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService()
		.createDeploymentQuery()
		.deploymentId("1")   //查询deploymentid为1的部署
		.singleResult();
		System.out.println(deployment.getId());
	}
	
	/**
	 * 查询所有的流程定义
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
	 * 根据pdid查询流程定义
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
	 * 根据pdkey查询流程定义
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
	 * 根据deploymentID查询流程定义   deploymentID和pdid是相对应的
	 */
	
	/**
	 * 删除
	 *    只能根据deploymentID进行删除
	 */
	@Test
	public void testDelete(){
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//		processEngine.getRepositoryService()
//		.deleteDeployment("1");//用该API只能删除流程定义的内容和部署的内容
		processEngine.getRepositoryService()
		.deleteDeployment("1", true);//删除了关于deploymentID为1的所有的数据，包括：流程定义、流程部署、任务等信息
	}
	
	/**
	 * 查询流程图
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
