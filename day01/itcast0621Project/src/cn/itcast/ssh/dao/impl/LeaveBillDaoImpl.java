package cn.itcast.ssh.dao.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import cn.itcast.ssh.dao.ILeaveBillDao;
import cn.itcast.ssh.domain.LeaveBill;

public class LeaveBillDaoImpl extends HibernateDaoSupport implements ILeaveBillDao {


	/**查询所有的请假单*/
	@Override
	public List<LeaveBill> findLeaveBillList() {
		return this.getHibernateTemplate().find("from LeaveBill");
	}
	
	/**保存请假单*/
	@Override
	public void saveLeaveBill(LeaveBill leaveBill) {
		this.getHibernateTemplate().save(leaveBill);
	}
	
	/**使用请假单ID，查询请假单对象*/
	@Override
	public LeaveBill findLeaveBillByID(Long id) {
		return this.getHibernateTemplate().get(LeaveBill.class, id);
	}
	
	/**更新请假单*/
	@Override
	public void updateLeaveBill(LeaveBill leaveBill) {
		this.getHibernateTemplate().update(leaveBill);
	}
	
	/**使用请假单ID，删除请假单对象*/
	@Override
	public void deleteLeaveBillByID(Long id) {
		//1：获取请假单ID，查询请假单对象
		LeaveBill leaveBill = this.findLeaveBillByID(id);
		//2：执行删除
		this.getHibernateTemplate().delete(leaveBill);
	}
}
