package cn.itcast.ssh.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;

import cn.itcast.ssh.dao.ILeaveBillDao;
import cn.itcast.ssh.domain.Employee;
import cn.itcast.ssh.domain.LeaveBill;
import cn.itcast.ssh.service.IWorkflowService;
import cn.itcast.ssh.utils.SessionContext;
import cn.itcast.ssh.web.form.WorkflowBean;

public class WorkflowServiceImpl implements IWorkflowService {
	/**请假申请Dao*/
	private ILeaveBillDao leaveBillDao;
	
	private RepositoryService repositoryService;
	
	private RuntimeService runtimeService;
	
	private TaskService taskService;
	
	private FormService formService;
	
	private HistoryService historyService;
	
	public void setLeaveBillDao(ILeaveBillDao leaveBillDao) {
		this.leaveBillDao = leaveBillDao;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}
	
	public void setFormService(FormService formService) {
		this.formService = formService;
	}
	
	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**部署流程定义*/
	@Override
	public void saveDeployByZip(String filename, File file) {
		
		try {
			//定义Zip输入流
			ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
			repositoryService.createDeployment()//
						.name(filename)//部署名称
						.addZipInputStream(zipInputStream)//使用zip格式的文件，完成部署流程定义
						.deploy();//完成部署
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**查询部署对象的List*/
	@Override
	public List<Deployment> findDeploymentList() {
		List<Deployment> list = repositoryService.createDeploymentQuery()//
						.orderByDeploymenTime().desc()//按照部署时间的降序排列
						.list();
		return list;
	}
	
	/**查询流程定义的集合List*/
	@Override
	public List<ProcessDefinition> findProcessDefinitionList() {
		List<ProcessDefinition> pdList = repositoryService.createProcessDefinitionQuery()//
						.orderByProcessDefinitionVersion().desc()//按照版本的降序排列
						.list();
		return pdList;
	}
	
	/**使用部署ID，完成流程定义的删除*/
	@Override
	public void deleteDeploymentByID(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);//级联删除
	}
	
	/**使用部署对象ID和资源图片名称，获取输入流对象*/
	@Override
	public InputStream findInputStreamByImage(String deploymentId,
			String imageName) {
		InputStream in = repositoryService.getResourceAsStream(deploymentId, imageName);
		return in;
	}
	
	/**启动流程实例，更新请假单表的状态*/
	@Override
	public void saveStartProcess(Long id) {
		//1：获取请假单ID，查询请假单的信息，更新state从0变成1
		LeaveBill leaveBill = leaveBillDao.findLeaveBillByID(id);
		if(leaveBill!=null){
			leaveBill.setState(1);
			//2：使用流程定义的key，启动流程实例，流程开始执行
			//流程定义的key对应javabean的名称
			String processDefinitionKey = leaveBill.getClass().getSimpleName();
			//3：流程到达第一个任务之前，设置第一个任务的办理人，使用流程变量
			//流程变量的名称userID，流程变量的值当前登录人的登录名（从Session中获取）
			Map<String, Object> variables = new HashMap<String, Object>();
			//从Session中获取当前用户的登录名
			Employee employee = SessionContext.get();
			variables.put("userID", employee.getName());
			//4：让业务关联流程
			//(1)使用流程变量，流程变量中的值LeaveBill.id的形式
			String objId = processDefinitionKey+"."+id;
			variables.put("objId", objId);
			//(2)保存数据的时候，在正在执行的执行对象表和流程实例历史表中对应的字段BUSINESS_KEY:添加业务信息，值的形式:LeaveBill.id
			runtimeService.startProcessInstanceByKey(processDefinitionKey,objId,variables);//使用流程定义的key启动流程实例，默认按照最新版本启动
		}
	}
	
	/**使用当前办理人，查询正在执行的任务，返回任务的列表*/
	@Override
	public List<Task> findTaskListByAssignee(String name) {
		List<Task> list = taskService.createTaskQuery()//
						.taskAssignee(name)//指定任务的办理人
						.orderByTaskCreateTime().asc()//
						.list();
		return list;
	}
	
	/**从任务的节点中获取FormKey的值（此时对应workflowAction_audit.action）*/
	@Override
	public String findTaskFormKey(String taskId) {
		TaskFormData formData = formService.getTaskFormData(taskId);
		String url = formData.getFormKey();
		return url;
	}
	
	/**使用任务ID，获取流程对应业务的主键id（请假单id）*/
	@Override
	public String findLeaveBillIdByTaskId(String taskId) {
		//1:使用任务ID，查询任务对象（从而获取流程实例ID）
		Task task = taskService.createTaskQuery()//
						.taskId(taskId)//使用任务ID查询
						.singleResult();
		//流程实例ID
		String piId = task.getProcessInstanceId();
		//2:使用流程实例ID，查询正在执行的执行对象表，获取流程实例的对象（从而获取BUSINIESS_KEY对应的值）
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
						.processInstanceId(piId)//按照流程实例ID查询
						.singleResult();
		//获取业务key的值(LeaveBill.2)
		String businessKey = pi.getBusinessKey();
		//请假单ID
		String id = "";
		if(StringUtils.isNotBlank(businessKey)){
			//截取字符串，小数点后的那个值
			id = businessKey.split("\\.")[1];
		}
		return id;
	}
	
	/**使用任务ID，获取当前任务完成后连线的集合名称，遍历在页面的按钮上*/
	@Override
	public List<String> findOutcomeListByTaskId(String taskId) {
		//存放连线的集合名称
		List<String> outcomeList = new ArrayList<String>();
		//1:使用任务ID，查询任务对象（从而获取流程实例ID）
		Task task = taskService.createTaskQuery()//
						.taskId(taskId)//使用任务ID查询
						.singleResult();
		//获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		//2:查询流程定义的实体对象（ProcessDefinitionEntity），从而获取LeaveBill.bpmn文件中的配置
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
		//3:使用流程实例ID，查询正在执行执行对象表，从而获取活动ID（activitiId）
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
					.processInstanceId(processInstanceId)//按照流程实例ID查询
					.singleResult();
		//获取当前活动的ID
		String activityId = pi.getActivityId();
		//获取当前活动的对象，需要指定活动ID
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
		//使用当前活动的对象，获取当前活动的连线的集合
		List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
		//遍历
		if(pvmList!=null && pvmList.size()>0){
			for(PvmTransition transition:pvmList){
				//获取连线中哪个属性的名称，获取该属性的值
				String name = (String) transition.getProperty("name");
				//将连线的名称name，放置到outcomeList中
				outcomeList.add(name);
			}
		}
		return outcomeList;
	}
	
	/**点击审批，完成任务*/
	@Override
	public void saveSubmitTask(WorkflowBean workflowBean) {
		//获取任务ID
		String taskId = workflowBean.getTaskId();
		//请假单ID
		Long id = workflowBean.getId();
		//连线名称
		String outcome = workflowBean.getOutcome();
		//获取页面中的批注
		String comment = workflowBean.getComment();
		//使用请假单ID，查询请假单的信息
		LeaveBill bill = leaveBillDao.findLeaveBillByID(id);
		/**
		 * 1：从页面中获取连线的名称（点击按钮的名称）
		   * 使用使用连线的名称，设置流程变量，此时才可以实现完成任务的同时，指定哪条连线离开
		   * 流程变量的名称outcome，流程变量的值：页面获取的连线名称
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("outcome", outcome);
		
		//使用任务ID，查询任务对象（从而获取流程实例ID）
		Task task = taskService.createTaskQuery()//
						.taskId(taskId)//使用任务ID查询
						.singleResult();
		//获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		
		//2:完成任务之前，获取页面的批注信息，添加一个批注
		/**
		 * 提供一个表（act_hi_comment），专门存储对应流程的审批记录
    	 	操作，向act_hi_comment表中添加数据
		 */
		//参数从Session中获取当前登录人，与审核人绑定（保证了审核的线程安全）
		String authenticatedUserId = SessionContext.get().getName();
		Authentication.setAuthenticatedUserId(authenticatedUserId);//当前登录人与审批人进行绑定
		taskService.addComment(taskId, processInstanceId, comment);//其中参数3表示批注
		//3：指定任务ID，并设置流程变量，完成任务
		taskService.complete(taskId, variables);
		//4：完成任务后，到达下一个任务，在任务节点中使用Listener，实现指定下一个任务的办理人（已经实现在ManagerTaskHandler类中）
		//5：如果完成任务后，流程已经结束，此时更新请假单表中的state状态，从1变成2（2表示审核完成）
		//判断流程是否结束
		
		//获取流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
						.processInstanceId(processInstanceId)//使用流程实例ID查询
						.singleResult();
		//此时流程实例结束了
		if(pi==null){
			//此时更新请假单表中的state状态，从1变成2（2表示审核完成）
			bill.setState(2);
		}
	}
	
	/**查询批注信息*/
	@Override
	public List<Comment> fincCommentListByTaskId(String taskId) {
		//返回的集合批注信息
		List<Comment> list = new ArrayList<Comment>();
		//使用任务ID，查询任务对象（从而获取流程实例ID）
		Task task = taskService.createTaskQuery()//
						.taskId(taskId)//使用任务ID查询
						.singleResult();
		//获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		//1：使用任务ID，查找历史的活动表，获取每个执行完成的任务ID
		List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()//
						.processInstanceId(processInstanceId)//使用流程实例ID查询
						.activityType("userTask")//按照历史活动节点类型是userTask查询
						.list();
		//遍历，获取当前流程实例中的每个任务ID
		if(haiList!=null && haiList.size()>0){
			for(HistoricActivityInstance hai:haiList){
				String hisTaskId = hai.getTaskId();
				//获取每个历史任务ID，对应的批注信息
				List<Comment> commentList = taskService.getTaskComments(hisTaskId);
				if(commentList!=null && commentList.size()>0){
					for(Comment c:commentList){
						list.add(c);
					}
				}
			}
		}
		return list;
	}
	
	/**使用任务ID，查询流程定义的对象*/
	@Override
	public ProcessDefinition findProcessDefintionByTaskId(String taskId) {
		//（1）使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery()//
					.taskId(taskId)//使用任务ID查询
					.singleResult();
		//（2）获取流程定义ID，查询流程定义对象
		String pdId = task.getProcessDefinitionId();
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()//
							.processDefinitionId(pdId)//使用流程定义ID查询
							.singleResult();
		return pd;
	}
	
	/**使用任务ID，查询当前任务的ID所对应的坐标*/
	@Override
	public Map<String, Object> findCoodingByTaskId(String taskId) {
		//存放到Map集合中
		Map<String, Object> map = new HashMap<String, Object>();
		//（1）使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery()//
					.taskId(taskId)//使用任务ID查询
					.singleResult();
		//获取流程定义的ID
		String pdId = task.getProcessDefinitionId();
		//获取流程定义的实体对象
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(pdId);
		
		
		//获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
								.processInstanceId(processInstanceId)//按照流程实例ID查询
								.singleResult();
		//获取当前活动的ID
		String activityId = pi.getActivityId();
		//使用流程定义的实体对象，获取当前活动的对象
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);//指定当前活动的ID
		//获取对应当前活动的坐标
		map.put("x", activityImpl.getX());
		map.put("y", activityImpl.getY());
		map.put("width", activityImpl.getWidth());
		map.put("height", activityImpl.getHeight());
		return map;
	}
	
