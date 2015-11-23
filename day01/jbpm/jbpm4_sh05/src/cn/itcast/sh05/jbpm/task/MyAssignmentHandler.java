package cn.itcast.sh05.jbpm.task;

import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;

public class MyAssignmentHandler implements AssignmentHandler{
	/**
	 * execution  ��ǰ������ʵ��
	 */
	@Override
	public void assign(Assignable assignable, OpenExecution execution)
			throws Exception {
		String user = execution.getVariable("user").toString();
		assignable.setAssignee(user);
	}
}
