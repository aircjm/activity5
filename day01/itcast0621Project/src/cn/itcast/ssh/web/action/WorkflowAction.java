package cn.itcast.ssh.web.action;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.struts2.ServletActionContext;

import cn.itcast.ssh.domain.Employee;
import cn.itcast.ssh.domain.LeaveBill;
import cn.itcast.ssh.service.ILeaveBillService;
import cn.itcast.ssh.service.IWorkflowService;
import cn.itcast.ssh.utils.SessionContext;
import cn.itcast.ssh.utils.ValueContext;
import cn.itcast.ssh.web.form.WorkflowBean;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public class WorkflowAction extends ActionSupport implements ModelDriven<WorkflowBean> {

	private WorkflowBean workflowBean = new WorkflowBean();
	
	@Override
	public WorkflowBean getModel() {
		return workflowBean;
	}
	
	private IWorkflowService workflowService;
	
	private ILeaveBillService leaveBillService;

	public void setLeaveBillService(ILeaveBillService leaveBillService) {
		this.leaveBillService = leaveBillService;
	}

	public void setWorkflowService(IWorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	/**
	 * 部署管理首页显示
	 * @return
	 */
	public String deployHome(){
		//1：查询所有的部署对象信息，返回List<>
		List<Deployment> deploymentList = workflowService.findDeploymentList();
		//2：查询所有的流程定义对象信息，返回List<>
		List<ProcessDefinition> pdList = workflowService.findProcessDefinitionList();
		//放置ActionContext
		ValueContext.putValueContext("deploymentList", deploymentList);
		ValueContext.putValueContext("pdList", pdList);
		return "deployHome";
	}
	
	/**
	 * 发布流程
	 * @return
	 */
	public String newdeploy(){
		//2：获取zip格式的文件和流程名称
		//流程文件的名称
		String filename = workflowBean.getFilename();
		//zip格式的File类型文件
		File file = workflowBean.getFile();
		//完成部署
		workflowService.saveDeployByZip(filename,file);
		return "list";
	}
	
	/**
	 * 删除部署信息
	 */
	public String delDeployment(){
		//获取部署ID
		String deploymentId = workflowBean.getDeploymentId();
		//2：使用部署对象ID，删除流程定义
		workflowService.deleteDeploymentByID(deploymentId);
		return "list";
	}
	
	/**
	 * 查看流程图
	 * @throws Exception 
	 */
	public String viewImage() throws Exception{
		//获取部署对象ID
		String deploymentId = workflowBean.getDeploymentId();
		//获取图片资源名称
		String imageName = workflowBean.getImageName();
		//2：使用部署对象ID和资源图片名称，获取InputStream流（存放图片）
		InputStream in = workflowService.findInputStreamByImage(deploymentId,imageName);
		//3：得到response对象，获取response对象中的输出流
		OutputStream out = ServletActionContext.getResponse().getOutputStream();
		//4:将Inputstream流对象中的数据读取出来，写到输出流中
		for(int b=-1;(b=in.read())!=-1;){
			out.write(b);
		}
		//查看流程图，不需要指定页面的时候，返回null即可
		return null;
	}
	
	// 启动流程
	public String startProcess(){
		//获取请假单ID
		Long id = workflowBean.getId();
		workflowService.saveStartProcess(id);
		return "listTask";
	}
	
	
	
	/**
	 * 任务管理首页显示
	 * @return
	 */
	public String listTask(){
		//1：从Session中获取当前登录人
		Employee employee = SessionContext.get();
		String name = employee.getName();
		//2：使用当前登录人作为任务的办理人，查询正在执行的任务，返回List<Task>
		List<Task> list = workflowService.findTaskListByAssignee(name);
		ValueContext.putValueContext("list", list);
		return "task";
	}
	
	/**
	 * 打开任务表单
	 */
	public String viewTaskForm(){
		//获取任务ID
		String taskId = workflowBean.getTaskId();
		//从任务的活动节点中，获取formkey值，此时是一个url连接
		String url = workflowService.findTaskFormKey(taskId);
		
		//使用任务ID，获取流程对应业务的主键id（请假单id）
		String id = workflowService.findLeaveBillIdByTaskId(taskId);
		url += "?id="+id+"&taskId="+taskId;
		ValueContext.putValueContext("url", url);
		return "viewTaskForm";
	}
	
	// 准备表单数据
	public String audit(){
		//获取任务ID
		String taskId = workflowBean.getTaskId();
		//获取请假单ID
		Long id = workflowBean.getId();
		//2：使用请假单ID，查询请假的信息，页面进行表单回显
		LeaveBill leaveBill = leaveBillService.findLeaveBillByID(id);
		ValueContext.putValueStack(leaveBill);
		//3：使用任务ID，获取当前任务完成后连线的集合名称，遍历在页面的按钮上
		List<String> outcomeList = workflowService.findOutcomeListByTaskId(taskId);
		ValueContext.putValueContext("outcomeList", outcomeList);
		//4：使用任务ID，获取当前请假单流程对应审核人的审批记录（List），查询act_hi_comment表
		List<Comment> commentList = workflowService.fincCommentListByTaskId(taskId);
		ValueContext.putValueContext("commentList", commentList);
		return "taskForm";
	}
	
	/**
	 * 提交任务
	 */
	public String submitTask(){
		workflowService.saveSubmitTask(workflowBean);
		return "listTask";
	}
	
	/**
	 * 查看当前流程图（查看当前活动节点，并使用红色的框标注）
	 */
	public String viewCurrentImage(){
		/**1:显示流程图*/
		//获取任务ID
		String taskId = workflowBean.getTaskId();
		//显示流程图，获取流程定义的对象
		ProcessDefinition pd = workflowService.findProcessDefintionByTaskId(taskId);
		//（3）获取部署ID和资源图片名称
		String deploymentId = pd.getDeploymentId();
		String imageSource = pd.getDiagramResourceName();
		//放置到ActionContext中
		ValueContext.putValueContext("deploymentId", deploymentId);
		ValueContext.putValueContext("imageName", imageSource);
		/**2:查询.bpmn文件中的当前活动所对应的坐标*/
		//map集合的key表示坐标的名称，map集合值表示坐标值
		Map<String, Object> map = workflowService.findCoodingByTaskId(taskId);
		ValueContext.putValueContext("acs", map);
		return "image";
	}
	
	// 查看历史的批注信息
	public String viewHisComment(){
		//获取请假单ID
		Long id = workflowBean.getId();
		//1：使用请假单ID，查询请假单信息，回显到页面上
		LeaveBill bill = leaveBillService.findLeaveBillByID(id);
		ValueContext.putValueStack(bill);
		//2：显示当前流程的对应所有人的批注信息
		List<Comment> commentList = workflowService.findCommonListByLeaveBillId(id);
		ValueContext.putValueContext("commentList", commentList);
		return "viewHisComment";
	}
}
