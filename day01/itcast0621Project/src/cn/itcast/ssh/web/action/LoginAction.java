package cn.itcast.ssh.web.action;

import cn.itcast.ssh.domain.Employee;
import cn.itcast.ssh.service.IEmployeeService;
import cn.itcast.ssh.utils.SessionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public class LoginAction extends ActionSupport implements ModelDriven<Employee> {

	private Employee employee = new Employee();
	
	@Override
	public Employee getModel() {
		return employee;
	}
	
	private IEmployeeService employeeService;

	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}


	/**
	 * 登录
	 * @return
	 */
	public String login(){
		//1：从从业员中获取当前登录名
		String name = employee.getName();
		//2：使用登录名作为查询条件，查询员工表，获取当前员工对象
		Employee emp = employeeService.findEmployeeByName(name);
		//3：如果对象不为null，将员工对象放置到Session中
		if(emp!=null){
			SessionContext.setUser(emp);
			return "success";
		}
		//如果对象为null，返回到登陆页面，并提示错误
		else{
			this.addFieldError("error", "当前登录名不存在");
			return "login";
		}
		
	}
	
	/**
	 * 标题
	 * @return
	 */
	public String top() {
		return "top";
	}
	
	/**
	 * 左侧菜单
	 * @return
	 */
	public String left() {
		return "left";
	}
	
	/**
	 * 主页显示
	 * @return
	 */
	public String welcome() {
		return "welcome";
	}
	
	/**重新登录*/
	public String logout(){
		//清空Session
		SessionContext.setUser(null);
		return "login";
	}
}
