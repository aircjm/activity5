package cn.itcast.e_historyQuery;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;

public class HistoryQueryTest {
	
	//创建工作流的核心对象（流程引擎）
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**查询历史流程实例*/
	@Test
	public void findHistoryProcessInstance(){
		//流程实例ID
		String processInstanceId = "2101";
		HistoricProcessInstance hpi = processEngine.getHistoryService()//
						.createHistoricProcessInstanceQuery()//
						.processInstanceId(processInstanceId)//
						.singleResult();
		System.out.println(hpi.getId()+"    "+hpi.getStartTime()+"     "+hpi.getEndTime()+"   "+hpi.getDurationInMillis());
	}
	
	/**查询历史任务*/
	/**历史任务*/
	@Test
	public void findHistoryTask(){
		//流程实例ID
		String processInstanceId = "2101";
		List<HistoricTaskInstance> list = processEngine.getHistoryService()//
					.createHistoricTaskInstanceQuery()//创建历史任务实例，操作历史任务表
					.processInstanceId(processInstanceId)
					.list();
		if(list!=null && list.size()>0){
			for(HistoricTaskInstance hti:list){
				System.out.println("任务ID："+hti.getId());
				System.out.println("任务的办理人："+hti.getAssignee());
				System.out.println("流程实例ID："+hti.getProcessInstanceId());
				System.out.println(hti.getStartTime()+"      "+hti.getEndTime()+"       "+hti.getDurationInMillis());
				System.out.println("##########################3");
			}
		}
		
	}
	
	/**查询历史活动*/
	@Test
	public void findHistoryActivity(){
		//流程实例ID
		String processInstanceId = "1601";
		List<HistoricActivityInstance> list = processEngine.getHistoryService()//
								.createHistoricActivityInstanceQuery()//
								.processInstanceId(processInstanceId)//
								.list();
		if(list!=null && list.size()>0){
			for(HistoricActivityInstance hai:list){
				System.out.println(hai.getId()+"   "+hai.getActivityName()+"   "+hai.getStartTime()+"   "+hai.getEndTime()+"   "+hai.getDurationInMillis());
				
			}
		}
	}
	
	
	
	/**查询历史的流程变量*/
	@Test
	public void findHistoryVariables(){
		String processInstanceId = "1601";
		List<HistoricVariableInstance> list = processEngine.getHistoryService()//
						.createHistoricVariableInstanceQuery()//
						.processInstanceId(processInstanceId)//
						.list();
		if(list!=null && list.size()>0){
			for(HistoricVariableInstance hvi:list){
				System.out.println(hvi.getId()+"    "+hvi.getProcessInstanceId()+"   "+hvi.getVariableName()+"   "+hvi.getValue()+"    "+hvi.getVariableTypeName());
			}
		}
	}
	
	
}
