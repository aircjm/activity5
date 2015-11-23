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
 * 流程定义管理
 *    1、对流程定义进行部署
 *    2、对流程定义进行删除
 *    3、对流程图进行查看
 *    4、对流程定义进行查询
 * @author zd
 *
 */
public class PDManager {
	/**
	 * 对流程定义进行发布
	 */
	@Test
	public void testDeployFromClasspath(){
		/**
		 * 获得jbpm的流程引擎
		 */
		ProcessEngine processEngine = Configuration.getProcessEngine();
		/**
		 * 下一个api的调用者是上一个api的返回值，所以可以用链式编程
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
		 * 第一个参数是文件的路径
		 * 第二个参数是inputStream
		 */
		.addResourceFromInputStream("qingjia.jpdl.xml", inputStream)
		.deploy();
	}
	
	@Test
	public void testDeployFromZipInputStream(){
		//得到zipinputStream
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("qingjia.zip");
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		//完成部署
		ProcessEngine processEngine = Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.createDeployment()
		.addResourcesFromZipInputStream(zipInputStream)
		.deploy();
	}
	
	/**
	 * 根据deploymentId查看流程定义图片
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
	 * 查询所有的流程部署
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
	 * 根据deploymentId查询流程部署
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
	 * 查询所有的流程定义
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
	 * 给定一个deploymentID,删除该流程部署
	 */
	@Test
	public void testDelete(){
		ProcessEngine processEngine =  Configuration.getProcessEngine();
		processEngine.getRepositoryService()
		.deleteDeploymentCascade("30001");
	}
	
	/**
	 * 给定一个pdkey,删除整个流程定义
	 *    1、根据pdkey查询出所有的流程定义
	 *    2、遍历给pdkey下的所有的流程定义，从每一个流程定义中把deploymentID提取出来
	 *    3、根据deploymentID删除流程定义
	 */
	@Test
	public void testDeletePD(){
		ProcessEngine processEngine =  Configuration.getProcessEngine();
		/**
		 * 根据pdkey获取所有的流程定义
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
