package cn.itcast.ssh.utils;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.itcast.ssh.domain.Employee;
import cn.itcast.ssh.service.IEmployeeService;

/**
 * 员工经理任务分配
 *
 */
@SuppressWarnings("serial")
public class ManagerTaskHandler implements TaskListener {

	@Override
	public void notify(DelegateTask delegateTask) {
		//从Session中获取当前登录人的信息
		Employee employee = SessionContext.get();
		//获取当前登录名
		String name = employee.getName();
		//使用登录名作为查询条件，查询当前登录名所对应的用户
		WebApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
		IEmployeeService employeeService = (IEmployeeService) ac.getBean("employeeService");
		Employee emp = employeeService.findEmployeeByName(name);
		//通过查询获取的用户，指定任务的办理人
		delegateTask.setAssignee(emp.getManager().getName());
	}

}
