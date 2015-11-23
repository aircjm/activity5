package cn.itcast.ssh.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import cn.itcast.ssh.dao.IEmployeeDao;
import cn.itcast.ssh.domain.Employee;

public class EmployeeDaoImpl extends HibernateDaoSupport implements IEmployeeDao {
	
	/**使用当前登录名，获取员工信息*/
	@Override
	public Employee findEmployeeByName(String name) {
		String hql = "from Employee o where o.name = ?";
		List<Employee> list = this.getHibernateTemplate().find(hql, name);
		//判断返回对象
		Employee employee = null;
		if(list!=null && list.size()>0){
			employee = list.get(0);
		}
		return employee;
	}
}
