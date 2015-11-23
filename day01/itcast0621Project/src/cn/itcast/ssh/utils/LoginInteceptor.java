package cn.itcast.ssh.utils;

import cn.itcast.ssh.domain.Employee;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;


/**
 * 登录验证拦截器
 *
 */
@SuppressWarnings("serial")
public class LoginInteceptor implements Interceptor {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	/**
	 * 控制项目中的session是否存在，如果存在调用invocation.invoke()对应的Action类，
	 * 如果不存在就跳转到登录页面
	 * */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		//获取当前访问的ActionName（连接）
		String actionName = invocation.getProxy().getActionName();
		if(actionName!=null && !actionName.equals("loginAction_login")){
			//判断Session是否存在
			Employee employee = SessionContext.get();
			//如果Session不存在，返回到登陆页面（return "login")
			if(employee==null){
				return "login";
			}
		}
		return invocation.invoke();//调用Action的类，并返回结果
		
	}

}
