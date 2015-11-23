package cn.itcast.sh05.jbpm.identity;

import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;

public class IdentityAssignmentHandler implements AssignmentHandler{
	/**
	 * execution  当前的流程实例
	 */
	@Override
	public void assign(Assignable assignable, OpenExecution execution)
			throws Exception {
		assignable.addCandidateGroup("咨询部");
	}
}
