package cn.itcast.ssh.service.impl;

import java.util.List;

import cn.itcast.ssh.dao.ILeaveBillDao;
import cn.itcast.ssh.domain.Employee;
import cn.itcast.ssh.domain.LeaveBill;
import cn.itcast.ssh.service.ILeaveBillService;
import cn.itcast.ssh.utils.SessionContext;

public class LeaveBillServiceImpl implements ILeaveBillService {

	private ILeaveBillDao leaveBillDao;

	public void setLeaveBillDao(ILeaveBillDao leaveBillDao) {
		this.leaveBillDao = leaveBillDao;
	}

	/**查询所有的请假单*/
	@Override
	public List<LeaveBill> findLeaveBillList() {
		List<LeaveBill> list = leaveBillDao.findLeaveBillList();
		return list;
	}
	
	/**保存请假单*/
	@Override
	public void saveLeaveBill(LeaveBill leaveBill) {
		//获取请假单ID
		Long id = leaveBill.getId();
		//如果请假单ID==null：执行save的操作
		if(id==null){
			//从Session中获取当前登录人，放置到LeaveBill对象中(如果不放置，此时请假单表的user_id将为null)
			Employee employee = SessionContext.get();
			leaveBill.setUser(employee);
			//保存
			leaveBillDao.saveLeaveBill(leaveBill);
		}
		//如果请假单ID!=null:使用id，执行更新LeaveBill对象（update()）
		else{
			leaveBillDao.updateLeaveBill(leaveBill);
		}
		
	}
	
	/**使用主键ID，查询请假单信息*/
	@Override
	public LeaveBill findLeaveBillByID(Long id) {
		LeaveBill bill = leaveBillDao.findLeaveBillByID(id);
		return bill;
	}
	
	/**使用请假单ID，删除请假单对象*/
	@Override
	public void deleteLeaveBillByID(Long id) {
		leaveBillDao.deleteLeaveBillByID(id);
	}
}
