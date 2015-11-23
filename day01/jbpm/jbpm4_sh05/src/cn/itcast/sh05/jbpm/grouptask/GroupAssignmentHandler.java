package cn.itcast.sh05.jbpm.grouptask;

import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;

public class GroupAssignmentHandler implements AssignmentHandler{

	@Override
	public void assign(Assignable assignable, OpenExecution execution)
			throws Exception {
		assignable.addCandidateUser("");
		assignable.addCandidateGroup("");
	}
}
