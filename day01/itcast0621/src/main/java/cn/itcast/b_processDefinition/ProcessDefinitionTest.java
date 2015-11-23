package cn.itcast.b_processDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ProcessDefinitionTest {
	
	//创建工作流的核心对象（流程引擎）
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**部署流程定义（classpath）*/
	@Test
	public void deployementProcessDefinition(){
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
					.createDeployment()//创建部署对象
					.name("请假流程")//指定部署名称
					.addClasspathResource("diagrams/helloworld.bpmn")//从类路径加载资源文件，一次只能加载一个文件
					.addClasspathResource("diagrams/helloworld.png")//从类路径加载资源文件，一次只能加载一个文件
					.deploy();//完成部署
		System.out.println("部署ID："+deployment.getId());
		System.out.println("部署名称："+deployment.getName());
	}
	
	/**部署流程定义（zip）*/
	@Test
	public void deployementProcessDefinition_zip(){
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
					.createDeployment()//创建部署对象
					.name("请假流程")//指定部署名称
					.addZipInputStream(zipInputStream)//使用zip格式的输入流完成部署
					.deploy();//完成部署
		System.out.println("部署ID："+deployment.getId());
		System.out.println("部署名称："+deployment.getName());
	}
	
	/**流程定义的查询*/
	@Test
	public void findProcessDefinition(){
		List<ProcessDefinition> list = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
					.createProcessDefinitionQuery()//创建流程定义的查询对象
					/**where条件*/
//					.processDefinitionId(processDefinitionId)//按照流程定义的ID查询
//					.processDefinitionKey(processDefinitionKey)//按照流程定义的key查询
//					.processDefinitionNameLike(processDefinitionNameLike)//按照流程定义的名称模糊查询
					
					/**排序*/
					.orderByProcessDefinitionVersion().asc()//按照版本的升序排列
//					.orderByProcessDefinitionName().desc()//按照名称的降序排列
					
					/**返回结果*/
//					.singleResult();//返回惟一结果集
					.list();//返回多个结果集
//					.listPage(firstResult, maxResults);//分页查询：firstResult：表示当前页从第几条开始检索，maxResults：当前页最多显示多少条记录
//					.count();//返回结果集的数量
		if(list!=null && list.size()>0){
			for(ProcessDefinition pd:list){
				System.out.println("流程定义ID:"+pd.getId());//流程定义的key:版本:随时生成数字
				System.out.println("流程定义的名称:"+pd.getName());//对应helloworld.bpmn文件中的name的属性值
				System.out.println("流程定义的key:"+pd.getKey());//对应helloworld.bpmn文件中的id的属性值
				System.out.println("流程定义的版本号:"+pd.getVersion());//默认流程定义的key第一个版本是1，此后当流程定义的key相同的情况下，再次部署版本+1
				System.out.println("资源名称bpmn:"+pd.getResourceName());
				System.out.println("资源图片名称png:"+pd.getDiagramResourceName());
				System.out.println("部署对象ID:"+pd.getDeploymentId());
				System.out.println("##############################################################");
			}
		}
	}
	
	/**删除流程定义*/
	@Test
	public void deleteProcessDefinition(){
		//部署ID对象
		String deploymentId = "701";
		/**
		 * .deleteDeployment(deploymentId);不带级联的删除
		 *   * 如果流程没有执行，此时可以使用不带级联的删除进行操作
		 *   * 但是如果流程启动了，此时再删除的时候，就会抛出异常
		 */
//		processEngine.getRepositoryService()//
//					.deleteDeployment(deploymentId);
		/**
		 * .deleteDeployment(deploymentId,true);带级联的删除
		 *   * 不管流程是否启动，都会级联删除对应数据库表的数据
		 */
		processEngine.getRepositoryService()//
					.deleteDeployment(deploymentId,true);
		
	}
	
	/**查看流程图*/
	@Test
	public void viewPic(){
		//部署ID对象
		String deploymentId = "501";
		//使用部署对象ID，获取资源名称
		List<String> list = processEngine.getRepositoryService()//
						.getDeploymentResourceNames(deploymentId);
		//资源图片的名称
		String resouceName = "";
		if(list!=null && list.size()>0){
			for(String name:list){
				if(name.indexOf(".png")>=0){
					resouceName = name;
				}
			}
		}
		//获取资源图片的输入流
		InputStream in = processEngine.getRepositoryService()//
					.getResourceAsStream(deploymentId, resouceName);
		File file = new File("D:/"+resouceName);
		try {
			FileUtils.copyInputStreamToFile(in, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**7.7：附加功能：查询最新版本的流程定义*/
	@Test
	public void findLastVersionProcessDefinition(){
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
						.createProcessDefinitionQuery()//
						.orderByProcessDefinitionVersion().asc()//按照版本的升序排列
						.list();
		/***
		 * 定义Map集合
		 * 当key值相同的情况下，后一次版本的值将替换前一次版本的值
		 *    * map集合的key：流程定义的key
		 *    * map集合的值：ProcessDefinition
		 */
		Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();//存放最新版本的流程定义
		if(list!=null && list.size()>0){
			for(ProcessDefinition pd:list){
				map.put(pd.getKey(), pd);
			}
		}
		List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());//存放最新版本的流程定义
		if(pdList!=null && pdList.size()>0){
			for(ProcessDefinition pd:pdList){
				System.out.println("流程定义ID:"+pd.getId());//流程定义的key:版本:随时生成数字
				System.out.println("流程定义的名称:"+pd.getName());//对应helloworld.bpmn文件中的name的属性值
				System.out.println("流程定义的key:"+pd.getKey());//对应helloworld.bpmn文件中的id的属性值
				System.out.println("流程定义的版本号:"+pd.getVersion());//默认流程定义的key第一个版本是1，此后当流程定义的key相同的情况下，再次部署版本+1
				System.out.println("资源名称bpmn:"+pd.getResourceName());
				System.out.println("资源图片名称png:"+pd.getDiagramResourceName());
				System.out.println("部署对象ID:"+pd.getDeploymentId());
				System.out.println("##############################################################");
			}
		}
		
	}
	
	/**附加功能：删除流程定义（删除key相同的所有不同版本的流程定义）*/
	@Test
	public void deleteProcessDefinitionByKey(){
		//使用流程定义key先查询
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
							.createProcessDefinitionQuery()//
							.processDefinitionKey("helloworld")//使用流程定义的key查询
							.list();
		if(list!=null && list.size()>0){
			for(ProcessDefinition pd:list){
				//部署ID对象
				String deploymentId = pd.getDeploymentId();
				
				processEngine.getRepositoryService()//
							.deleteDeployment(deploymentId,true);
				
			}
		}
		
		
		
	}
}
