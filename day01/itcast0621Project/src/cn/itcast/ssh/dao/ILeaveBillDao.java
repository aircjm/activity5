package cn.itcast.ssh.dao;

import java.util.List;

import cn.itcast.ssh.domain.LeaveBill;



public interface ILeaveBillDao {

	List<LeaveBill> findLeaveBillList();

	void saveLeaveBill(LeaveBill leaveBill);

	LeaveBill findLeaveBillByID(Long id);

	void updateLeaveBill(LeaveBill leaveBill);

	void deleteLeaveBillByID(Long id);


}