	/**使用申请ID，查询当前请假单对应审核人的批注信息*/
	@Override
	public List<Comment> findCommonListByLeaveBillId(Long id) {
		//返回的集合批注信息
		List<Comment> list = new ArrayList<Comment>();
		
		//查询历史的流程变量表，使用流程变量为objId，对应的值LeaveBill.2
		//流程变量的名称
		String variableName = "objId";
		//流程变量的值
		LeaveBill leaveBill = leaveBillDao.findLeaveBillByID(id);
		String variableValue = leaveBill.getClass().getSimpleName()+"."+id;
		HistoricVariableInstance hvi = historyService.createHistoricVariableInstanceQuery()//历史流程变量查询
						.variableValueEquals(variableName, variableValue)//指定流程变量的名称和流程变量的值，查询惟一的历史记录
						.singleResult();
						
		//获取流程实例ID
		String processInstanceId = hvi.getProcessInstanceId();
		//1：使用任务ID，查找历史的活动表，获取每个执行完成的任务ID
		List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()//
						.processInstanceId(processInstanceId)//使用流程实例ID查询
						.activityType("userTask")//按照历史活动节点类型是userTask查询
						.list();
		//遍历，获取当前流程实例中的每个任务ID
		if(haiList!=null && haiList.size()>0){
			for(HistoricActivityInstance hai:haiList){
				String hisTaskId = hai.getTaskId();
				//获取每个历史任务ID，对应的批注信息
				List<Comment> commentList = taskService.getTaskComments(hisTaskId);
				if(commentList!=null && commentList.size()>0){
					for(Comment c:commentList){
						list.add(c);
					}
				}
			}
		}
		return list;
	}
}
