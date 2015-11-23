package cn.itcast.k_personTask02;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@SuppressWarnings("serial")
public class TaskListenerImpl implements TaskListener {

	/**通过实现TaskListener接口的notify方法，指定任务（个人任务和组任务）的办理人*/
	@Override
	public void notify(DelegateTask delegateTask) {
		//指定任务的办理人
		delegateTask.setAssignee("黄老邪");
	}

}
