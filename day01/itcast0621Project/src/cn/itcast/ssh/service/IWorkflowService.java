package cn.itcast.ssh.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import cn.itcast.ssh.web.form.WorkflowBean;



public interface IWorkflowService {

	void saveDeployByZip(String filename, File file);

	List<Deployment> findDeploymentList();

	List<ProcessDefinition> findProcessDefinitionList();

	void deleteDeploymentByID(String deploymentId);

	InputStream findInputStreamByImage(String deploymentId, String imageName);

	void saveStartProcess(Long id);

	List<Task> findTaskListByAssignee(String name);

	String findTaskFormKey(String taskId);

	String findLeaveBillIdByTaskId(String taskId);

	List<String> findOutcomeListByTaskId(String taskId);

	void saveSubmitTask(WorkflowBean workflowBean);

	List<Comment> fincCommentListByTaskId(String taskId);

	ProcessDefinition findProcessDefintionByTaskId(String taskId);

	Map<String, Object> findCoodingByTaskId(String taskId);

	List<Comment> findCommonListByLeaveBillId(Long id);

	

}
