package cn.itcast.sh05.jbpm.decision;

import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;

public class MyHandler implements DecisionHandler{

	@Override
	public String decide(OpenExecution execution) {
		String outin = (String)execution.getVariable("outin");
		if(outin.equals("a")){
			return "to task2";
		}else{
			return "to end1";
		}
	}
}
