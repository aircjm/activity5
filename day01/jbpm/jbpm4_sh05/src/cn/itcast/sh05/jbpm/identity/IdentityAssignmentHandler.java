package cn.itcast.sh05.jbpm.identity;

import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;

public class IdentityAssignmentHandler implements AssignmentHandler{
	/**
	 * execution  ��ǰ������ʵ��
	 */
	@Override
	public void assign(Assignable assignable, OpenExecution execution)
			throws Exception {
		assignable.addCandidateGroup("��ѯ��");
	}
}
