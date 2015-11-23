package cn.itcast.ssh.web.action;

import java.util.List;

import cn.itcast.ssh.domain.LeaveBill;
import cn.itcast.ssh.service.ILeaveBillService;
import cn.itcast.ssh.utils.ValueContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("serial")
public class LeaveBillAction extends ActionSupport implements ModelDriven<LeaveBill> {

	private LeaveBill leaveBill = new LeaveBill();
	
	@Override
	public LeaveBill getModel() {
		return leaveBill;
	}
	
	private ILeaveBillService leaveBillService;

	public void setLeaveBillService(ILeaveBillService leaveBillService) {
		this.leaveBillService = leaveBillService;
	}

	/**
	 * 请假管理首页显示
	 * @return
	 */
	public String home(){
		//查询所有的请假单，返回List<LeaveBill>
		List<LeaveBill> list = leaveBillService.findLeaveBillList();
		//放置到ActionContext对象中
		ValueContext.putValueContext("list", list);
		return "home";
	}
	
	/**
	 * 添加请假申请
	 * @return
	 */
	public String input(){
		//获取请假单ID
		Long id = leaveBill.getId();
		//跳转到编辑页面，如果请假单ID不为null，使用请假单ID查询请假单
		if(id!=null){
			LeaveBill bill = leaveBillService.findLeaveBillByID(id);
			//放入栈顶
			ValueContext.putValueStack(bill);
		}
		return "input";
	}
	
	/**
	 * 保存/更新，请假申请
	 * 
	 * */
	public String save() {
		//1：获取页面中传递值，组织保存的PO对象，执行保存（save()）
		leaveBillService.saveLeaveBill(leaveBill);
		return "save";
	}
	
	/**
	 * 删除，请假申请
	 * 
	 * */
	public String delete(){
		//获取请假单ID
		Long id = leaveBill.getId();
		//执行删除
		leaveBillService.deleteLeaveBillByID(id);
		return "save";
	}
	
}
